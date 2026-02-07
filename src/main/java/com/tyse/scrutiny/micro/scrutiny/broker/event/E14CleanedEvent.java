package com.tyse.scrutiny.micro.scrutiny.broker.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Event received from the e14-data-cleaned Kafka topic.
 * Represents a cleaned E14 form ready for persistence.
 */
public record E14CleanedEvent(
    UUID pdfId,
    String divipolKey,
    String depCode,
    String munCode,
    String zone,
    String station,
    String table,
    Integer totalVoters,
    Integer totalBallotBoxVotes,
    String totalIncinerated,
    List<PartyData> parties,
    Integer pagesReceived,
    Integer totalPages,
    Boolean isComplete,
    BigDecimal ocrConfidence,
    Instant processedAt
) {
    /**
     * Party data within an E14 form.
     */
    public record PartyData(
        String partyNumber,
        String name,
        String voteType,
        Integer partyOnlyVotes,
        Integer totalVotes,
        Boolean needsAudit,
        List<CandidateData> candidates
    ) {}

    /**
     * Candidate data within a party.
     */
    public record CandidateData(
        String id,
        Integer votes,
        Boolean needsAudit
    ) {}
}
