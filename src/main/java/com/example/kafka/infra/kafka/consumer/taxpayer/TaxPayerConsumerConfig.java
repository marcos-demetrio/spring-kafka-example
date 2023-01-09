package com.example.kafka.infra.kafka.consumer.taxpayer;

import com.example.kafka.infra.kafka.KafkaProperties;
import com.example.kafka.infra.kafka.consumer.ConsumerConfigPort;
import com.example.kafka.infra.kafka.topic.TaxPayerTopicConfig;
import io.cloudevents.CloudEvent;
import io.cloudevents.kafka.CloudEventDeserializer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

@Configuration
@RequiredArgsConstructor
public class TaxPayerConsumerConfig implements ConsumerConfigPort<CloudEvent> {
  private final KafkaProperties kafkaProperties;
  private final TaxPayerTopicConfig topicConfig;

  @Bean(name = "TaxPayerConsumerConfig")
  @Override
  public ConcurrentKafkaListenerContainerFactory<String, CloudEvent> configure() {
    var configs = new HashMap<>(kafkaProperties.getDefaultConfigs());

    configs.put(GROUP_ID_CONFIG, topicConfig.getGroupId());
    configs.put(AUTO_OFFSET_RESET_CONFIG, topicConfig.getAutoOffsetReset());
    configs.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configs.put(VALUE_DESERIALIZER_CLASS_CONFIG, CloudEventDeserializer.class);
    configs.put(ENABLE_AUTO_COMMIT_CONFIG, true);

    var factory = new ConcurrentKafkaListenerContainerFactory<String, CloudEvent>();
    factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(configs));

    return factory;
  }
}
