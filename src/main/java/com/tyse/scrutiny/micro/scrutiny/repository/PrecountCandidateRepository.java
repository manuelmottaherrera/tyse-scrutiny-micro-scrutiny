package com.tyse.scrutiny.micro.scrutiny.repository;

import com.tyse.scrutiny.micro.scrutiny.domain.PrecountCandidate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * R2DBC repository for the PrecountCandidate entity.
 */
@Repository
public interface PrecountCandidateRepository extends R2dbcRepository<PrecountCandidate, Long> {

    /**
     * Find all candidates for a precount.
     */
    Flux<PrecountCandidate> findByPrecountId(Long precountId);

    /**
     * Find candidate by precount, party and candidate ID.
     */
    Mono<PrecountCandidate> findByPrecountIdAndPartyNumberAndCandidateId(
        Long precountId, String partyNumber, String candidateId);

    /**
     * Find candidates by party number for a precount.
     */
    Flux<PrecountCandidate> findByPrecountIdAndPartyNumber(Long precountId, String partyNumber);

    /**
     * Delete all candidates for a precount.
     */
    Mono<Void> deleteByPrecountId(Long precountId);

    /**
     * Find candidate votes for comparison with scrutiny.
     */
    @Query("""
        SELECT pc.* FROM precount_candidate pc
        JOIN precount p ON pc.precount_id = p.id
        WHERE p.election_process_id = :electionProcessId
        AND p.divipol_key = :divipolKey
        AND pc.party_number = :partyNumber
        AND pc.candidate_id = :candidateId
        """)
    Mono<PrecountCandidate> findForComparison(
        Long electionProcessId, String divipolKey, String partyNumber, String candidateId);
}
