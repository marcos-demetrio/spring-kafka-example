package com.example.kafka.infra.kafka.producer;

import io.cloudevents.CloudEvent;
import org.apache.kafka.clients.producer.ProducerRecord;

public interface ProducerPort<T> {
  String topic();

  ProducerRecord<String, CloudEvent> configureProducerRecord(T type);

  void send(T event);
}
