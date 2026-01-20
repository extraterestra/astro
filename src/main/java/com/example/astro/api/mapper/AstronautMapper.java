package com.example.astro.api.mapper;

import com.example.astro.api.dto.AstronautDto;
import com.example.astro.api.dto.MissionDto;
import com.example.astro.domain.Astronaut;
import com.example.astro.domain.Mission;
import com.example.astro.service.AstronautService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Component
public class AstronautMapper {

  private final AstronautService service;

  public AstronautMapper(AstronautService service) {
    this.service = service;
  }

  public AstronautDto toDto(Astronaut a) {
    AstronautDto dto = new AstronautDto();
    dto.id = a.getId();
    dto.firstName = a.getFirstName();
    dto.lastName = a.getLastName();
    dto.dateOfBirth = a.getDateOfBirth();
    dto.totalDaysOnISS = service.totalDaysOnISS(a);
    dto.totalHoursOnOrbit = service.totalHoursOnOrbit(a);
    dto.missions = a.getMissions().stream().map(this::toDto).collect(Collectors.toList());
    return dto;
  }

  public AstronautDto toDtoWithinRange(Astronaut a, LocalDate from, LocalDate to) {
    AstronautDto dto = toDto(a);
    dto.daysOnISSWithinRange = service.daysOnISSWithin(a, from, to);
    return dto;
  }

  public Astronaut toDomain(AstronautDto dto) {
    return new Astronaut(
      dto.id,
      dto.firstName,
      dto.lastName,
      dto.dateOfBirth,
      dto.missions == null
        ? java.util.List.of()
        : dto.missions.stream().map(this::toDomain).collect(Collectors.toList())
    );
  }

  private MissionDto toDto(Mission m) {
    MissionDto dto = new MissionDto();
    dto.missionName = m.getMissionName();
    dto.startDate = m.getStartDate();
    dto.endDate = m.getEndDate();
    return dto;
  }

  private Mission toDomain(MissionDto dto) {
    return new Mission(dto.missionName, dto.startDate, dto.endDate);
  }
}


