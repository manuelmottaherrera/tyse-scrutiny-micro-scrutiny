package com.tyse.scrutiny.micro.scrutiny.repository;

import com.tyse.scrutiny.micro.scrutiny.domain.Precount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * R2DBC repository for the Precount entity.
 */
@Repository
public interface PrecountRepository extends R2dbcRepository<Precount, Long> {

    /**
     * Find precount by election process with pagination.
     */
    Flux<Precount> findByElectionProcessId(Long electionProcessId, Pageable pageable);

    /**
     * Find precount by divipol key.
     */
    Mono<Precount> findByElectionProcessIdAndDivipolKey(Long electionProcessId, String divipolKey);

    /**
     * Find precount by department.
     */
    Flux<Precount> findByElectionProcessIdAndDepCode(Long electionProcessId, String depCode, Pageable pageable);

    /**
     * Find precount by municipality.
     */
    Flux<Precount> findByElectionProcessIdAndDepCodeAndMunCode(
        Long electionProcessId, String depCode, String munCode, Pageable pageable);

    /**
     * Count precount entries by election process.
     */
    Mono<Long> countByElectionProcessId(Long electionProcessId);

    /**
     * Check if precount exists for divipol key.
     */
    Mono<Boolean> existsByElectionProcessIdAndDivipolKey(Long electionProcessId, String divipolKey);

    /**
     * Delete all precount entries for an election process.
     */
    Mono<Void> deleteByElectionProcessId(Long electionProcessId);
}
