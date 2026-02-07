package com.tyse.scrutiny.micro.scrutiny.repository;

import com.tyse.scrutiny.micro.scrutiny.domain.E14Party;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * R2DBC repository for the E14Party entity.
 */
@Repository
public interface E14PartyRepository extends R2dbcRepository<E14Party, Long> {

    /**
     * Find all parties for an E14 form.
     */
    Flux<E14Party> findByE14FormId(Long e14FormId);

    /**
     * Find a party by E14 form and party number.
     */
    Mono<E14Party> findByE14FormIdAndPartyNumber(Long e14FormId, String partyNumber);

    /**
     * Delete all parties for an E14 form.
     */
    Mono<Void> deleteByE14FormId(Long e14FormId);

    /**
     * Find parties that need audit for an E14 form.
     */
    Flux<E14Party> findByE14FormIdAndNeedsAuditTrue(Long e14FormId);
}
