# ISS Astronauts API

A minimal Spring Boot (Maven) service exposing endpoints to query astronauts who have stayed on the International Space Station (ISS). It calculates:

- totalDaysOnISS: cumulative days across all ISS missions
- totalHoursOnOrbit: totalDaysOnISS x 24
- daysOnISSWithinRange: days spent on ISS within a requested date range (only on the range endpoint)

Seed data is loaded in-memory at startup. The core uses a clean port (`AstronautQueryPort`) for easy future adapters (DB, NASA API, MCP server/client).

## Endpoints

- GET `/astronauts?dateFrom=YYYY-MM-DD&dateTo=YYYY-MM-DD`
  - Returns astronauts with any ISS mission overlapping the range.
  - If `dateTo` is omitted, it defaults to today.
  - Each item includes `daysOnISSWithinRange` for the requested window.

- GET `/astronauts/{id}`
  - Returns a single astronaut by id (404 if not found).

Example response fields (both endpoints):
- `id`, `firstName`, `lastName`, `dateOfBirth`
- `totalDaysOnISS`, `totalHoursOnOrbit`
- `missions[]` with `missionName`, `startDate`, `endDate`
- `daysOnISSWithinRange` (only on the range endpoint)

## Project structure

```
astro/
  pom.xml
  src/main/java/com/example/astro/
    AstroApplication.java
    api/
      AstronautController.java
      dto/
        AstronautDto.java
        MissionDto.java
      mapper/
        AstronautMapper.java
    config/
      JacksonConfig.java
    domain/
      Astronaut.java
      Mission.java
      ports/
        AstronautQueryPort.java
    repo/
      InMemoryAstronautRepository.java
      DataLoader.java
    service/
      AstronautService.java
```

## Prerequisites

- Java 21
- Maven 3.9+
- macOS/Linux/Windows

Check versions:

```bash
java -version
mvn -v
```

On macOS (Homebrew):

```bash
brew install openjdk@21 maven
# Optional: set JAVA_HOME for this session
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

## Build & Run

From the project root (`astro/`):

```bash
mvn -DskipTests package
mvn spring-boot:run
```

Service will start at `http://localhost:8080`.

## Try it

- List by date range:

```bash
curl "http://localhost:8080/astronauts?dateFrom=2023-01-01&dateTo=2024-03-31" | jq
```

- Get by id (copy `id` from previous response):

```bash
curl "http://localhost:8080/astronauts/<ID>" | jq
```

Notes:
- `dateTo` is optional; if omitted it defaults to today.
- The range endpoint returns `daysOnISSWithinRange` for that window.

## Swagger / API documentation

This service exposes interactive Swagger / OpenAPI documentation via **springdoc-openapi**.

- **Local Swagger UI**
  - URL: `http://localhost:8080/swagger-ui.html`
  - Shows all endpoints (`GET`, `POST`, `PUT`, `DELETE`) under the **Astronauts** tag.
  - You can use the **Try it out** button to execute requests directly from the browser.

- **Deployed (Railway) Swagger UI**
  - If deployed to Railway with a public domain (for example `https://astro-production-4c19.up.railway.app`), Swagger UI is available at:
    - `https://astro-production-4c19.up.railway.app/swagger-ui.html`

### Documented endpoints

- **GET `/astronauts?dateFrom=YYYY-MM-DD&dateTo=YYYY-MM-DD`**
  - 200: List of astronauts overlapping the date range.
  - 400: Invalid date range (e.g., `dateTo` before `dateFrom`).

- **GET `/astronauts/{id}`**
  - 200: Astronaut found.
  - 404: Astronaut not found.

- **POST `/astronauts`**
  - Generates a new `id` and creates an astronaut.
  - 201: Astronaut created (with `Location` header pointing to `/astronauts/{id}`).
  - 400: Invalid request body.
  - Example request body (also shown in Swagger examples):

    ```json
    {
      "firstName": "John",
      "lastName": "Doe",
      "dateOfBirth": "1980-01-01",
      "missions": [
        {
          "missionName": "Expedition 99",
          "startDate": "2025-01-01",
          "endDate": "2025-06-01"
        }
      ]
    }
    ```

- **PUT `/astronauts/{id}`**
  - Replaces astronaut data for the given `id` (path `id` wins over any `id` in the body).
  - 200: Astronaut updated.
  - 404: Astronaut not found.

- **DELETE `/astronauts/{id}`**
  - 204: Astronaut deleted.
  - 404: Astronaut not found.

## Docker (optional)

If you prefer containerized runs, add a Dockerfile like:

```Dockerfile
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/astro-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

Build and run:

```bash
mvn -DskipTests package
docker build -t astro:local .
docker run --rm -p 8080:8080 astro:local
```

## Extensibility (MCP-ready)

- Domain depends on `AstronautQueryPort`; the current `InMemoryAstronautRepository` is an adapter.
- To integrate MCP:
  - Implement an MCP Server tool (e.g., `astronaut.search`) delegating to `AstronautService` via the port.
  - Or replace the in-memory repo with adapters for a DB or external API without changing controllers/services.

## License

N/A


