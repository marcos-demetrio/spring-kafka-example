package com.example.kafka.domain.exception;

public class RetryableException extends RuntimeException {
  public RetryableException(String message) {
    super(message);
  }
}
