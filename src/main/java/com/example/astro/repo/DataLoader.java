package com.example.astro.repo;

import com.example.astro.domain.Astronaut;
import com.example.astro.domain.Mission;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Configuration
public class DataLoader {

  @Bean
  CommandLineRunner seed(InMemoryAstronautRepository repo) {
    return args -> {
      Astronaut a1 = new Astronaut(
        UUID.randomUUID().toString(),
        "Sergey",
        "Prokopyev",
        LocalDate.of(1975, 2, 19),
        List.of(
          new Mission("Expedition 56/57", LocalDate.of(2018, 6, 6), LocalDate.of(2018, 12, 20)),
          new Mission("Expedition 67/68", LocalDate.of(2022, 9, 21), LocalDate.of(2023, 3, 28))
        )
      );

      Astronaut a2 = new Astronaut(
        UUID.randomUUID().toString(),
        "Jasmin",
        "Moghbeli",
        LocalDate.of(1983, 6, 24),
        List.of(
          new Mission("Expedition 69/70", LocalDate.of(2023, 8, 26), LocalDate.of(2024, 3, 12))
        )
      );

      Astronaut a3 = new Astronaut(
        UUID.randomUUID().toString(),
        "Andreas",
        "Mogensen",
        LocalDate.of(1976, 11, 2),
        List.of(
          new Mission("Expedition 70", LocalDate.of(2023, 8, 26), LocalDate.of(2024, 4, 6))
        )
      );

      repo.putAll(List.of(a1, a2, a3));
    };
  }
}


