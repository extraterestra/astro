package com.example.astro.domain;

import java.time.LocalDate;

public class Mission {
  private final String missionName;
  private final LocalDate startDate;
  private final LocalDate endDate;

  public Mission(String missionName, LocalDate startDate, LocalDate endDate) {
    this.missionName = missionName;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public String getMissionName() { return missionName; }
  public LocalDate getStartDate() { return startDate; }
  public LocalDate getEndDate() { return endDate; }
}


