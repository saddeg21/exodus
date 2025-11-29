# Exodus

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

## Services

| Service | Port | Its Job                          |
| ------- | ---- | -------------------------------- |
| dome    | 8081 | Authentication & User Management |
| wall    | 8082 | Profile Management Service       |

## Development

```bash
# Build all services
./build-all.sh

# Test
./test-all.sh
```

## License

MIT License
