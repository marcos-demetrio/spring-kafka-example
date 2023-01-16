package com.example.kafka.domain.kafka.messaging.taxpayer;

import com.example.kafka.domain.kafka.KafkaTopicNames;
import com.example.kafka.domain.taxpayer.TaxPayerService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaxPayerRetryConsumer {

  private final TaxPayerService service;

  @KafkaListener(
      topics = KafkaTopicNames.TAX_PAYER_RETRY_TOPIC,
      concurrency = "${kafka.topic.numPartitions}",
      containerFactory = "KafkaListenerFactoryStringRetry")
  public void onMessage(@Payload ConsumerRecord<String, String> consumerRecord) {
    service.processTaxPayer("retry-".concat(consumerRecord.value()));
  }
}
