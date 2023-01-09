package com.example.kafka.infra.kafka.consumer;

import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

public interface ConsumerConfigPort<T> {
  ConcurrentKafkaListenerContainerFactory<String, T> configure();
}
