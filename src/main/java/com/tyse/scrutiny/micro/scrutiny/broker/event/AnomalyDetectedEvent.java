package com.tyse.scrutiny.micro.scrutiny.broker.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Event published to the e14-anomaly-detected Kafka topic.
 * Represents a detected anomaly in electoral data.
 */
public record AnomalyDetectedEvent(
    UUID anomalyId,
    String type,
    String severity,
    String divipolKey,
    String table,
    String partyNumber,
    String candidateId,
    String candidateFirstName,
    String candidateLastName,
    Integer precountVotes,
    Integer scrutinyVotes,
    Integer difference,
    String scrutinyDate,
    Long electionProcessId,
    Instant detectedAt
) {
    public static AnomalyDetectedEvent from(
        Long anomalyId,
        String type,
        String severity,
        String divipolKey,
        String table,
        String partyNumber,
        String candidateId,
        String candidateFirstName,
        String candidateLastName,
        Integer precountVotes,
        Integer scrutinyVotes,
        Integer difference,
        String scrutinyDate,
        Long electionProcessId
    ) {
        return new AnomalyDetectedEvent(
            UUID.randomUUID(),
            type,
            severity,
            divipolKey,
            table,
            partyNumber,
            candidateId,
            candidateFirstName,
            candidateLastName,
            precountVotes,
            scrutinyVotes,
            difference,
            scrutinyDate,
            electionProcessId,
            Instant.now()
        );
    }
}
