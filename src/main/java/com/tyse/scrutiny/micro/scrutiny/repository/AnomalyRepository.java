package com.tyse.scrutiny.micro.scrutiny.repository;

import com.tyse.scrutiny.micro.scrutiny.domain.Anomaly;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * R2DBC repository for the Anomaly entity.
 */
@Repository
public interface AnomalyRepository extends R2dbcRepository<Anomaly, Long> {

    /**
     * Find anomalies by election process with pagination.
     */
    Flux<Anomaly> findByElectionProcessId(Long electionProcessId, Pageable pageable);

    /**
     * Find anomalies by status.
     */
    Flux<Anomaly> findByElectionProcessIdAndStatus(Long electionProcessId, String status, Pageable pageable);

    /**
     * Find anomalies by type.
     */
    Flux<Anomaly> findByElectionProcessIdAndType(Long electionProcessId, String type, Pageable pageable);

    /**
     * Find anomalies by severity.
     */
    Flux<Anomaly> findByElectionProcessIdAndSeverity(Long electionProcessId, String severity, Pageable pageable);

    /**
     * Find anomalies by divipol key.
     */
    Flux<Anomaly> findByElectionProcessIdAndDivipolKey(Long electionProcessId, String divipolKey);

    /**
     * Find anomalies by department.
     */
    Flux<Anomaly> findByElectionProcessIdAndDepCode(Long electionProcessId, String depCode, Pageable pageable);

    /**
     * Find anomalies by municipality.
     */
    Flux<Anomaly> findByElectionProcessIdAndDepCodeAndMunCode(
        Long electionProcessId, String depCode, String munCode, Pageable pageable);

    /**
     * Count anomalies by status.
     */
    Mono<Long> countByElectionProcessIdAndStatus(Long electionProcessId, String status);

    /**
     * Count anomalies by type.
     */
    Mono<Long> countByElectionProcessIdAndType(Long electionProcessId, String type);

    /**
     * Count anomalies by severity.
     */
    Mono<Long> countByElectionProcessIdAndSeverity(Long electionProcessId, String severity);

    /**
     * Count all anomalies for an election process.
     */
    Mono<Long> countByElectionProcessId(Long electionProcessId);

    /**
     * Find new (unreviewed) anomalies.
     */
    @Query("""
        SELECT * FROM anomaly
        WHERE election_process_id = :electionProcessId
        AND status = 'NEW'
        ORDER BY detected_at DESC
        LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}
        """)
    Flux<Anomaly> findNewAnomalies(Long electionProcessId, Pageable pageable);

    /**
     * Find high severity anomalies.
     */
    @Query("""
        SELECT * FROM anomaly
        WHERE election_process_id = :electionProcessId
        AND severity IN ('HIGH', 'CRITICAL')
        ORDER BY detected_at DESC
        LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}
        """)
    Flux<Anomaly> findHighSeverityAnomalies(Long electionProcessId, Pageable pageable);
}
