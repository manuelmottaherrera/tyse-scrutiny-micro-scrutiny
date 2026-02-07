package com.tyse.scrutiny.micro.scrutiny.broker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyse.scrutiny.micro.scrutiny.broker.event.AnomalyDetectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

/**
 * Kafka producer for e14-anomaly-detected topic.
 * Publishes detected anomalies for notification.
 */
@Component
public class AnomalyProducer {

    private static final Logger LOG = LoggerFactory.getLogger(AnomalyProducer.class);
    private static final String BINDING_NAME = "anomalyProducer-out-0";

    private final StreamBridge streamBridge;
    private final ObjectMapper objectMapper;

    public AnomalyProducer(StreamBridge streamBridge, ObjectMapper objectMapper) {
        this.streamBridge = streamBridge;
        this.objectMapper = objectMapper;
    }

    /**
     * Publish an anomaly detected event.
     */
    public void publishAnomaly(AnomalyDetectedEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            LOG.info("Publishing anomaly to Kafka: type={}, divipolKey={}", event.type(), event.divipolKey());
            streamBridge.send(BINDING_NAME, json);
            LOG.debug("Published anomaly event: {}", json);
        } catch (JsonProcessingException e) {
            LOG.error("Failed to serialize AnomalyDetectedEvent: {}", e.getMessage(), e);
        }
    }
}
