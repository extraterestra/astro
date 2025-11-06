package com.example.astro.api;

import com.example.astro.api.dto.AstronautDto;
import com.example.astro.api.mapper.AstronautMapper;
import com.example.astro.service.AstronautService;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/astronauts")
@Validated
public class AstronautController {

  private final AstronautService service;
  private final AstronautMapper mapper;

  public AstronautController(AstronautService service, AstronautMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @GetMapping
  public ResponseEntity<List<AstronautDto>> getByRange(
      @RequestParam("dateFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
      @RequestParam(value = "dateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo
  ) {
    LocalDate to = (dateTo == null) ? LocalDate.now() : dateTo;
    if (to.isBefore(dateFrom)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    List<AstronautDto> result = service.findByRange(dateFrom, to).stream()
      .map(a -> mapper.toDtoWithinRange(a, dateFrom, to))
      .collect(Collectors.toList());
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AstronautDto> getById(
      @PathVariable("id")
      @Pattern(regexp = "^[A-Za-z0-9\\-]+$", message = "Invalid id") String id
  ) {
    return service.getById(id)
      .map(mapper::toDto)
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }
}


