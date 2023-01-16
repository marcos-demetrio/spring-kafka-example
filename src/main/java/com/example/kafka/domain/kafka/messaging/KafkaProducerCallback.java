package com.example.kafka.domain.kafka.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
public class KafkaProducerCallback<K, V> implements ListenableFutureCallback<SendResult<K, V>> {

  @Override
  public void onFailure(Throwable ex) {
    log.warn("Unable to deliver message. {}", ex.getMessage());
  }

  @Override
  public void onSuccess(SendResult<K, V> result) {
    if (result != null) {
      log.debug("Message delivered at {}}", result.getRecordMetadata());
    }
  }
}
