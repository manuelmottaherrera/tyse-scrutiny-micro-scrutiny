package com.tyse.scrutiny.micro.scrutiny.repository;

import com.tyse.scrutiny.micro.scrutiny.domain.ScrutinyResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * R2DBC repository for the ScrutinyResult entity.
 */
@Repository
public interface ScrutinyResultRepository extends R2dbcRepository<ScrutinyResult, Long> {

    /**
     * Find results by election process and scrutiny day.
     */
    Flux<ScrutinyResult> findByElectionProcessIdAndScrutinyDayId(
        Long electionProcessId, Long scrutinyDayId, Pageable pageable);

    /**
     * Find results by divipol key.
     */
    Flux<ScrutinyResult> findByElectionProcessIdAndDivipolKey(Long electionProcessId, String divipolKey);

    /**
     * Find result by unique combination.
     */
    Mono<ScrutinyResult> findByElectionProcessIdAndScrutinyDayIdAndDivipolKeyAndPartyNumberAndCandidateId(
        Long electionProcessId, Long scrutinyDayId, String divipolKey, String partyNumber, String candidateId);

    /**
     * Find results by party.
     */
    Flux<ScrutinyResult> findByElectionProcessIdAndPartyNumber(Long electionProcessId, String partyNumber, Pageable pageable);

    /**
     * Sum votes by party for a scrutiny day.
     */
    @Query("""
        SELECT COALESCE(SUM(votes), 0)
        FROM scrutiny_result
        WHERE election_process_id = :electionProcessId
        AND scrutiny_day_id = :scrutinyDayId
        AND party_number = :partyNumber
        """)
    Mono<Long> sumVotesByParty(Long electionProcessId, Long scrutinyDayId, String partyNumber);

    /**
     * Sum votes by candidate for a scrutiny day.
     */
    @Query("""
        SELECT COALESCE(SUM(votes), 0)
        FROM scrutiny_result
        WHERE election_process_id = :electionProcessId
        AND scrutiny_day_id = :scrutinyDayId
        AND party_number = :partyNumber
        AND candidate_id = :candidateId
        """)
    Mono<Long> sumVotesByCandidate(Long electionProcessId, Long scrutinyDayId, String partyNumber, String candidateId);
}
