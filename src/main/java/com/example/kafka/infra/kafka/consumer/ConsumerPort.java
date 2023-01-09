package com.example.kafka.infra.kafka.consumer;

public interface ConsumerPort<E, T> {
  void onMessage(E event);

  T deserialize(E event);
}
