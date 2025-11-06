package com.example.astro.domain;

import java.time.LocalDate;
import java.util.List;

public class Astronaut {
  private final String id;
  private final String firstName;
  private final String lastName;
  private final LocalDate dateOfBirth;
  private final List<Mission> missions;

  public Astronaut(String id, String firstName, String lastName, LocalDate dateOfBirth, List<Mission> missions) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.missions = missions;
  }

  public String getId() { return id; }
  public String getFirstName() { return firstName; }
  public String getLastName() { return lastName; }
  public LocalDate getDateOfBirth() { return dateOfBirth; }
  public List<Mission> getMissions() { return missions; }
}


