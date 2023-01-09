package com.example.kafka.infra.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.kafka.clients.CommonClientConfigs.SECURITY_PROTOCOL_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
@NoArgsConstructor
public class KafkaProperties {
  private static final String SSL_PROTOCOL = "SSL";

  private List<String> bootstrapServers;
  private String trustStoreLocation;

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
