package com.example.kafka.api.taxpayer;

import com.example.kafka.domain.taxpayer.TaxPayer;
import com.example.kafka.domain.taxpayer.TaxPayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/tax-payer")
@RequiredArgsConstructor
public class TaxPayerController {
  private final TaxPayerService service;

  @PostMapping
  public ResponseEntity<TaxPayer> post(@RequestBody @Valid TaxPayerRequest request) {
    var taxPayer = service.create(TaxPayer.builder().name(request.getName()).build());

    return ResponseEntity.ok(taxPayer);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaxPayer> put(
      @PathVariable("id") UUID id, @RequestBody @Valid TaxPayerRequest request) {
    var taxPayer = service.update(TaxPayer.builder().id(id).name(request.getName()).build());

    return ResponseEntity.ok(taxPayer);
  }
}
