package com.example.kafka.domain.kafka.messaging.taxpayer;

import com.example.kafka.domain.kafka.KafkaTopicNames;
import com.example.kafka.domain.kafka.messaging.KafkaProducerCallback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaxPayerProducer {

  @Autowired
  @Qualifier("KafkaTemplateString")
  private KafkaTemplate<String, String> kafkaTemplate;

  public void send(String key, String value) {
    var producerRecord = new ProducerRecord<>(KafkaTopicNames.TAX_PAYER_TOPIC, key, value);

    kafkaTemplate.send(producerRecord).addCallback(new KafkaProducerCallback<>());
  }
}
