# Dome

[![CI](https://github.com/saddeg21/exodus/actions/workflows/ci.yml/badge.svg?branch=develop)](https://github.com/saddeg21/exodus/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/saddeg21/exodus/branch/develop/graph/badge.svg)](https://codecov.io/gh/saddeg21/exodus)

Dome is the authentication and authorization service for the Exodus platform. It handles user registration, login, and token management.

## Overview

The service uses JWT (JSON Web Tokens) for authentication. Access tokens are short-lived and used for API authorization. Refresh tokens are stored in Redis to allow token rotation and invalidation on logout.

## Endpoints

| Method | Endpoint           | Description              |
| ------ | ------------------ | ------------------------ |
| POST   | /api/auth/register | Register a new user      |
| POST   | /api/auth/login    | Login and get tokens     |
| POST   | /api/auth/refresh  | Refresh access token     |
| POST   | /api/auth/logout   | Invalidate refresh token |

## Configuration

Key properties in `application.yaml`:

```yaml
jwt:
  secret: your-secret-key
  access-token-expiration: 900000 # 15 minutes
  refresh-token-expiration: 604800000 # 7 days

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/exodus
  data:
    redis:
      host: localhost
      port: 6379
```

## Port

Runs on port 8081.
