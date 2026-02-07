package com.tyse.scrutiny.micro.scrutiny.broker.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Event published to the e14-processing-status Kafka topic.
 * Represents the processing status of a document.
 */
public record ProcessingStatusEvent(
    UUID pdfId,
    String stage,
    String status,
    String message,
    Instant timestamp
) {
    public static ProcessingStatusEvent inProgress(UUID pdfId, String stage, String message) {
        return new ProcessingStatusEvent(pdfId, stage, "IN_PROGRESS", message, Instant.now());
    }

    public static ProcessingStatusEvent completed(UUID pdfId, String stage, String message) {
        return new ProcessingStatusEvent(pdfId, stage, "COMPLETED", message, Instant.now());
    }

    public static ProcessingStatusEvent failed(UUID pdfId, String stage, String message) {
        return new ProcessingStatusEvent(pdfId, stage, "FAILED", message, Instant.now());
    }
}
