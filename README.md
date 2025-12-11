# Sistem de Management Dispozitive

Aplicație bazată pe microservicii pentru gestionarea consumului de energie ale unor dispozitive.

## Tehnologii

- **Backend**: Java, Spring Boot
- **Frontend**: React + TypeScript + Vite + Material-UI
- **Bază de date**: PostgreSQL
- **Reverse Proxy & API Gateway**: Traefik
- **Containerizare**: Docker
- **Autentificare**: JWT

## Structura proiectului

- **auth_microservice** – autentificare (login, register, JWT)
- **user_microservice** – CRUD utilizatori
- **device_microservice** – CRUD dispozitive, device-user
- **frontend** – interfață web React
- **docker-compose.yml** – orchestrare containere
- **traefik.yml** – configurare reverse proxy

## Cerințe de sistem

- Docker & Docker Compose
- Java JDK 21+ (pentru development local)
- Node.js & npm (pentru development local)

## Structura bazei de date

**User (userDB)**:
- id: UUID
- name: String
- address: String
- age: int

**Device (deviceDB)**:
- id: UUID
- manufacturer: String
- model: String
- maxHourlyConsumption: Double
- userId: UUID (foreign key)

**AuthUser (authDB)**:
- id: Long
- name: String
- password: String (hashed)
- roles: String

## Rulare proiect

```bash
git clone <repo-url>
```

### Build imagini Docker

```bash
docker build -t auth-service:latest ./auth_microservice

docker build -t user-service:latest ./user_microservice

docker build -t device-service:latest ./device_microservice

docker build -t frontend:latest ./frontend
```

### Pornire aplicație

```bash
docker-compose up -d
```

### Oprire aplicație

```bash
docker-compose down
```

## Accesare aplicație

- **Frontend**: Configurabil prin Traefik (vezi dynamic config)
- **Traefik Dashboard**: http://localhost:8080
- **Baze de date** (debugging):
  - User DB: `localhost:5432`
  - Device DB: `localhost:5433`
  - Auth DB: `localhost:5434`
