# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## RULES

Responder en español

## Project Overview

**tyseScrutinyMicroScrutiny** es un microservicio JHipster 8.11.0 (Java 17) que gestiona el escrutinio electoral. Servicio principal de negocio del ecosistema Tyse Scrutiny:

- Persiste datos E14 recibidos desde Kafka (topic `e14-data-cleaned`)
- Gestiona preconteo electoral (importación CSV)
- Detecta anomalías comparando escrutinio vs preconteo (3 tipos: PRECOUNT_DIFFERENCE, VOTES_EXCEED_VOTERS, SUM_MISMATCH)
- Publica anomalías a Kafka (`e14-anomaly-detected`) y estado de procesamiento (`e14-processing-status`)

**Stack:** Spring Boot 3.4.5, Spring WebFlux (reactive), PostgreSQL con R2DBC, Consul, Kafka, JWT, Liquibase.

Puerto: **8084** | PostgreSQL: **5434**

## Development Commands

```bash
# Levantar infraestructura compartida (Consul, Kafka, MinIO, MailHog)
cd ../tyse-scrutiny-infrastructure && docker compose up -d

# Levantar PostgreSQL de este microservicio
docker compose -f src/main/docker/postgresql.yml up --wait

# Iniciar en modo desarrollo (requiere Consul en localhost:8500)
./mvnw

# Debug remoto en puerto 8000
npm run backend:debug

# Regenerar código OpenAPI después de modificar swagger/api.yml
./mvnw generate-sources
```

### Testing

```bash
# Todos los tests (requiere Docker para Testcontainers)
./mvnw verify

# Tests unitarios sin logs
npm run backend:unit:test

# Tests de integración
./mvnw failsafe:integration-test

# Un test específico
./mvnw test -Dtest=NombreDelTest

# Una clase de integration test específica
./mvnw verify -Dit.test=NombreDelTestIT

# Checkstyle (nohttp)
npm run backend:nohttp:test

# Prettier check
npm run prettier:check

# Prettier format
npm run prettier:format
```

### Building

```bash
# Build de producción
./mvnw -Pprod clean verify

# Build Docker image
npm run java:docker
```

## Architecture

### Flujo principal de datos

```
PDF → [OCR Pipeline] → Kafka(e14-data-cleaned)
    → E14DataConsumer → E14PersistenceService → DB
    → AnomalyDetectionService → compara con Precount
    → Anomaly → Kafka(e14-anomaly-detected)
```

El consumer Kafka usa `MessageListenerContainer` programático (no `@KafkaListener`) para evitar `ConcurrentModificationException` con `KafkaBinderMetrics`. Ver `KafkaConsumerConfig.java`.

### Stack reactivo

Todo el stack es reactivo — controladores retornan `Mono<T>` / `Flux<T>`, repositorios son R2DBC (no JPA), Kafka con listeners programáticos. **No usar llamadas bloqueantes** (BlockHound activo en tests).

### API-First Development

La API se define en `src/main/resources/swagger/api.yml`. Los delegados se generan en `web/api/` con `openapi-generator`. Implementar delegados con clases `@Service`. Después de modificar el spec: `./mvnw generate-sources`.

### Microservicio en contexto

```
tyse-scrutiny-infrastructure/: Consul(:8500), Kafka(:9092), MinIO(:9000), MailHog(:1025)
Gateway:                       :8080, PostgreSQL :5432
Microservicio Divipol:         :8081, PostgreSQL :5433
Microservicio Scrutiny (este): :8084, PostgreSQL :5434
```

### Kafka

- **Consume:** `e14-data-cleaned` (consumer group: `scrutiny-e14-consumer`)
- **Produce:** `e14-anomaly-detected`, `e14-processing-status`

### Profiles

- `dev` (default), `prod`, `test`/`testdev`/`testprod`, `api-docs` (Swagger UI), `no-liquibase`, `local-dev`, `docker-dev`

### Testing infrastructure

- `@IntegrationTest` = meta-anotación que configura `@SpringBootTest` + `@EmbeddedSQL` + `@EmbeddedKafka`
- Testcontainers para PostgreSQL y Kafka
- ArchUnit (`TechnicalStructureTest`) para validar arquitectura de paquetes
- BlockHound para detectar llamadas bloqueantes en reactive

## Branch Governance

- **`main`** y **`develop`** están protegidas — NO push directo
- Todo cambio va por PR, requiere 1 aprobación de @manuelmottaherrera
- Feature branches: `feature/<nombre>-<descripcion>` (ej: `feature/manuel-kafka-config`)
- Convención de commits: `type(scope): descripción` (feat, fix, docs, style, refactor, test, chore)
- NO modificar: `CODEOWNERS`, `CONTRIBUTING.md`, `CLAUDE.md`, `.github/workflows/`
