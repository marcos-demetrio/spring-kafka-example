package com.example.kafka.domain.kafka.messaging;

import com.example.kafka.domain.exception.RetryableException;
import com.example.kafka.domain.kafka.KafkaConfig;
import com.example.kafka.domain.kafka.KafkaTopicNames;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

@Slf4j
@Configuration
public class KafkaConsumerConfig {

  @Autowired private KafkaConfig kafkaConfig;

  @Value("${kafka.groupId}")
  private String groupId;

  @Value("${kafka.autoOffsetReset}")
  private String autoOffsetReset;

  @Bean(name = "KafkaListenerFactoryString")
  public ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaListenerString(
      @Qualifier("RecovererString") DeadLetterPublishingRecoverer recoverer) {

    var configs = new HashMap<>(kafkaConfig.getDefaultConfigs());

    configs.put(GROUP_ID_CONFIG, groupId);
    configs.put(AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
    configs.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configs.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configs.put(ENABLE_AUTO_COMMIT_CONFIG, true);

    var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
    factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(configs));
    factory.setCommonErrorHandler(deadLetterErrorHandlerString(recoverer));

    return factory;
  }

  @Bean(name = "KafkaListenerFactoryStringRetry")
  public ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaListenerStringRetry(
      @Qualifier("RecovererStringRetry") DeadLetterPublishingRecoverer recoverer) {

    var configs = new HashMap<>(kafkaConfig.getDefaultConfigs());

    configs.put(GROUP_ID_CONFIG, groupId);
    configs.put(AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
    configs.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configs.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configs.put(ENABLE_AUTO_COMMIT_CONFIG, true);

    var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
    factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(configs));
    factory.setCommonErrorHandler(deadLetterErrorHandlerStringRetry(recoverer));

    return factory;
  }

  @Bean(name = "RecovererString")
  public DeadLetterPublishingRecoverer deadLetterPublishingRecovererString(
      @Qualifier("KafkaTemplateString") KafkaTemplate<String, String> kafkaTemplate) {
    return new DeadLetterPublishingRecoverer(
        kafkaTemplate,
        (rec, ex) -> {
          log.warn("Sending message to retry topic. key={}, reason={}", rec.key(), ex.getMessage());

          return new TopicPartition(KafkaTopicNames.TAX_PAYER_RETRY_TOPIC, rec.partition());
        });
  }

  @Bean(name = "RecovererStringRetry")
  public DeadLetterPublishingRecoverer deadLetterPublishingRecovererStringRetry(
      @Qualifier("KafkaTemplateString") KafkaTemplate<String, String> kafkaTemplate) {
    return new DeadLetterPublishingRecoverer(
        kafkaTemplate,
        (rec, ex) -> {
          log.warn("Sending message to DLT. key={}, reason={}", rec.key(), ex.getMessage());

          return new TopicPartition(KafkaTopicNames.TAX_PAYER_DLT_TOPIC, 0);
        });
  }

  private DefaultErrorHandler deadLetterErrorHandlerString(ConsumerRecordRecoverer recoverer) {
    var exceptions = new HashMap<Class<? extends Throwable>, Boolean>();

    exceptions.put(TimeoutException.class, true);
    exceptions.put(IOException.class, true);

    var backOffWithMaxRetries = new ExponentialBackOffWithMaxRetries(5);
    backOffWithMaxRetries.setInitialInterval(3_000L);
    backOffWithMaxRetries.setMaxInterval(60_000L);
    backOffWithMaxRetries.setMultiplier(2.0);

    var defaultErrorHandler = new DefaultErrorHandler(recoverer, backOffWithMaxRetries);
    defaultErrorHandler.setClassifications(exceptions, false);

    return defaultErrorHandler;
  }

  private DefaultErrorHandler deadLetterErrorHandlerStringRetry(ConsumerRecordRecoverer recoverer) {
    var exceptions = new HashMap<Class<? extends Throwable>, Boolean>();

    exceptions.put(TimeoutException.class, true);
    exceptions.put(IOException.class, true);
    exceptions.put(RetryableException.class, true);

    var backOffWithMaxRetries = new ExponentialBackOffWithMaxRetries(5);
    backOffWithMaxRetries.setInitialInterval(1_000L);
    backOffWithMaxRetries.setMaxInterval(60_000L);
    backOffWithMaxRetries.setMultiplier(2.0);

    var defaultErrorHandler = new DefaultErrorHandler(recoverer, backOffWithMaxRetries);
    defaultErrorHandler.setClassifications(exceptions, false);

    return defaultErrorHandler;
  }
}
