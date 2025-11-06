package com.example.astro.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

@Configuration
public class JacksonConfig {
  private final ObjectMapper objectMapper;
  public JacksonConfig(ObjectMapper objectMapper) { this.objectMapper = objectMapper; }

  @PostConstruct
  void registerJavaTime() {
    objectMapper.registerModule(new JavaTimeModule());
  }
}


