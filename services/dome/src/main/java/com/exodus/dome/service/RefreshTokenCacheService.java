package com.exodus.dome.service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenCacheService {
  private final StringRedisTemplate redisTemplate;

  @Autowired
  public RefreshTokenCacheService(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  // Our key value pair will be looks like: refresh:token:<tokenValue> → userId
  private String buildKey(String token) {
    return "refresh:token:" + token;
  }

  // TTL is 14 days
  public void storeToken(String token, UUID userId, long ttlInSeconds) {
    String key = buildKey(token);
    redisTemplate.opsForValue().set(key, userId.toString(), Duration.ofSeconds(ttlInSeconds));
  }

  // Returns Optional.empty() if token not found or invalid UUID
  // Returns userId if token being found
  public Optional<UUID> getUserIdForToken(String token) {
    String key = buildKey(token);
    String value = redisTemplate.opsForValue().get(key);

    if (value == null) {
      return Optional.empty();
    }

    try {
      return Optional.of(UUID.fromString(value));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  // If token exists, gets user in getUserIdForToken function,
  // then call it to delete token from redis
  public void deleteToken(String token) {
    String key = buildKey(token);
    redisTemplate.delete(key);
  }
}
