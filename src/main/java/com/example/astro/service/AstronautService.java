package com.example.astro.service;

import com.example.astro.domain.Astronaut;
import com.example.astro.domain.ports.AstronautQueryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AstronautService {

  private final AstronautQueryPort port;

  public AstronautService(AstronautQueryPort port) {
    this.port = port;
  }

  public Optional<Astronaut> getById(String id) {
    return port.findById(id);
  }

  public List<Astronaut> findByRange(LocalDate from, LocalDate to) {
    return port.findOverlapping(from, to);
  }

  public Astronaut create(Astronaut prototype) {
    String id = UUID.randomUUID().toString();
    Astronaut toSave = new Astronaut(
      id,
      prototype.getFirstName(),
      prototype.getLastName(),
      prototype.getDateOfBirth(),
      prototype.getMissions()
    );
    return port.save(toSave);
  }

  public Optional<Astronaut> update(String id, Astronaut prototype) {
    Astronaut updated = new Astronaut(
      id,
      prototype.getFirstName(),
      prototype.getLastName(),
      prototype.getDateOfBirth(),
      prototype.getMissions()
    );
    return port.update(id, updated);
  }

  public boolean delete(String id) {
    return port.deleteById(id);
  }

  public long totalDaysOnISS(Astronaut a) {
    return a.getMissions().stream()
      .mapToLong(m -> ChronoUnit.DAYS.between(m.getStartDate(), m.getEndDate().plusDays(1)))
      .sum();
  }

  public long totalHoursOnOrbit(Astronaut a) {
    return totalDaysOnISS(a) * 24L;
  }

  public long daysOnISSWithin(Astronaut a, LocalDate from, LocalDate to) {
    return a.getMissions().stream()
      .mapToLong(m -> overlapDays(m.getStartDate(), m.getEndDate(), from, to))
      .sum();
  }

  private long overlapDays(LocalDate s1, LocalDate e1, LocalDate s2, LocalDate e2) {
    LocalDate start = s1.isAfter(s2) ? s1 : s2;
    LocalDate end = e1.isBefore(e2) ? e1 : e2;
    if (end.isBefore(start)) return 0;
    return ChronoUnit.DAYS.between(start, end.plusDays(1));
  }
}


