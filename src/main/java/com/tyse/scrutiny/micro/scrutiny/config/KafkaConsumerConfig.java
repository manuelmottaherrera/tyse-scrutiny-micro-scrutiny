package com.tyse.scrutiny.micro.scrutiny.config;

import com.tyse.scrutiny.micro.scrutiny.broker.E14DataConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka consumer configuration for E14 data.
 * Uses direct Spring Kafka instead of Spring Cloud Stream to avoid
 * ConcurrentModificationException issues with KafkaBinderMetrics.
 * Creates listener container programmatically for guaranteed startup.
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    @Value("${spring.cloud.stream.kafka.binder.brokers:localhost:9092}")
    private String bootstrapServers;

    private static final String E14_CONSUMER_GROUP = "scrutiny-e14-consumer";

    @Bean(name = "e14ConsumerFactory")
    public ConsumerFactory<String, String> e14ConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, E14_CONSUMER_GROUP);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "e14-consumer-client");

        LOG.info("Creating E14 Kafka ConsumerFactory with bootstrap servers: {}, group: {}",
            bootstrapServers, E14_CONSUMER_GROUP);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean(name = "e14KafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> e14KafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(e14ConsumerFactory());
        factory.setConcurrency(1);
        factory.setAutoStartup(true);

        LOG.info("Created E14 KafkaListenerContainerFactory for e14-data-cleaned topic");
        return factory;
    }

    /**
     * Creates and starts the message listener container programmatically.
     * This ensures the consumer is started regardless of @KafkaListener annotation processing.
     */
    @Bean
    public ConcurrentMessageListenerContainer<String, String> e14MessageListenerContainer(
            E14DataConsumer e14DataConsumer) {

        ContainerProperties containerProps = new ContainerProperties("e14-data-cleaned");
        containerProps.setGroupId(E14_CONSUMER_GROUP);
        containerProps.setClientId("e14-listener-container");
        containerProps.setMessageListener((MessageListener<String, String>) record -> {
            LOG.info("Programmatic listener received message from partition {} offset {}",
                record.partition(), record.offset());
            e14DataConsumer.consume(record.value());
        });

        ConcurrentMessageListenerContainer<String, String> container =
            new ConcurrentMessageListenerContainer<>(e14ConsumerFactory(), containerProps);
        container.setConcurrency(1);
        container.setAutoStartup(true);
        container.setBeanName("e14ListenerContainer");

        LOG.info("Created programmatic E14 MessageListenerContainer for e14-data-cleaned topic");
        return container;
    }
}
