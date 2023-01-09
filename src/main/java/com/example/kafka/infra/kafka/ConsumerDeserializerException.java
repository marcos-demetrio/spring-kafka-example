package com.example.kafka.infra.kafka;

public class ConsumerDeserializerException extends RuntimeException {
  public ConsumerDeserializerException(String message) {
    super(message);
  }
}
