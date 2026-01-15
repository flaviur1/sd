# Sistem de Management Dispozitive

Aplicație bazată pe microservicii pentru gestionarea consumului de energie ale unor dispozitive.

## Tehnologii

- **Backend**: Java, Spring Boot
- **Frontend**: React + TypeScript + Vite + Material-UI
- **Bază de date**: PostgreSQL
- **Message Broker**: RabbitMQ
- **Real-time Communication**: WebSocket
- **Reverse Proxy & API Gateway**: Traefik
- **Containerizare**: Docker
- **Autentificare**: JWT
- **Simulator**: Python

## Structura proiectului

- **auth_microservice** – autentificare (login, register, JWT)
- **user_microservice** – CRUD utilizatori
- **device_microservice** – CRUD dispozitive, device-user
- **monitoring_microservice** – monitorizare consum energie, WebSocket real-time
- **frontend** – interfață web React
- **simulator.py** – simulator pentru generare date consum energie
- **docker-compose.yml** – orchestrare containere
- **traefik.yml** – configurare reverse proxy

## Cerințe de sistem

- Docker & Docker Compose
- Java JDK 21+ (pentru development local)
- Node.js & npm (pentru development local)
- Python 3.8+ (pentru simulator)

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

**Monitoring (monitorDB)**:
- Device (cache):
  - id: UUID
  - userId: UUID
- EnergyReading:
  - id: Long
  - deviceId: UUID
  - timestamp: Timestamp
  - energyConsumption: Double

## Rulare proiect

```bash
git clone <repo-url>
```

### Build imagini Docker

```bash
docker build -t auth-service:latest ./auth_microservice

docker build -t user-service:latest ./user_microservice

docker build -t device-service:latest ./device_microservice

docker build -t monitoring-service:latest ./monitoring_microservice

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

- **Frontend**: http://localhost
- **Traefik Dashboard**: http://localhost:8080
- **RabbitMQ Management**: http://localhost:15672 (guest/guest)

### Simulator consum energie

Pentru a genera date de consum energie în timp real:

```bash
python simulator.py <device id>
```

Simulatorul trimite date către serviciul de monitoring prin RabbitMQ.
