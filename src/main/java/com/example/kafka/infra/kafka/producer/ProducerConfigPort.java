package com.example.kafka.infra.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;

public interface ProducerConfigPort<T> {
  KafkaTemplate<String, T> configure();
}
