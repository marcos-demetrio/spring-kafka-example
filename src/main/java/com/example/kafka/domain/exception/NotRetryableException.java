package com.example.kafka.domain.exception;

public class NotRetryableException extends RuntimeException {
  public NotRetryableException(String message) {
    super(message);
  }
}
