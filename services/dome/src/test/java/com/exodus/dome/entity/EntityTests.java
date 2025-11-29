package com.exodus.dome.entity;

import com.exodus.dome.enums.UserRole;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EntityTests {

    // ==================== UserEntity Tests ====================

    @Test
    void userEntity_createWithAllArgs_shouldSetAllFields() {
        UUID id = UUID.randomUUID();
        String email = "test@example.com";
        String passwordHash = "hashedPassword";
        UserRole role = UserRole.RIDER;
        boolean active = true;
        Instant createdAt = Instant.now();
        Instant lastLoginAt = Instant.now();

        UserEntity user = new UserEntity(id, email, passwordHash, role, active, createdAt, lastLoginAt);

        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(passwordHash, user.getPasswordHash());
        assertEquals(role, user.getRole());
        assertTrue(user.isActive());
        assertEquals(createdAt, user.getCreatedAt());
        assertEquals(lastLoginAt, user.getLastLoginAt());
    }

    @Test
    void userEntity_settersAndGetters_shouldWork() {
        UserEntity user = new UserEntity();

        UUID id = UUID.randomUUID();
        user.setId(id);
        user.setEmail("new@example.com");
        user.setPasswordHash("newHash");
        user.setRole(UserRole.DRIVER);
        user.setActive(false);
        user.setCreatedAt(Instant.now());
        user.setLastLoginAt(Instant.now());

        assertEquals(id, user.getId());
        assertEquals("new@example.com", user.getEmail());
        assertEquals("newHash", user.getPasswordHash());
        assertEquals(UserRole.DRIVER, user.getRole());
        assertFalse(user.isActive());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getLastLoginAt());
    }

    @Test
    void userEntity_isActive_shouldReturnCorrectValue() {
        UserEntity activeUser = new UserEntity();
        activeUser.setActive(true);

        UserEntity inactiveUser = new UserEntity();
        inactiveUser.setActive(false);

        assertTrue(activeUser.isActive());
        assertFalse(inactiveUser.isActive());
    }

    // ==================== RefreshToken Tests ====================

    @Test
    void refreshToken_createWithAllArgs_shouldSetAllFields() {
        UUID id = UUID.randomUUID();
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        String token = "test-token";
        Instant expiresAt = Instant.now().plusSeconds(86400);
        Instant createdAt = Instant.now();
        Instant revokedAt = null;

        RefreshToken refreshToken = new RefreshToken(id, user, token, expiresAt, createdAt, revokedAt);

        assertEquals(id, refreshToken.getId());
        assertEquals(user, refreshToken.getUser());
        assertEquals(token, refreshToken.getToken());
        assertEquals(expiresAt, refreshToken.getExpiresAt());
        assertEquals(createdAt, refreshToken.getCreatedAt());
        assertNull(refreshToken.getRevokedAt());
    }

    @Test
    void refreshToken_settersAndGetters_shouldWork() {
        RefreshToken refreshToken = new RefreshToken();

        UUID id = UUID.randomUUID();
        UserEntity user = new UserEntity();
        Instant now = Instant.now();

        refreshToken.setId(id);
        refreshToken.setUser(user);
        refreshToken.setToken("new-token");
        refreshToken.setExpiresAt(now.plusSeconds(3600));
        refreshToken.setCreatedAt(now);
        refreshToken.setRevokedAt(now.plusSeconds(1800));

        assertEquals(id, refreshToken.getId());
        assertEquals(user, refreshToken.getUser());
        assertEquals("new-token", refreshToken.getToken());
        assertNotNull(refreshToken.getExpiresAt());
        assertNotNull(refreshToken.getCreatedAt());
        assertNotNull(refreshToken.getRevokedAt());
    }

    @Test
    void refreshToken_isExpired_whenNotExpired_shouldReturnFalse() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiresAt(Instant.now().plusSeconds(3600)); // 1 hour in future

        assertFalse(refreshToken.isExpired());
    }

    @Test
    void refreshToken_isExpired_whenExpired_shouldReturnTrue() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiresAt(Instant.now().minusSeconds(3600)); // 1 hour in past

        assertTrue(refreshToken.isExpired());
    }

    @Test
    void refreshToken_isRevoked_whenNotRevoked_shouldReturnFalse() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRevokedAt(null);

        assertFalse(refreshToken.isRevoked());
    }

    @Test
    void refreshToken_isRevoked_whenRevoked_shouldReturnTrue() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRevokedAt(Instant.now());

        assertTrue(refreshToken.isRevoked());
    }

    @Test
    void refreshToken_isExpiredAndRevoked_shouldReturnCorrectValues() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiresAt(Instant.now().minusSeconds(3600)); // expired
        refreshToken.setRevokedAt(Instant.now().minusSeconds(1800)); // revoked

        assertTrue(refreshToken.isExpired());
        assertTrue(refreshToken.isRevoked());
    }
}
