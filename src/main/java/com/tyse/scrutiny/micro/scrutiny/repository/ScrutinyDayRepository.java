package com.tyse.scrutiny.micro.scrutiny.repository;

import com.tyse.scrutiny.micro.scrutiny.domain.ScrutinyDay;
import java.time.LocalDate;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * R2DBC repository for the ScrutinyDay entity.
 */
@Repository
public interface ScrutinyDayRepository extends R2dbcRepository<ScrutinyDay, Long> {

    /**
     * Find all scrutiny days for an election process.
     */
    Flux<ScrutinyDay> findByElectionProcessIdOrderByDayNumberAsc(Long electionProcessId);

    /**
     * Find a scrutiny day by election process and date.
     */
    Mono<ScrutinyDay> findByElectionProcessIdAndDate(Long electionProcessId, LocalDate date);

    /**
     * Find the current (latest) scrutiny day for an election process.
     */
    Mono<ScrutinyDay> findFirstByElectionProcessIdOrderByDayNumberDesc(Long electionProcessId);

    /**
     * Count scrutiny days for an election process.
     */
    Mono<Long> countByElectionProcessId(Long electionProcessId);
}
