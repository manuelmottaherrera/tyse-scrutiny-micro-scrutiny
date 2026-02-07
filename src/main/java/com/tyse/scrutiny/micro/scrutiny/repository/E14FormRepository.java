package com.tyse.scrutiny.micro.scrutiny.repository;

import com.tyse.scrutiny.micro.scrutiny.domain.E14Form;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * R2DBC repository for the E14Form entity.
 */
@Repository
public interface E14FormRepository extends R2dbcRepository<E14Form, Long> {

    /**
     * Find E14 forms by election process with pagination.
     */
    Flux<E14Form> findByElectionProcessId(Long electionProcessId, Pageable pageable);

    /**
     * Find E14 form by PDF ID.
     */
    Mono<E14Form> findByPdfId(UUID pdfId);

    /**
     * Find E14 form by divipol key and election process.
     */
    Mono<E14Form> findByElectionProcessIdAndDivipolKeyAndScrutinyDayId(
        Long electionProcessId, String divipolKey, Long scrutinyDayId);

    /**
     * Find E14 forms by department code.
     */
    Flux<E14Form> findByElectionProcessIdAndDepCode(Long electionProcessId, String depCode, Pageable pageable);

    /**
     * Find E14 forms by municipality code.
     */
    Flux<E14Form> findByElectionProcessIdAndDepCodeAndMunCode(
        Long electionProcessId, String depCode, String munCode, Pageable pageable);

    /**
     * Find incomplete E14 forms.
     */
    Flux<E14Form> findByElectionProcessIdAndIsCompleteFalse(Long electionProcessId);

    /**
     * Count E14 forms by election process.
     */
    Mono<Long> countByElectionProcessId(Long electionProcessId);

    /**
     * Count complete E14 forms by election process.
     */
    Mono<Long> countByElectionProcessIdAndIsCompleteTrue(Long electionProcessId);

    /**
     * Check if E14 form exists by divipol key.
     */
    Mono<Boolean> existsByElectionProcessIdAndDivipolKeyAndScrutinyDayId(
        Long electionProcessId, String divipolKey, Long scrutinyDayId);
}
