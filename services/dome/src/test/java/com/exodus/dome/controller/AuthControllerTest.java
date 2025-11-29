package com.exodus.dome.controller;

import com.exodus.dome.entity.dto.AuthResponse;
import com.exodus.dome.entity.dto.LoginRequest;
import com.exodus.dome.entity.dto.LogoutRequest;
import com.exodus.dome.entity.dto.RefreshRequest;
import com.exodus.dome.entity.dto.RegisterRequest;
import com.exodus.dome.enums.UserRole;
import com.exodus.dome.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authService);
    }

    // ==================== Register Tests ====================

    @Test
    void register_shouldReturnOk() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setRole(UserRole.RIDER);

        doNothing().when(authService).register(any(RegisterRequest.class));

        ResponseEntity<Void> response = authController.register(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authService).register(request);
    }

    @Test
    void register_withDriverRole_shouldReturnOk() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("driver@example.com");
        request.setPassword("securePassword");
        request.setRole(UserRole.DRIVER);

        doNothing().when(authService).register(any(RegisterRequest.class));

        ResponseEntity<Void> response = authController.register(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authService).register(request);
    }

    // ==================== Login Tests ====================

    @Test
    void login_shouldReturnAuthResponse() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        AuthResponse expectedResponse = new AuthResponse("access-token", "refresh-token", 3600L);
        when(authService.login(any(LoginRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("access-token", response.getBody().getAccessToken());
        assertEquals("refresh-token", response.getBody().getRefreshToken());
        assertEquals(3600L, response.getBody().getExpiresIn());
        verify(authService).login(request);
    }

    @Test
    void login_shouldCallAuthServiceWithCorrectRequest() {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setPassword("myPassword");

        AuthResponse mockResponse = new AuthResponse("token", "refresh", 1800L);
        when(authService.login(request)).thenReturn(mockResponse);

        authController.login(request);

        verify(authService, times(1)).login(request);
    }

    // ==================== Refresh Tests ====================

    @Test
    void refresh_shouldReturnNewAuthResponse() {
        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken("old-refresh-token");

        AuthResponse expectedResponse = new AuthResponse("new-access-token", "new-refresh-token", 3600L);
        when(authService.refresh(any(RefreshRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<AuthResponse> response = authController.refresh(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("new-access-token", response.getBody().getAccessToken());
        assertEquals("new-refresh-token", response.getBody().getRefreshToken());
        verify(authService).refresh(request);
    }

    @Test
    void refresh_shouldCallAuthServiceWithCorrectRequest() {
        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken("my-refresh-token");

        AuthResponse mockResponse = new AuthResponse("access", "refresh", 7200L);
        when(authService.refresh(request)).thenReturn(mockResponse);

        authController.refresh(request);

        verify(authService, times(1)).refresh(request);
    }

    // ==================== Logout Tests ====================

    @Test
    void logout_shouldReturnNoContent() {
        LogoutRequest request = new LogoutRequest();
        request.setRefreshToken("refresh-token-to-logout");

        doNothing().when(authService).logout(any(LogoutRequest.class));

        ResponseEntity<Void> response = authController.logout(request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(authService).logout(request);
    }

    @Test
    void logout_shouldCallAuthServiceWithCorrectRequest() {
        LogoutRequest request = new LogoutRequest();
        request.setRefreshToken("token-to-revoke");

        doNothing().when(authService).logout(request);

        authController.logout(request);

        verify(authService, times(1)).logout(request);
    }

    // ==================== LogoutAll Tests ====================

    @Test
    void logoutAll_shouldReturnNoContent() {
        LogoutRequest request = new LogoutRequest();
        request.setRefreshToken("current-refresh-token");

        doNothing().when(authService).logoutAllSessions(any(LogoutRequest.class));

        ResponseEntity<Void> response = authController.logoutAll(request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(authService).logoutAllSessions(request);
    }

    @Test
    void logoutAll_shouldCallAuthServiceWithCorrectRequest() {
        LogoutRequest request = new LogoutRequest();
        request.setRefreshToken("user-session-token");

        doNothing().when(authService).logoutAllSessions(request);

        authController.logoutAll(request);

        verify(authService, times(1)).logoutAllSessions(request);
    }
}
