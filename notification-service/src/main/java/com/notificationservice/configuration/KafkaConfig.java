package com.notificationservice.configuration;

import com.notificationservice.dto.OrderKafkaDTO;
import com.notificationservice.exception.NonRetryableException;
import com.notificationservice.exception.RetryableException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

/**
 * Настройка кафки
 */
@Configuration
public class KafkaConfig {

    /**
     * Адреса Kafka брокеров, задаются из application.properties
     */
    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * ID группы потребителей Kafka, задаётся из конфигурации
     */
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    /**
     * Создаёт фабрику потребителей Kafka с нужными настройками.
     * Включает десериализацию ключей и значений сообщений,
     * а также обработку ошибок при десериализации.
     *
     * @return ConsumerFactory для создания потребителей Kafka
     */
    @Bean
    ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, OrderKafkaDTO.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "com.notificationservice.dto.ProductKafkaDTO");
        config.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        return new DefaultKafkaConsumerFactory<>(config);
    }

    /**
     * Создаёт фабрику контейнеров слушателей Kafka,
     * настраивает обработчик ошибок с повторными попытками и Dead Letter Queue (DLQ).
     *
     * @param consumerFactory фабрика потребителей Kafka
     * @param kafkaTemplate  шаблон для отправки сообщений в Kafka (для DLQ)
     * @return фабрика контейнеров слушателей Kafka
     */
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory, KafkaTemplate kafkaTemplate) {
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(kafkaTemplate),
                new FixedBackOff(3000, 3)
        );
        errorHandler.addNotRetryableExceptions(NonRetryableException.class);
        errorHandler.addRetryableExceptions(RetryableException.class);
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    /**
     * Создаёт KafkaTemplate для отправки сообщений в Kafka
     *
     * @param producerFactory фабрика продюсеров Kafka
     * @return KafkaTemplate для отправки сообщений
     */
    @Bean
    KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    /**
     * Создаёт фабрику продюсеров Kafka с настройками сериализации ключей и значений сообщений.
     *
     * @return ProducerFactory для создания продюсеров Kafka
     */
    @Bean
    ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

}

