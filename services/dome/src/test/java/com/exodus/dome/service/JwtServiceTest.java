package com.exodus.dome.service;

import com.exodus.dome.entity.UserEntity;
import com.exodus.dome.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        // Use a 256-bit (32 byte) secret key for HS256
        String secret = "verysecuresecretkeyforjwttestingpurposes12345";
        String issuer = "test-issuer";
        long accessTokenValiditySeconds = 3600L; // 1 hour

        jwtService = new JwtService(secret, issuer, accessTokenValiditySeconds);

        testUser = new UserEntity(
                UUID.randomUUID(),
                "test@example.com",
                "hashedPassword123",
                UserRole.RIDER,
                true,
                Instant.now(),
                null);
    }

    @Test
    void generateAccessToken_shouldReturnValidToken() {
        String token = jwtService.generateAccessToken(testUser);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateAccessToken_shouldContainCorrectClaims() {
        String token = jwtService.generateAccessToken(testUser);

        Jws<Claims> claims = jwtService.validate(token);

        assertNotNull(claims);
        assertEquals(testUser.getId().toString(), claims.getBody().getSubject());
        assertEquals("test-issuer", claims.getBody().getIssuer());
        assertEquals("RIDER", claims.getBody().get("role", String.class));
    }

    @Test
    void generateAccessToken_forDriverRole_shouldContainDriverRole() {
        testUser.setRole(UserRole.DRIVER);

        String token = jwtService.generateAccessToken(testUser);
        Jws<Claims> claims = jwtService.validate(token);

        assertEquals("DRIVER", claims.getBody().get("role", String.class));
    }

    @Test
    void generateAccessToken_forAdminRole_shouldContainAdminRole() {
        testUser.setRole(UserRole.ADMIN);

        String token = jwtService.generateAccessToken(testUser);
        Jws<Claims> claims = jwtService.validate(token);

        assertEquals("ADMIN", claims.getBody().get("role", String.class));
    }

    @Test
    void validate_withValidToken_shouldReturnClaims() {
        String token = jwtService.generateAccessToken(testUser);

        Jws<Claims> claims = jwtService.validate(token);

        assertNotNull(claims);
        assertNotNull(claims.getBody());
        assertNotNull(claims.getBody().getSubject());
    }

    @Test
    void validate_withInvalidToken_shouldThrowException() {
        String invalidToken = "invalid.token.here";

        assertThrows(IllegalArgumentException.class, () -> {
            jwtService.validate(invalidToken);
        });
    }

    @Test
    void validate_withTamperedToken_shouldThrowException() {
        String token = jwtService.generateAccessToken(testUser);
        String tamperedToken = token.substring(0, token.length() - 5) + "XXXXX";

        assertThrows(IllegalArgumentException.class, () -> {
            jwtService.validate(tamperedToken);
        });
    }

    @Test
    void validate_withDifferentSecretKey_shouldThrowException() {
        String token = jwtService.generateAccessToken(testUser);

        JwtService differentJwtService = new JwtService(
                "differentsecretkeythatisatleast32characters",
                "test-issuer",
                3600L);

        assertThrows(IllegalArgumentException.class, () -> {
            differentJwtService.validate(token);
        });
    }

    @Test
    void generateAccessToken_shouldHaveValidExpiration() {
        String token = jwtService.generateAccessToken(testUser);
        Jws<Claims> claims = jwtService.validate(token);

        assertNotNull(claims.getBody().getExpiration());
        assertTrue(claims.getBody().getExpiration().after(claims.getBody().getIssuedAt()));
    }

    @Test
    void generateAccessToken_forMultipleUsers_shouldGenerateDifferentTokens() {
        UserEntity user1 = new UserEntity(
                UUID.randomUUID(),
                "user1@example.com",
                "password1",
                UserRole.RIDER,
                true,
                Instant.now(),
                null);

        UserEntity user2 = new UserEntity(
                UUID.randomUUID(),
                "user2@example.com",
                "password2",
                UserRole.DRIVER,
                true,
                Instant.now(),
                null);

        String token1 = jwtService.generateAccessToken(user1);
        String token2 = jwtService.generateAccessToken(user2);

        assertNotEquals(token1, token2);
    }
}
