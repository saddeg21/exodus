# Exodus - Routing Solution

## Project Structure

```
exodus/
├── services/                    # Microservices Folder
│   ├── dome/                    # Fully capable auth server
│   ├── wall/                    # Profile Service
│   └── [....]/                  # Upcoming Services
├── docker-compose.yml
├── .github/workflows/           # CI/CD pipelines for standalone builds.
└── README.md
```

### Requirements

- Docker & Docker Compose
- Java 17+
- Maven 3.8+
- Spring Boot 3
- Redis
- Postgresql 16

### Run a Service

```bash
cd services/dome
./mvnw spring-boot:run
```

## Services

| Servis | Port | Açıklama                         |
| ------ | ---- | -------------------------------- |
| dome   | 8081 | Authentication & User Management |
| wall   | 8082 | Profile Management Service       |

## Environment Variables

- For now the arbitrary values are being held in repo.

## 🔧 Development

```bash
# Build all services
./build-all.sh

# Test
./test-all.sh
```

## 📝 License

MIT License
