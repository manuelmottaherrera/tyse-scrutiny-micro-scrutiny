package com.tyse.scrutiny.micro.scrutiny.service;

import com.tyse.scrutiny.micro.scrutiny.domain.Anomaly;
import com.tyse.scrutiny.micro.scrutiny.repository.AnomalyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Service for managing anomalies.
 */
@Service
@Transactional
public class AnomalyService {

    private static final Logger LOG = LoggerFactory.getLogger(AnomalyService.class);

    private final AnomalyRepository anomalyRepository;

    public AnomalyService(AnomalyRepository anomalyRepository) {
        this.anomalyRepository = anomalyRepository;
    }

    /**
     * Save an anomaly.
     */
    public Mono<Anomaly> save(Anomaly anomaly) {
        LOG.debug("Request to save Anomaly : {}", anomaly);
        if (anomaly.getDetectedAt() == null) {
            anomaly.setDetectedAt(Instant.now());
        }
        if (anomaly.getStatus() == null) {
            anomaly.setStatus("NEW");
        }
        return anomalyRepository.save(anomaly);
    }

    /**
     * Get all anomalies for an election process.
     */
    @Transactional(readOnly = true)
    public Flux<Anomaly> findByElectionProcess(Long electionProcessId, Pageable pageable) {
        LOG.debug("Request to get Anomalies for ElectionProcess : {}", electionProcessId);
        return anomalyRepository.findByElectionProcessId(electionProcessId, pageable);
    }

    /**
     * Get anomalies by status.
     */
    @Transactional(readOnly = true)
    public Flux<Anomaly> findByStatus(Long electionProcessId, String status, Pageable pageable) {
        LOG.debug("Request to get Anomalies by status : {}", status);
        return anomalyRepository.findByElectionProcessIdAndStatus(electionProcessId, status, pageable);
    }

    /**
     * Get anomalies by type.
     */
    @Transactional(readOnly = true)
    public Flux<Anomaly> findByType(Long electionProcessId, String type, Pageable pageable) {
        LOG.debug("Request to get Anomalies by type : {}", type);
        return anomalyRepository.findByElectionProcessIdAndType(electionProcessId, type, pageable);
    }

    /**
     * Get anomalies by severity.
     */
    @Transactional(readOnly = true)
    public Flux<Anomaly> findBySeverity(Long electionProcessId, String severity, Pageable pageable) {
        LOG.debug("Request to get Anomalies by severity : {}", severity);
        return anomalyRepository.findByElectionProcessIdAndSeverity(electionProcessId, severity, pageable);
    }

    /**
     * Get new (unreviewed) anomalies.
     */
    @Transactional(readOnly = true)
    public Flux<Anomaly> findNewAnomalies(Long electionProcessId, Pageable pageable) {
        LOG.debug("Request to get new Anomalies for ElectionProcess : {}", electionProcessId);
        return anomalyRepository.findNewAnomalies(electionProcessId, pageable);
    }

    /**
     * Get high severity anomalies.
     */
    @Transactional(readOnly = true)
    public Flux<Anomaly> findHighSeverity(Long electionProcessId, Pageable pageable) {
        LOG.debug("Request to get high severity Anomalies for ElectionProcess : {}", electionProcessId);
        return anomalyRepository.findHighSeverityAnomalies(electionProcessId, pageable);
    }

    /**
     * Count anomalies by election process.
     */
    @Transactional(readOnly = true)
    public Mono<Long> count(Long electionProcessId) {
        LOG.debug("Request to count Anomalies for ElectionProcess : {}", electionProcessId);
        return anomalyRepository.countByElectionProcessId(electionProcessId);
    }

    /**
     * Get one anomaly by id.
     */
    @Transactional(readOnly = true)
    public Mono<Anomaly> findOne(Long id) {
        LOG.debug("Request to get Anomaly : {}", id);
        return anomalyRepository.findById(id);
    }

    /**
     * Update anomaly status (review workflow).
     */
    public Mono<Anomaly> updateStatus(Long id, String status, String reviewedBy, String notes) {
        LOG.debug("Request to update Anomaly status : {} to {}", id, status);
        return anomalyRepository.findById(id)
            .flatMap(anomaly -> {
                anomaly.setStatus(status);
                anomaly.setReviewedBy(reviewedBy);
                anomaly.setReviewDate(Instant.now());
                if (notes != null) {
                    anomaly.setNotes(notes);
                }
                return anomalyRepository.save(anomaly);
            });
    }

    /**
     * Delete the anomaly by id.
     */
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Anomaly : {}", id);
        return anomalyRepository.deleteById(id);
    }
}
