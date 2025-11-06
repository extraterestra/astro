package com.example.astro.api.dto;

import java.time.LocalDate;
import java.util.List;

public class AstronautDto {
  public String id;
  public String firstName;
  public String lastName;
  public LocalDate dateOfBirth;
  public long totalDaysOnISS;
  public long totalHoursOnOrbit;
  public Long daysOnISSWithinRange;
  public List<MissionDto> missions;
}


