package com.example.kafka.domain.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.CommonClientConfigs.SECURITY_PROTOCOL_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG;

@Configuration
public class KafkaConfig {
  private static final String SSL_PROTOCOL = "SSL";

  @Value("${kafka.bootstrapServers}")
  private String bootstrapServers;

  @Value("${kafka.trustStoreLocation}")
  private String trustStoreLocation;

  @Bean
  public KafkaAdmin kafkaAdmin() {
    return new KafkaAdmin(this.getDefaultConfigs());
  }

  public Map<String, Object> getDefaultConfigs() {
    var configs = new HashMap<String, Object>();

    if (StringUtils.hasText(trustStoreLocation)) {
      configs.put(SSL_TRUSTSTORE_LOCATION_CONFIG, trustStoreLocation);
      configs.put(SECURITY_PROTOCOL_CONFIG, SSL_PROTOCOL);
    }

    configs.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

    return configs;
  }
}
