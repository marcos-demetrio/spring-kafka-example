package com.example.kafka.domain.kafka;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaTopicNames {
  public static final String TAX_PAYER_TOPIC = "com.example.taxpayer.0";
  public static final String TAX_PAYER_RETRY_TOPIC = "com.example.taxpayer.retry.0";
  public static final String TAX_PAYER_DLT_TOPIC = "com.example.taxpayer.dlt.0";
}
