package com.example.kafka.domain.taxpayer;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TaxPayer {
  private UUID id;
  private String name;
}
