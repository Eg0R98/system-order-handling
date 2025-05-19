package com.orderservice.configuration;


import com.orderservice.dto.OrderDTOFoKafka;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Настройка кафки
 */
@Configuration
public class KafkaConfig {

    /**
     * Адреса Kafka брокеров для продюсера,
     * задаются из application.properties
     */
    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Класс сериализации ключа сообщений Kafka,
     * задаётся из конфигурации
     */
    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    /**
     * Класс сериализации значения сообщений Kafka,
     * задаётся из конфигурации
     */
    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    /**
     * Параметр подтверждения доставки сообщений Kafka,
     * задаётся из конфигурации (например, "all", "1", "0")
     */
    @Value("${spring.kafka.producer.acks}")
    private String acks;

    /**
     * Таймаут доставки сообщений в миллисекундах,
     * задаётся из конфигурации
     */
    @Value("${spring.kafka.producer.properties.delivery.timeout.ms}")
    private String deliveryTimeout;

    /**
     * Время задержки перед отправкой пакета сообщений в миллисекундах,
     * задаётся из конфигурации
     */
    @Value("${spring.kafka.producer.properties.delivery.linger.ms}")
    private String linger;

    /**
     * Таймаут запроса в миллисекундах,
     * задаётся из конфигурации
     */
    @Value("${spring.kafka.producer.properties.request.timeout.ms}")
    private String requestTimeout;

    /**
     * Максимальное количество запросов в полёте на одно соединение,
     * задаётся из конфигурации
     */
    @Value("${spring.kafka.producer.properties.max.in.flight.requests.per.connection}")
    private String maxInFlightRequests;

    /**
     * Конфигурация фабрики продюсеров Kafka с настройками из application.properties.
     * Включает сериализацию ключей и значений, а также параметры производительности и подтверждений.
     *
     * @return фабрика продюсеров Kafka для сообщений с ключом String и значением OrderDTOFoKafka
     */
    @Bean
    ProducerFactory<String, OrderDTOFoKafka> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        config.put(ProducerConfig.ACKS_CONFIG, acks);
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeout);
        config.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeout);
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlightRequests);

        return new DefaultKafkaProducerFactory<>(config);
    }

    /**
     * Создаёт KafkaTemplate — высокоуровневый компонент для отправки сообщений Kafka,
     * использующий настроенную фабрику продюсеров.
     *
     * @return KafkaTemplate для отправки сообщений с ключом String и значением OrderDTOFoKafka
     */
    @Bean
    KafkaTemplate<String, OrderDTOFoKafka> kafkaTemplate() {
        return new KafkaTemplate<String, OrderDTOFoKafka>(producerFactory());
    }
}
