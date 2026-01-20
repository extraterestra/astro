package com.example.astro.repo;

import com.example.astro.domain.Astronaut;
import com.example.astro.domain.Mission;
import com.example.astro.domain.ports.AstronautQueryPort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryAstronautRepository implements AstronautQueryPort {

  private final Map<String, Astronaut> store = new ConcurrentHashMap<>();

  @Override
  public Optional<Astronaut> findById(String id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public List<Astronaut> findOverlapping(LocalDate from, LocalDate to) {
    return store.values().stream()
      .filter(a -> a.getMissions().stream().anyMatch(m -> overlaps(m, from, to)))
      .collect(Collectors.toList());
  }

  @Override
  public List<Astronaut> findAll() {
    return new ArrayList<>(store.values());
  }

  @Override
  public Astronaut save(Astronaut astronaut) {
    store.put(astronaut.getId(), astronaut);
    return astronaut;
  }

  @Override
  public Optional<Astronaut> update(String id, Astronaut astronaut) {
    if (!store.containsKey(id)) {
      return Optional.empty();
    }
    store.put(id, astronaut);
    return Optional.of(astronaut);
  }

  @Override
  public boolean deleteById(String id) {
    return store.remove(id) != null;
  }

  void putAll(Collection<Astronaut> astronauts) {
    astronauts.forEach(a -> store.put(a.getId(), a));
  }

  private boolean overlaps(Mission m, LocalDate from, LocalDate to) {
    LocalDate start = m.getStartDate();
    LocalDate end = m.getEndDate();
    return (start.isBefore(to.plusDays(1)) && end.isAfter(from.minusDays(1)));
  }
}


