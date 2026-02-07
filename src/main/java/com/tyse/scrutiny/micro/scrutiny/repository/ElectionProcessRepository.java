package com.tyse.scrutiny.micro.scrutiny.repository;

import com.tyse.scrutiny.micro.scrutiny.domain.ElectionProcess;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * R2DBC repository for the ElectionProcess entity.
 */
@Repository
public interface ElectionProcessRepository extends R2dbcRepository<ElectionProcess, Long> {

    /**
     * Find all active election processes.
     */
    Flux<ElectionProcess> findByActiveTrue();

    /**
     * Find the current active election process.
     */
    Mono<ElectionProcess> findFirstByActiveTrueOrderByElectionDateDesc();

    /**
     * Find election processes by type.
     */
    Flux<ElectionProcess> findByType(String type);

    /**
     * Find election processes with pagination.
     */
    Flux<ElectionProcess> findAllBy(Pageable pageable);

    /**
     * Count active election processes.
     */
    Mono<Long> countByActiveTrue();
}
