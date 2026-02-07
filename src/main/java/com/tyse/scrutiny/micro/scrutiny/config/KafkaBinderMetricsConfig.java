package com.tyse.scrutiny.micro.scrutiny.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Placeholder configuration for Kafka Binder Metrics.
 * The actual exclusion is done in application.yml via spring.autoconfigure.exclude.
 */
@Configuration
public class KafkaBinderMetricsConfig {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaBinderMetricsConfig.class);

    public KafkaBinderMetricsConfig() {
        LOG.info("KafkaBinderMetricsConfig loaded - Kafka metrics exclusions configured in application.yml");
    }
}
