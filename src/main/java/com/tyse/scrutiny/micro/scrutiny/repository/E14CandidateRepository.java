package com.tyse.scrutiny.micro.scrutiny.repository;

import com.tyse.scrutiny.micro.scrutiny.domain.E14Candidate;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * R2DBC repository for the E14Candidate entity.
 */
@Repository
public interface E14CandidateRepository extends R2dbcRepository<E14Candidate, Long> {

    /**
     * Find all candidates for a party.
     */
    Flux<E14Candidate> findByE14PartyId(Long e14PartyId);

    /**
     * Find a candidate by party and candidate ID.
     */
    Mono<E14Candidate> findByE14PartyIdAndCandidateId(Long e14PartyId, String candidateId);

    /**
     * Delete all candidates for a party.
     */
    Mono<Void> deleteByE14PartyId(Long e14PartyId);

    /**
     * Find candidates that need audit for a party.
     */
    Flux<E14Candidate> findByE14PartyIdAndNeedsAuditTrue(Long e14PartyId);
}
