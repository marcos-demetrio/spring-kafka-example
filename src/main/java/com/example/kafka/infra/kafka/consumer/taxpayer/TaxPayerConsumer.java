package com.example.kafka.infra.kafka.consumer.taxpayer;

import com.example.kafka.domain.taxpayer.TaxPayerService;
import com.example.kafka.infra.ObjectMapperHolder;
import com.example.kafka.infra.kafka.ConsumerDeserializerException;
import com.example.kafka.infra.kafka.JsonMappingException;
import com.example.kafka.infra.kafka.consumer.ConsumerPort;
import com.example.kafka.infra.kafka.producer.taxpayer.TaxPayerEvent;
import io.cloudevents.CloudEvent;
import io.cloudevents.CloudEventData;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaxPayerConsumer implements ConsumerPort<CloudEvent, TaxPayerEvent> {
  private final TaxPayerService service;

  @KafkaListener(
      topics = "${kafka.topic.taxpayer.name}",
      containerFactory = "TaxPayerConsumerConfig")
  @Override
  public void onMessage(@Payload CloudEvent cloudEvent) {
    var event = this.deserialize(cloudEvent);

    service.processTaxPayerEvent(event);
  }

  @Override
  public TaxPayerEvent deserialize(CloudEvent event) {
    byte[] data =
        Optional.ofNullable(event.getData())
            .map(CloudEventData::toBytes)
            .orElseThrow(() -> new ConsumerDeserializerException("Cannot deserialize Cloud Event"));

    try {
      return ObjectMapperHolder.getInstance().readValue(data, TaxPayerEvent.class);
    } catch (IOException e) {
      throw new JsonMappingException(e.getMessage());
    }
  }
}
