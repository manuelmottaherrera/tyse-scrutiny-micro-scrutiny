package com.tyse.scrutiny.micro.scrutiny.service;

import com.tyse.scrutiny.micro.scrutiny.broker.AnomalyProducer;
import com.tyse.scrutiny.micro.scrutiny.broker.ProcessingStatusProducer;
import com.tyse.scrutiny.micro.scrutiny.broker.event.AnomalyDetectedEvent;
import com.tyse.scrutiny.micro.scrutiny.domain.*;
import com.tyse.scrutiny.micro.scrutiny.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Service for detecting anomalies in electoral data.
 * Compares E14 scrutiny results with precount data.
 */
@Service
@Transactional
public class AnomalyDetectionService {

    private static final Logger LOG = LoggerFactory.getLogger(AnomalyDetectionService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final AnomalyRepository anomalyRepository;
    private final PrecountRepository precountRepository;
    private final PrecountCandidateRepository precountCandidateRepository;
    private final E14PartyRepository e14PartyRepository;
    private final E14CandidateRepository e14CandidateRepository;
    private final ScrutinyDayRepository scrutinyDayRepository;
    private final AnomalyProducer anomalyProducer;
    private final ProcessingStatusProducer processingStatusProducer;

    public AnomalyDetectionService(
        AnomalyRepository anomalyRepository,
        PrecountRepository precountRepository,
        PrecountCandidateRepository precountCandidateRepository,
        E14PartyRepository e14PartyRepository,
        E14CandidateRepository e14CandidateRepository,
        ScrutinyDayRepository scrutinyDayRepository,
        AnomalyProducer anomalyProducer,
        ProcessingStatusProducer processingStatusProducer
    ) {
        this.anomalyRepository = anomalyRepository;
        this.precountRepository = precountRepository;
        this.precountCandidateRepository = precountCandidateRepository;
        this.e14PartyRepository = e14PartyRepository;
        this.e14CandidateRepository = e14CandidateRepository;
        this.scrutinyDayRepository = scrutinyDayRepository;
        this.anomalyProducer = anomalyProducer;
        this.processingStatusProducer = processingStatusProducer;
    }

    /**
     * Detect anomalies for a newly persisted E14 form.
     */
    public Mono<Void> detectAnomalies(E14Form e14Form, Long electionProcessId) {
        LOG.info("Starting anomaly detection for E14: {} divipolKey: {}",
            e14Form.getId(), e14Form.getDivipolKey());

        return detectPrecountDifferences(e14Form, electionProcessId)
            .then(detectVotesExceedVoters(e14Form, electionProcessId))
            .then(detectSumMismatch(e14Form, electionProcessId))
            .doOnSuccess(v -> {
                LOG.info("Anomaly detection completed for E14: {}", e14Form.getId());
                processingStatusProducer.publishAnalyzed(e14Form.getPdfId());
            })
            .doOnError(e -> {
                LOG.error("Anomaly detection failed for E14: {}: {}", e14Form.getId(), e.getMessage());
                processingStatusProducer.publishFailed(e14Form.getPdfId(), "ANALYZED", e.getMessage());
            });
    }

    /**
     * Detect differences between precount and scrutiny.
     * This is the main anomaly type - "votes disappeared".
     */
    private Mono<Void> detectPrecountDifferences(E14Form e14Form, Long electionProcessId) {
        return precountRepository.findByElectionProcessIdAndDivipolKey(
            electionProcessId, e14Form.getDivipolKey()
        ).flatMap(precount ->
            e14PartyRepository.findByE14FormId(e14Form.getId())
                .flatMap(e14Party ->
                    e14CandidateRepository.findByE14PartyId(e14Party.getId())
                        .flatMap(e14Candidate ->
                            precountCandidateRepository.findForComparison(
                                electionProcessId,
                                e14Form.getDivipolKey(),
                                e14Party.getPartyNumber(),
                                e14Candidate.getCandidateId()
                            ).flatMap(precountCandidate -> {
                                int difference = e14Candidate.getVotes() - precountCandidate.getVotes();

                                // Only report if scrutiny has LESS votes than precount
                                if (difference < 0) {
                                    return createAnomaly(
                                        electionProcessId,
                                        "PRECOUNT_DIFFERENCE",
                                        calculateSeverity(Math.abs(difference)),
                                        e14Form,
                                        e14Party.getPartyNumber(),
                                        e14Candidate.getCandidateId(),
                                        precountCandidate.getFirstName(),
                                        precountCandidate.getLastName(),
                                        precountCandidate.getVotes(),
                                        e14Candidate.getVotes(),
                                        difference
                                    );
                                }
                                return Mono.empty();
                            })
                        )
                )
                .then()
        ).switchIfEmpty(Mono.empty());
    }

    /**
     * Detect if total votes exceed registered voters.
     */
    private Mono<Void> detectVotesExceedVoters(E14Form e14Form, Long electionProcessId) {
        if (e14Form.getTotalVoters() == null || e14Form.getTotalBallotBoxVotes() == null) {
            return Mono.empty();
        }

        if (e14Form.getTotalBallotBoxVotes() > e14Form.getTotalVoters()) {
            int difference = e14Form.getTotalBallotBoxVotes() - e14Form.getTotalVoters();
            return createAnomaly(
                electionProcessId,
                "VOTES_EXCEED_VOTERS",
                "HIGH",
                e14Form,
                null,
                null,
                null,
                null,
                e14Form.getTotalVoters(),
                e14Form.getTotalBallotBoxVotes(),
                difference
            ).then();
        }

        return Mono.empty();
    }

    /**
     * Detect if party vote sums don't match.
     */
    private Mono<Void> detectSumMismatch(E14Form e14Form, Long electionProcessId) {
        return e14PartyRepository.findByE14FormId(e14Form.getId())
            .flatMap(party -> {
                Integer partyTotal = party.getTotalPartyCandidateVotes();
                if (partyTotal == null) {
                    return Mono.empty();
                }

                return e14CandidateRepository.findByE14PartyId(party.getId())
                    .map(c -> c.getVotes() != null ? c.getVotes() : 0)
                    .reduce(0, Integer::sum)
                    .flatMap(candidateSum -> {
                        if (!candidateSum.equals(partyTotal)) {
                            int difference = candidateSum - partyTotal;
                            return createAnomaly(
                                electionProcessId,
                                "SUM_MISMATCH",
                                "MEDIUM",
                                e14Form,
                                party.getPartyNumber(),
                                null,
                                null,
                                null,
                                partyTotal,
                                candidateSum,
                                difference
                            );
                        }
                        return Mono.empty();
                    });
            })
            .then();
    }

    /**
     * Create and save an anomaly, then publish to Kafka.
     */
    private Mono<Anomaly> createAnomaly(
        Long electionProcessId,
        String type,
        String severity,
        E14Form e14Form,
        String partyNumber,
        String candidateId,
        String candidateFirstName,
        String candidateLastName,
        Integer precountVotes,
        Integer scrutinyVotes,
        Integer difference
    ) {
        Anomaly anomaly = new Anomaly()
            .electionProcessId(electionProcessId)
            .type(type)
            .severity(severity)
            .divipolKey(e14Form.getDivipolKey())
            .depCode(e14Form.getDepCode())
            .munCode(e14Form.getMunCode())
            .votingTable(e14Form.getVotingTable())
            .partyNumber(partyNumber)
            .candidateId(candidateId)
            .candidateFirstName(candidateFirstName)
            .candidateLastName(candidateLastName)
            .precountVotes(precountVotes)
            .scrutinyVotes(scrutinyVotes)
            .difference(difference)
            .scrutinyDayId(e14Form.getScrutinyDayId())
            .status("NEW")
            .detectedAt(Instant.now());

        return anomalyRepository.save(anomaly)
            .doOnSuccess(saved -> {
                LOG.info("Anomaly detected: type={}, severity={}, divipolKey={}, difference={}",
                    type, severity, e14Form.getDivipolKey(), difference);

                // Publish to Kafka for notifications
                anomalyProducer.publishAnomaly(AnomalyDetectedEvent.from(
                    saved.getId(),
                    type,
                    severity,
                    e14Form.getDivipolKey(),
                    e14Form.getVotingTable(),
                    partyNumber,
                    candidateId,
                    candidateFirstName,
                    candidateLastName,
                    precountVotes,
                    scrutinyVotes,
                    difference,
                    LocalDate.now().format(DATE_FORMATTER),
                    electionProcessId
                ));
            });
    }

    /**
     * Calculate severity based on vote difference.
     */
    private String calculateSeverity(int difference) {
        if (difference >= 50) {
            return "CRITICAL";
        } else if (difference >= 20) {
            return "HIGH";
        } else if (difference >= 5) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
}
