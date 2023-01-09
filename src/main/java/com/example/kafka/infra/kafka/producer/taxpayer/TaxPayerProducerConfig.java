package com.example.kafka.infra.kafka.producer.taxpayer;

import com.example.kafka.infra.kafka.KafkaProperties;
import com.example.kafka.infra.kafka.producer.ProducerConfigPort;
import com.example.kafka.infra.kafka.topic.TaxPayerTopicConfig;
import io.cloudevents.CloudEvent;
import io.cloudevents.kafka.CloudEventSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@RequiredArgsConstructor
@Configuration
public class TaxPayerProducerConfig implements ProducerConfigPort<CloudEvent> {
  private final KafkaProperties kafkaProperties;
  private final TaxPayerTopicConfig topicConfig;

  private ProducerFactory<String, CloudEvent> producerFactory() {
    var properties = new HashMap<>(kafkaProperties.getDefaultConfigs());

    properties.put(RETRIES_CONFIG, topicConfig.getRetriesConfig());
    properties.put(ACKS_CONFIG, topicConfig.getAcksConfig());
    properties.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.put(VALUE_SERIALIZER_CLASS_CONFIG, CloudEventSerializer.class);

    return new DefaultKafkaProducerFactory<>(properties);
  }

  @Bean(name = "TaxPayerProducerTemplate")
  @Override
  public KafkaTemplate<String, CloudEvent> configure() {
    return new KafkaTemplate<>(producerFactory());
  }
}
