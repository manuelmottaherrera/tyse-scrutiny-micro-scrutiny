package com.tyse.scrutiny.micro.scrutiny.service;

import com.tyse.scrutiny.micro.scrutiny.broker.ProcessingStatusProducer;
import com.tyse.scrutiny.micro.scrutiny.broker.event.E14CleanedEvent;
import com.tyse.scrutiny.micro.scrutiny.domain.*;
import com.tyse.scrutiny.micro.scrutiny.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Service for persisting E14 data received from Kafka.
 */
@Service
@Transactional
public class E14PersistenceService {

    private static final Logger LOG = LoggerFactory.getLogger(E14PersistenceService.class);

    private final E14FormRepository e14FormRepository;
    private final E14PartyRepository e14PartyRepository;
    private final E14CandidateRepository e14CandidateRepository;
    private final ElectionProcessRepository electionProcessRepository;
    private final ScrutinyDayService scrutinyDayService;
    private final ProcessedDocumentRepository processedDocumentRepository;
    private final ProcessingStatusProducer processingStatusProducer;
    private final AnomalyDetectionService anomalyDetectionService;

    public E14PersistenceService(
        E14FormRepository e14FormRepository,
        E14PartyRepository e14PartyRepository,
        E14CandidateRepository e14CandidateRepository,
        ElectionProcessRepository electionProcessRepository,
        ScrutinyDayService scrutinyDayService,
        ProcessedDocumentRepository processedDocumentRepository,
        ProcessingStatusProducer processingStatusProducer,
        AnomalyDetectionService anomalyDetectionService
    ) {
        this.e14FormRepository = e14FormRepository;
        this.e14PartyRepository = e14PartyRepository;
        this.e14CandidateRepository = e14CandidateRepository;
        this.electionProcessRepository = electionProcessRepository;
        this.scrutinyDayService = scrutinyDayService;
        this.processedDocumentRepository = processedDocumentRepository;
        this.processingStatusProducer = processingStatusProducer;
        this.anomalyDetectionService = anomalyDetectionService;
    }

    /**
     * Persist an E14 form from a Kafka event.
     */
    public Mono<E14Form> persistE14(E14CleanedEvent event) {
        LOG.info("Persisting E14 for divipolKey: {}", event.divipolKey());

        return electionProcessRepository.findFirstByActiveTrueOrderByElectionDateDesc()
            .switchIfEmpty(Mono.error(new IllegalStateException("No active election process found")))
            .flatMap(electionProcess ->
                scrutinyDayService.getOrCreateForToday(electionProcess.getId())
                    .flatMap(scrutinyDay -> {
                        // Check if E14 already exists for this divipol key and scrutiny day
                        return e14FormRepository.existsByElectionProcessIdAndDivipolKeyAndScrutinyDayId(
                            electionProcess.getId(),
                            event.divipolKey(),
                            scrutinyDay.getId()
                        ).flatMap(exists -> {
                            if (exists) {
                                LOG.warn("E14 already exists for divipolKey: {} on scrutinyDay: {}",
                                    event.divipolKey(), scrutinyDay.getLabel());
                                return e14FormRepository.findByElectionProcessIdAndDivipolKeyAndScrutinyDayId(
                                    electionProcess.getId(),
                                    event.divipolKey(),
                                    scrutinyDay.getId()
                                );
                            }

                            // Create new E14 form
                            E14Form form = new E14Form()
                                .electionProcessId(electionProcess.getId())
                                .pdfId(event.pdfId())
                                .depCode(event.depCode())
                                .munCode(event.munCode())
                                .zone(event.zone())
                                .station(event.station())
                                .votingTable(event.table())
                                .divipolKey(event.divipolKey())
                                .totalVoters(event.totalVoters())
                                .totalBallotBoxVotes(event.totalBallotBoxVotes())
                                .totalIncinerated(event.totalIncinerated())
                                .pagesReceived(event.pagesReceived())
                                .totalPages(event.totalPages())
                                .isComplete(event.isComplete())
                                .scrutinyDayId(scrutinyDay.getId())
                                .ocrConfidence(event.ocrConfidence())
                                .processedAt(event.processedAt() != null ? event.processedAt() : Instant.now())
                                .createdDate(Instant.now());

                            return e14FormRepository.save(form)
                                .flatMap(savedForm -> persistParties(savedForm, event)
                                    .then(Mono.just(savedForm)))
                                .doOnSuccess(savedForm -> {
                                    // Publish status
                                    processingStatusProducer.publishPersisted(event.pdfId());

                                    // Trigger anomaly detection
                                    anomalyDetectionService.detectAnomalies(savedForm, electionProcess.getId())
                                        .subscribe();
                                })
                                .doOnError(e -> processingStatusProducer.publishFailed(
                                    event.pdfId(), "PERSISTED", e.getMessage()));
                        });
                    })
            );
    }

    /**
     * Persist parties for an E14 form.
     */
    private Mono<Void> persistParties(E14Form form, E14CleanedEvent event) {
        if (event.parties() == null || event.parties().isEmpty()) {
            return Mono.empty();
        }

        return Flux.fromIterable(event.parties())
            .flatMap(partyData -> {
                E14Party party = new E14Party()
                    .e14FormId(form.getId())
                    .partyNumber(partyData.partyNumber())
                    .partyName(partyData.name())
                    .voteType(partyData.voteType())
                    .partyOnlyVotes(partyData.partyOnlyVotes())
                    .totalPartyCandidateVotes(partyData.totalVotes())
                    .needsAudit(partyData.needsAudit());

                return e14PartyRepository.save(party)
                    .flatMap(savedParty -> persistCandidates(savedParty, partyData));
            })
            .then();
    }

    /**
     * Persist candidates for a party.
     */
    private Mono<Void> persistCandidates(E14Party party, E14CleanedEvent.PartyData partyData) {
        if (partyData.candidates() == null || partyData.candidates().isEmpty()) {
            return Mono.empty();
        }

        return Flux.fromIterable(partyData.candidates())
            .flatMap(candidateData -> {
                E14Candidate candidate = new E14Candidate()
                    .e14PartyId(party.getId())
                    .candidateId(candidateData.id())
                    .votes(candidateData.votes())
                    .needsAudit(candidateData.needsAudit());

                return e14CandidateRepository.save(candidate);
            })
            .then();
    }

    /**
     * Update document processing status.
     */
    public Mono<ProcessedDocument> updateDocumentStatus(
        java.util.UUID pdfId,
        String stage,
        String status
    ) {
        return processedDocumentRepository.findByPdfId(pdfId)
            .flatMap(doc -> {
                doc.setStage(stage);
                doc.setStatus(status);
                doc.setLastModified(Instant.now());

                switch (stage) {
                    case "PERSISTED" -> doc.setPersistedAt(Instant.now());
                    case "ANALYZED" -> doc.setAnalyzedAt(Instant.now());
                }

                return processedDocumentRepository.save(doc);
            })
            .switchIfEmpty(Mono.defer(() -> {
                // Create new document record if it doesn't exist
                ProcessedDocument doc = new ProcessedDocument()
                    .pdfId(pdfId)
                    .stage(stage)
                    .status(status)
                    .createdDate(Instant.now())
                    .lastModified(Instant.now());

                if ("PERSISTED".equals(stage)) {
                    doc.setPersistedAt(Instant.now());
                }

                return processedDocumentRepository.save(doc);
            }));
    }
}
