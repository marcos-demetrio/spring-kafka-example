package com.example.kafka.api.taxpayer;

import com.example.kafka.domain.taxpayer.TaxPayer;
import com.example.kafka.domain.taxpayer.TaxPayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/v1/tax-payer")
@RequiredArgsConstructor
public class TaxPayerController {

  private final TaxPayerService service;

  @PostMapping
  public ResponseEntity<List<TaxPayer>> post(@RequestBody @Valid TaxPayerRequest[] requests) {
    var taxPayerList =
        Stream.of(requests)
            .map(request -> TaxPayer.builder().name(request.getName()).build())
            .map(service::create)
            .collect(Collectors.toList());

    return ResponseEntity.ok(taxPayerList);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaxPayer> put(
      @PathVariable("id") UUID id, @RequestBody @Valid TaxPayerRequest request) {
    var taxPayer = service.update(TaxPayer.builder().id(id).name(request.getName()).build());

    return ResponseEntity.ok(taxPayer);
  }
}
