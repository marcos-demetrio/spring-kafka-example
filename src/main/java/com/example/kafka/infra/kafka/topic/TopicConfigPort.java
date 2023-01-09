package com.example.kafka.infra.kafka.topic;

import org.apache.kafka.clients.admin.NewTopic;

public interface TopicConfigPort {
  NewTopic configure();
}
