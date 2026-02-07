package com.tyse.scrutiny.micro.scrutiny.broker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyse.scrutiny.micro.scrutiny.broker.event.ProcessingStatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Kafka producer for e14-processing-status topic.
 * Publishes processing status updates.
 */
@Component
public class ProcessingStatusProducer {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessingStatusProducer.class);
    private static final String BINDING_NAME = "processingStatusProducer-out-0";

    private final StreamBridge streamBridge;
    private final ObjectMapper objectMapper;

    public ProcessingStatusProducer(StreamBridge streamBridge, ObjectMapper objectMapper) {
        this.streamBridge = streamBridge;
        this.objectMapper = objectMapper;
    }

    /**
     * Publish a processing status event.
     */
    public void publishStatus(ProcessingStatusEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            LOG.debug("Publishing processing status to Kafka: pdfId={}, stage={}, status={}",
                event.pdfId(), event.stage(), event.status());
            streamBridge.send(BINDING_NAME, json);
        } catch (JsonProcessingException e) {
            LOG.error("Failed to serialize ProcessingStatusEvent: {}", e.getMessage(), e);
        }
    }

    /**
     * Publish a "persisted" status.
     */
    public void publishPersisted(UUID pdfId) {
        publishStatus(ProcessingStatusEvent.completed(pdfId, "PERSISTED", "E14 data persisted to database"));
    }

    /**
     * Publish an "analyzed" status.
     */
    public void publishAnalyzed(UUID pdfId) {
        publishStatus(ProcessingStatusEvent.completed(pdfId, "ANALYZED", "Anomaly detection completed"));
    }

    /**
     * Publish a "failed" status.
     */
    public void publishFailed(UUID pdfId, String stage, String message) {
        publishStatus(ProcessingStatusEvent.failed(pdfId, stage, message));
    }
}
