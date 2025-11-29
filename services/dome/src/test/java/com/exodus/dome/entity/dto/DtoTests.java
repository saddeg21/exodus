package com.exodus.dome.entity.dto;

import com.exodus.dome.enums.UserRole;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class DtoTests {

    // ==================== AuthResponse Tests ====================

    @Test
    void authResponse_constructor_shouldSetAllFields() {
        AuthResponse response = new AuthResponse("access-token", "refresh-token", 3600L);

        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertEquals(3600L, response.getExpiresIn());
        assertEquals("Bearer", response.getTokenType());
    }

    @Test
    void authResponse_setters_shouldUpdateValues() {
        AuthResponse response = new AuthResponse("old-access", "old-refresh", 1800L);

        response.setAccessToken("new-access");
        response.setRefreshToken("new-refresh");
        response.setExpiresIn(7200L);
        response.setTokenType("Custom");

        assertEquals("new-access", response.getAccessToken());
        assertEquals("new-refresh", response.getRefreshToken());
        assertEquals(7200L, response.getExpiresIn());
        assertEquals("Custom", response.getTokenType());
    }

    // ==================== LoginRequest Tests ====================

    @Test
    void loginRequest_settersAndGetters_shouldWork() {
        LoginRequest request = new LoginRequest();

        request.setEmail("test@example.com");
        request.setPassword("password123");

        assertEquals("test@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
    }

    // ==================== RegisterRequest Tests ====================

    @Test
    void registerRequest_settersAndGetters_shouldWork() {
        RegisterRequest request = new RegisterRequest();

        request.setEmail("new@example.com");
        request.setPassword("securePassword");
        request.setRole(UserRole.DRIVER);

        assertEquals("new@example.com", request.getEmail());
        assertEquals("securePassword", request.getPassword());
        assertEquals(UserRole.DRIVER, request.getRole());
    }

    @Test
    void registerRequest_withNullRole_shouldAcceptNull() {
        RegisterRequest request = new RegisterRequest();

        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setRole(null);

        assertNull(request.getRole());
    }

    // ==================== RefreshRequest Tests ====================

    @Test
    void refreshRequest_settersAndGetters_shouldWork() {
        RefreshRequest request = new RefreshRequest();

        request.setRefreshToken("my-refresh-token");

        assertEquals("my-refresh-token", request.getRefreshToken());
    }

    // ==================== LogoutRequest Tests ====================

    @Test
    void logoutRequest_settersAndGetters_shouldWork() {
        LogoutRequest request = new LogoutRequest();

        request.setRefreshToken("logout-refresh-token");

        assertEquals("logout-refresh-token", request.getRefreshToken());
    }

    // ==================== ApiError Tests ====================

    @Test
    void apiError_constructor_shouldSetAllFields() {
        ApiError error = new ApiError(404, "Not Found", "Resource not found", "/api/resource");

        assertEquals(404, error.getStatus());
        assertEquals("Not Found", error.getError());
        assertEquals("Resource not found", error.getMessage());
        assertEquals("/api/resource", error.getPath());
        assertNotNull(error.getTimestamp());
    }

    @Test
    void apiError_timestamp_shouldBeSetAutomatically() {
        Instant before = Instant.now();
        ApiError error = new ApiError(500, "Internal Server Error", "Something went wrong", "/api/test");
        Instant after = Instant.now();

        assertNotNull(error.getTimestamp());
        assertTrue(error.getTimestamp().compareTo(before) >= 0);
        assertTrue(error.getTimestamp().compareTo(after) <= 0);
    }

    @Test
    void apiError_differentStatusCodes_shouldBeAccepted() {
        ApiError badRequest = new ApiError(400, "Bad Request", "Invalid input", "/api/data");
        ApiError unauthorized = new ApiError(401, "Unauthorized", "Auth required", "/api/secure");
        ApiError forbidden = new ApiError(403, "Forbidden", "Access denied", "/api/admin");
        ApiError conflict = new ApiError(409, "Conflict", "Duplicate entry", "/api/create");

        assertEquals(400, badRequest.getStatus());
        assertEquals(401, unauthorized.getStatus());
        assertEquals(403, forbidden.getStatus());
        assertEquals(409, conflict.getStatus());
    }
}
