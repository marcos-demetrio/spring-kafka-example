package com.example.kafka.infra.kafka.producer.taxpayer;

import com.example.kafka.domain.taxpayer.TaxPayer;
import com.example.kafka.infra.kafka.Event;
import lombok.*;

import java.util.UUID;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaxPayerEvent implements Event {
  private UUID id;
  private String name;

  public static TaxPayerEvent fromTaxPayer(TaxPayer taxPayer) {
    return TaxPayerEvent.builder().id(taxPayer.getId()).name(taxPayer.getName()).build();
  }

  @Override
  public String getKey() {
    return this.id == null ? null : this.id.toString();
  }
}
