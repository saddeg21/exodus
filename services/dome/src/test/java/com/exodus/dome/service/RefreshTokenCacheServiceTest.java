package com.exodus.dome.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenCacheServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private RefreshTokenCacheService refreshTokenCacheService;

    @BeforeEach
    void setUp() {
        refreshTokenCacheService = new RefreshTokenCacheService(redisTemplate);
    }

    @Test
    void storeToken_shouldStoreTokenWithTTL() {
        String token = "test-refresh-token";
        UUID userId = UUID.randomUUID();
        long ttlInSeconds = 1209600L; // 14 days

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        refreshTokenCacheService.storeToken(token, userId, ttlInSeconds);

        verify(redisTemplate).opsForValue();
        verify(valueOperations).set(
                eq("refresh:token:" + token),
                eq(userId.toString()),
                eq(Duration.ofSeconds(ttlInSeconds)));
    }

    @Test
    void getUserIdForToken_whenTokenExists_shouldReturnUserId() {
        String token = "existing-token";
        UUID expectedUserId = UUID.randomUUID();

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("refresh:token:" + token)).thenReturn(expectedUserId.toString());

        Optional<UUID> result = refreshTokenCacheService.getUserIdForToken(token);

        assertTrue(result.isPresent());
        assertEquals(expectedUserId, result.get());
    }

    @Test
    void getUserIdForToken_whenTokenDoesNotExist_shouldReturnEmpty() {
        String token = "non-existing-token";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("refresh:token:" + token)).thenReturn(null);

        Optional<UUID> result = refreshTokenCacheService.getUserIdForToken(token);

        assertTrue(result.isEmpty());
    }

    @Test
    void getUserIdForToken_whenValueIsInvalidUUID_shouldReturnEmpty() {
        String token = "invalid-uuid-token";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("refresh:token:" + token)).thenReturn("not-a-valid-uuid");

        Optional<UUID> result = refreshTokenCacheService.getUserIdForToken(token);

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteToken_shouldDeleteFromRedis() {
        String token = "token-to-delete";

        refreshTokenCacheService.deleteToken(token);

        verify(redisTemplate).delete("refresh:token:" + token);
    }

    @Test
    void storeToken_multipleTokens_shouldStoreAll() {
        String token1 = "token-1";
        String token2 = "token-2";
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        long ttl = 3600L;

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        refreshTokenCacheService.storeToken(token1, userId1, ttl);
        refreshTokenCacheService.storeToken(token2, userId2, ttl);

        verify(valueOperations).set(eq("refresh:token:" + token1), eq(userId1.toString()), any(Duration.class));
        verify(valueOperations).set(eq("refresh:token:" + token2), eq(userId2.toString()), any(Duration.class));
    }

    @Test
    void getUserIdForToken_withEmptyString_shouldReturnEmpty() {
        String token = "";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("refresh:token:")).thenReturn(null);

        Optional<UUID> result = refreshTokenCacheService.getUserIdForToken(token);

        assertTrue(result.isEmpty());
    }
}
