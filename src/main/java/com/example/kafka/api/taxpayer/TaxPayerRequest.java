package com.example.kafka.api.taxpayer;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TaxPayerRequest {
  @NotNull(message = "Name is required")
  private String name;
}
