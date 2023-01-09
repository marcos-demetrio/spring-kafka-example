package com.example.kafka.infra.kafka.topic;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static org.apache.kafka.common.config.TopicConfig.RETENTION_MS_CONFIG;

@Configuration
@ConfigurationProperties(prefix = "kafka.topic.taxpayer")
@Data
@NoArgsConstructor
public class TaxPayerTopicConfig implements TopicConfigPort {
  private String name;
  private String retention;
  private String groupId;
  private String autoOffsetReset;
  private String acksConfig;
  private String retriesConfig;
  private int partitionCount;
  private short replicaCount;

  @Bean(name = "TaxPayerTopicConfig")
  @Override
  public NewTopic configure() {
    return TopicBuilder.name(name)
        .config(RETENTION_MS_CONFIG, retention)
        .partitions(partitionCount)
        .replicas(replicaCount)
        .compact()
        .build();
  }
}
