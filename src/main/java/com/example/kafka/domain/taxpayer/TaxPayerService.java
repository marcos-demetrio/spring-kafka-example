package com.example.kafka.domain.taxpayer;

import com.example.kafka.infra.kafka.producer.taxpayer.TaxPayerEvent;
import com.example.kafka.infra.kafka.producer.taxpayer.TaxPayerProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaxPayerService {
  private final TaxPayerProducer producer;

  public TaxPayer create(TaxPayer taxPayer) {
    taxPayer.setId(UUID.randomUUID());

    var event = TaxPayerEvent.fromTaxPayer(taxPayer);

    producer.send(event);

    return taxPayer;
  }

  public TaxPayer update(TaxPayer taxPayer) {
    var event = TaxPayerEvent.fromTaxPayer(taxPayer);

    producer.send(event);

    return taxPayer;
  }

  public void processTaxPayerEvent(TaxPayerEvent event) {
    log.info("TaxPayer: id={}, name={}", event.getId(), event.getName());
  }
}
