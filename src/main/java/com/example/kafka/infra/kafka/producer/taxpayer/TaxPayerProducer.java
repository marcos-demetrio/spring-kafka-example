package com.example.kafka.infra.kafka.producer.taxpayer;

import com.example.kafka.infra.ObjectMapperHolder;
import com.example.kafka.infra.kafka.JsonMappingException;
import com.example.kafka.infra.kafka.producer.ProducerPort;
import com.example.kafka.infra.kafka.topic.TaxPayerTopicConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Component
public class TaxPayerProducer implements ProducerPort<TaxPayerEvent> {

  @Qualifier("TaxPayerProducerTemplate")
  @Autowired
  private KafkaTemplate<String, CloudEvent> producer;

  @Autowired private TaxPayerTopicConfig topicConfig;

  @Override
  public String topic() {
    return topicConfig.getName();
  }

  @Override
  public ProducerRecord<String, CloudEvent> configureProducerRecord(TaxPayerEvent event) {
    try {
      var cloudEvent =
          buildCloudEvent(event, ObjectMapperHolder.getInstance().writeValueAsBytes(event));
      return new ProducerRecord<>(this.topic(), event.getKey(), cloudEvent);
    } catch (JsonProcessingException e) {
      throw new JsonMappingException(e.getMessage());
    }
  }

  @Override
  public void send(TaxPayerEvent event) {
    var producerRecord = this.configureProducerRecord(event);
    var recordSent = producer.send(producerRecord);

    recordSent.addCallback(
        new ListenableFutureCallback<>() {
          @Override
          public void onFailure(Throwable ex) {
            log.warn("Unable to deliver message. {}", ex.getMessage());
          }

          @Override
          public void onSuccess(SendResult<String, CloudEvent> result) {
            log.info("Message delivered at {}}", result.getRecordMetadata());
          }
        });
  }

  private CloudEvent buildCloudEvent(TaxPayerEvent event, byte[] bytes) {
    return CloudEventBuilder.v1()
        .withId(UUID.randomUUID().toString())
        .withTime(OffsetDateTime.now())
        .withSource(URI.create("127.0.0.1"))
        .withType(event.getClass().getSimpleName())
        .withSubject(event.getId().toString())
        .withDataContentType("application/json")
        .withData(bytes)
        .build();
  }
}
