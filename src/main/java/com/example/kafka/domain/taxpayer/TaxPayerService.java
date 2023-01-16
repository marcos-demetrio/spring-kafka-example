package com.example.kafka.domain.taxpayer;

import com.example.kafka.domain.exception.RetryableException;
import com.example.kafka.domain.kafka.messaging.taxpayer.TaxPayerProducer;
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
    var taxPayerId = UUID.randomUUID();

    taxPayer.setId(taxPayerId);

    producer.send(taxPayer.getId().toString(), taxPayer.getName());

    return taxPayer;
  }

  public TaxPayer update(TaxPayer taxPayer) {
    return taxPayer;
  }

  public void processTaxPayer(String event) {
    if (event.startsWith("retry")) {
      throw new RetryableException("An error occurred retrying with event ".concat(event));
    }

    if (event.startsWith("fail")) {
      throw new RetryableException("An error occurred with event ".concat(event));
    }

    log.info("Processing: name={}", event);
  }
}
