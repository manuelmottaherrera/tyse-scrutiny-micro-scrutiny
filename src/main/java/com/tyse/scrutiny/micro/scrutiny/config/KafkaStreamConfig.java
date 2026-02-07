package com.tyse.scrutiny.micro.scrutiny.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Kafka Stream bindings.
 * Note: Consumer beans are defined as @Component classes in the broker package.
 * Spring Cloud Stream automatically binds them based on spring.cloud.function.definition.
 */
@Configuration
public class KafkaStreamConfig {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamConfig.class);

    // E14DataConsumer is defined as @Component("e14DataConsumer") in broker package
    // Spring Cloud Stream will bind it to e14DataConsumer-in-0 automatically
}
