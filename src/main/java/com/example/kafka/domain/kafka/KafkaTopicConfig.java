package com.example.kafka.domain.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.stream.Stream;

@Configuration
public class KafkaTopicConfig {
  @Autowired private KafkaAdmin kafkaAdmin;

  @Value("${kafka.topic.numPartitions}")
  private int numPartitions;

  @Value("${kafka.topic.replicationFactor}")
  private short replicationFactor;

  @Order(Ordered.HIGHEST_PRECEDENCE)
  @EventListener(ApplicationReadyEvent.class)
  public void createOrModifyTopics() {
    var topics =
        Stream.of(KafkaTopicNames.TAX_PAYER_TOPIC, KafkaTopicNames.TAX_PAYER_RETRY_TOPIC)
            .map(s -> new NewTopic(s, numPartitions, replicationFactor))
            .toArray(NewTopic[]::new);

    kafkaAdmin.createOrModifyTopics(topics);
  }

  @Order(Ordered.HIGHEST_PRECEDENCE + 1)
  @EventListener(ApplicationReadyEvent.class)
  public void createOrModifyDltTopics() {
    var topics =
        Stream.of(KafkaTopicNames.TAX_PAYER_DLT_TOPIC)
            .map(s -> new NewTopic(s, 1, replicationFactor))
            .toArray(NewTopic[]::new);

    kafkaAdmin.createOrModifyTopics(topics);
  }
}
