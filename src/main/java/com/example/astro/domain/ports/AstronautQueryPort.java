package com.example.astro.domain.ports;

import com.example.astro.domain.Astronaut;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AstronautQueryPort {
  Optional<Astronaut> findById(String id);
  List<Astronaut> findOverlapping(LocalDate from, LocalDate to);
  List<Astronaut> findAll();
}


