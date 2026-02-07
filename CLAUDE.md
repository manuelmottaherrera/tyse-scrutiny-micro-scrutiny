# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## RULES

Responder en español

## Project Overview

**tyseScrutinyMicroScrutiny** es un microservicio JHipster 8.11.0 que gestiona el escrutinio electoral. Es el servicio principal de negocio del ecosistema Tyse Scrutiny, responsable de:

- Persistir datos E14 recibidos desde Kafka
- Gestionar el preconteo electoral (importación CSV)
- Detectar anomalías comparando escrutinio vs preconteo
- Exponer API REST para consultas y exportación

**Stack tecnológico:**
- Spring Boot 3.4.5 con Spring WebFlux (reactive)
- PostgreSQL con R2DBC para acceso reactivo
- Consul para service discovery (localhost:8500)
- Kafka para mensajería (localhost:9092)
- Autenticación JWT
- Liquibase para migraciones de BD

El microservicio corre en **puerto 8084** y usa PostgreSQL en **puerto 5434**.

## Development Commands

### Running the Application

```bash
# Iniciar en modo desarrollo
./mvnw

# Iniciar con debugging remoto en puerto 8000
npm run backend:debug
```

**IMPORTANTE**: Requiere que Consul esté corriendo en localhost:8500.

### Database

```bash
# Iniciar PostgreSQL (puerto 5434)
docker compose -f src/main/docker/postgresql.yml up --wait

# Detener PostgreSQL
docker compose -f src/main/docker/postgresql.yml down -v

# Conexión (dev):
# - URL: r2dbc:postgresql://localhost:5434/tyseScrutinyMicroScrutiny
# - Username: tyseScrutinyMicroScrutiny
# - Password: (vacío)
```

### Required Services

**IMPORTANTE**: Este microservicio depende de servicios compartidos que se levantan desde `tyse-infrastructure/`.

```bash
# 1. Levantar infraestructura compartida (una sola vez)
cd ../tyse-infrastructure
docker compose up -d

# Verificar:
docker compose ps

# 2. Levantar PostgreSQL de este microservicio
cd ../tyse-scrutiny-micro-scrutiny
docker compose -f src/main/docker/postgresql.yml up --wait
```

### Testing

```bash
# Ejecutar todos los tests
./mvnw verify

# Tests unitarios sin logs
npm run backend:unit:test

# Tests de integración
./mvnw failsafe:integration-test

# Tests Cucumber BDD
./mvnw integration-test

# Tests de rendimiento Gatling
./mvnw gatling:test
```

### Building

```bash
# Build de producción
./mvnw -Pprod clean verify

# Build Docker image
npm run java:docker

# Build Docker image ARM64 (Apple Silicon)
npm run java:docker:arm64
```

## Architecture

### Microservice in Context

```
tyse-infrastructure/:
├── Consul (localhost:8500)         ← Service discovery
├── Kafka (localhost:9092)          ← Message broker
├── MinIO (localhost:9000/9001)     ← Object storage
└── MailHog (localhost:1025/8025)   ← SMTP desarrollo

Gateway (tysescrutinygateway):
├── Puerto: 8080
└── PostgreSQL: localhost:5432

Microservicio Divipol:
├── Puerto: 8081
└── PostgreSQL: localhost:5433

Microservicio Scrutiny (este repo):
├── Puerto: 8084
└── PostgreSQL: localhost:5434
```

### Kafka Topics

**Consumidos:**
- `e14-data-cleaned` - Datos E14 limpios del pipeline OCR
  - Consumer group: `tyse-scrutiny-micro-scrutiny`

**Producidos:**
- `e14-anomaly-detected` - Anomalías detectadas
- `e14-processing-status` - Estado del procesamiento

### Package Structure

```
com.tyse.scrutiny.micro.scrutiny
├── TyseScrutinyMicroScrutinyApp.java
├── aop/logging/           # Aspectos de logging
├── broker/                # Kafka consumers/producers
├── config/                # Configuración Spring
├── domain/                # Entidades R2DBC
│   ├── ElectionProcess.java
│   ├── ScrutinyDay.java
│   ├── E14Form.java
│   ├── E14Party.java
│   ├── E14Candidate.java
│   ├── Precount.java
│   ├── PrecountCandidate.java
│   ├── ScrutinyResult.java
│   ├── Anomaly.java
│   ├── ProcessedDocument.java
│   └── enumeration/       # Enums (AnomalyType, Severity, etc.)
├── repository/            # Repositorios R2DBC
├── service/               # Lógica de negocio
├── web/rest/              # Controladores REST
└── web/api/               # Delegados OpenAPI generados
```

### Database Schema

**10 tablas principales:**

| Tabla | Descripción |
|-------|-------------|
| `election_process` | Procesos electorales (alcaldía, gobernación, etc.) |
| `scrutiny_day` | Días de escrutinio por proceso |
| `e14_form` | Formularios E14 procesados |
| `e14_party` | Partidos por formulario E14 |
| `e14_candidate` | Candidatos por partido E14 |
| `precount` | Preconteo importado desde CSV |
| `precount_candidate` | Candidatos del preconteo |
| `scrutiny_result` | Votos acumulados por día de escrutinio |
| `anomaly` | Anomalías detectadas |
| `processed_document` | Tracking de procesamiento de PDFs |

**Índices clave:**
- `idx_e14_divipol` - Búsqueda por código divipol
- `idx_anomaly_status` - Filtro por estado de anomalía
- `idx_precount_divipol` - Comparación preconteo vs escrutinio

### Reactive Architecture

Todo el stack es reactivo:
- Controladores retornan `Mono<T>` o `Flux<T>`
- Repositorios R2DBC (no JPA)
- WebClient para HTTP
- Kafka con bindings reactivos

## Configuration

### Profiles

- `dev` (default) - Desarrollo con logs verbosos
- `prod` - Producción optimizada
- `test` / `testdev` / `testprod` - Testing
- `api-docs` - Habilita Swagger UI
- `no-liquibase` - Desactiva migraciones

### Key Configuration Files

- `application.yml` - Configuración base
- `application-dev.yml` - Overrides desarrollo (puerto 5434)
- `bootstrap.yml` - Configuración Consul
- `swagger/api.yml` - Especificación OpenAPI

## API Endpoints (Planned)

```
# Election Processes
GET    /api/election-processes
POST   /api/election-processes
GET    /api/election-processes/{id}
PUT    /api/election-processes/{id}

# E14 Forms
GET    /api/e14/forms
GET    /api/e14/forms/{id}
GET    /api/e14/forms/by-divipol/{key}

# Precount
POST   /api/precounts/upload-csv
GET    /api/precounts
GET    /api/precounts/{id}

# Anomalies
GET    /api/anomalies
GET    /api/anomalies/{id}
PUT    /api/anomalies/{id}/status
GET    /api/anomalies/export/excel

# Processing Status
GET    /api/documents/status
GET    /api/documents/{pdfId}
```

## Testing

- **Testcontainers** para PostgreSQL y Kafka en tests de integración
- **ArchUnit** para validar arquitectura
- **BlockHound** para detectar llamadas bloqueantes
- Requiere Docker para tests de integración

## Important Notes

1. Este es un **microservicio** - diseñado para correr detrás del Gateway
2. CORS deshabilitado por defecto
3. JWT secret configurado en Consul (compartido con otros microservicios)
4. Programación reactiva en todo el stack
5. Ejecutar `./mvnw generate-sources` después de modificar `swagger/api.yml`
