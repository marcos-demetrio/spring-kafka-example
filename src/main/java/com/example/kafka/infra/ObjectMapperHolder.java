package com.example.kafka.infra;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperHolder {
  private static final ObjectMapper INSTANCE =
      new ObjectMapper()
          .findAndRegisterModules()
          .setVisibility(PropertyAccessor.ALL, Visibility.NONE)
          .setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

  private ObjectMapperHolder() {
    throw new IllegalStateException("Operation not allowed");
  }

  public static ObjectMapper getInstance() {
    return INSTANCE;
  }
}
