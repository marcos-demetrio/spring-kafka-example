package com.example.kafka.domain.kafka.messaging;

import com.example.kafka.domain.kafka.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Configuration
public class KafkaProducerConfig {

  @Autowired private KafkaConfig kafkaConfig;

  @Value("${kafka.retriesConfig}")
  private int retriesConfig;

  @Value("${kafka.acksConfig}")
  private String acksConfig;

  @Bean(name = "KafkaTemplateString")
  public KafkaTemplate<String, String> kafkaTemplateString() {
    return new KafkaTemplate<>(producerFactoryString());
  }

  private ProducerFactory<String, String> producerFactoryString() {
    var properties = new HashMap<>(kafkaConfig.getDefaultConfigs());

    properties.put(RETRIES_CONFIG, retriesConfig);
    properties.put(ACKS_CONFIG, acksConfig);
    properties.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

    return new DefaultKafkaProducerFactory<>(properties);
  }

  @Slf4j
  public static class ProducerCallback<K, V> implements ListenableFutureCallback<SendResult<K, V>> {

    @Override
    public void onFailure(Throwable ex) {
      log.warn("Unable to deliver message. {}", ex.getMessage());
    }

    @Override
    public void onSuccess(SendResult<K, V> result) {
      if (result != null) {
        log.info("Message delivered at {}}", result.getRecordMetadata());
      }
    }
  }
}
