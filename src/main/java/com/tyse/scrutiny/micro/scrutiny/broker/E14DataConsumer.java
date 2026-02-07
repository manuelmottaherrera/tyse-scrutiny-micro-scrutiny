package com.tyse.scrutiny.micro.scrutiny.broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyse.scrutiny.micro.scrutiny.broker.event.E14CleanedEvent;
import com.tyse.scrutiny.micro.scrutiny.service.E14PersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * Kafka consumer for e14-data-cleaned topic.
 * Receives cleaned E14 data and persists it to the database.
 * Uses programmatic MessageListenerContainer to avoid
 * ConcurrentModificationException issues with KafkaBinderMetrics.
 */
@Component
public class E14DataConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(E14DataConsumer.class);

    private final ObjectMapper objectMapper;
    private final E14PersistenceService e14PersistenceService;

    public E14DataConsumer(ObjectMapper objectMapper, E14PersistenceService e14PersistenceService) {
        this.objectMapper = objectMapper;
        this.e14PersistenceService = e14PersistenceService;
        LOG.info("E14DataConsumer bean created - ready to consume from e14-data-cleaned topic");
    }

    @PostConstruct
    public void init() {
        LOG.info("E14DataConsumer initialized - using programmatic MessageListenerContainer");
    }

    /**
     * Called by the programmatic MessageListenerContainer when a message is received.
     */
    public void consume(String message) {
        LOG.info("Received E14 data from Kafka: {}", message.substring(0, Math.min(message.length(), 200)));
        try {
            E14CleanedEvent event = objectMapper.readValue(message, E14CleanedEvent.class);
            LOG.debug("Parsed E14CleanedEvent for divipolKey: {}", event.divipolKey());

            e14PersistenceService.persistE14(event)
                .doOnSuccess(form -> LOG.info("Successfully persisted E14 form: {} for {}", form.getId(), event.divipolKey()))
                .doOnError(e -> LOG.error("Failed to persist E14 form for {}: {}", event.divipolKey(), e.getMessage()))
                .subscribe();

        } catch (Exception e) {
            LOG.error("Failed to parse E14CleanedEvent: {}", e.getMessage(), e);
        }
    }
}
