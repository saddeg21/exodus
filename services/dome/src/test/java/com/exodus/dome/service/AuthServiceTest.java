package com.exodus.dome.service;

import com.exodus.dome.entity.RefreshToken;
import com.exodus.dome.entity.UserEntity;
import com.exodus.dome.entity.dto.AuthResponse;
import com.exodus.dome.entity.dto.LoginRequest;
import com.exodus.dome.entity.dto.LogoutRequest;
import com.exodus.dome.entity.dto.RefreshRequest;
import com.exodus.dome.entity.dto.RegisterRequest;
import com.exodus.dome.enums.UserRole;
import com.exodus.dome.exception.*;
import com.exodus.dome.repository.RefreshTokenRepository;
import com.exodus.dome.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenCacheService refreshTokenCacheService;

    private AuthService authService;

    private UserEntity testUser;
    private RefreshToken testRefreshToken;

    @BeforeEach
    void setUp() {
        long refreshTokenValiditySeconds = 1209600L; // 14 days

        authService = new AuthService(
                userRepository,
                refreshTokenRepository,
                passwordEncoder,
                jwtService,
                refreshTokenValiditySeconds,
                refreshTokenCacheService);

        testUser = new UserEntity(
                UUID.randomUUID(),
                "test@example.com",
                "hashedPassword",
                UserRole.RIDER,
                true,
                Instant.now(),
                null);

        testRefreshToken = new RefreshToken(
                UUID.randomUUID(),
                testUser,
                "test-refresh-token",
                Instant.now().plusSeconds(86400),
                Instant.now(),
                null);
    }

    // ==================== Register Tests ====================

    @Test
    void register_withNewEmail_shouldSaveUser() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("new@example.com");
        request.setPassword("password123");
        request.setRole(UserRole.RIDER);

        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        authService.register(request);

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());

        UserEntity savedUser = userCaptor.getValue();
        assertEquals("new@example.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPasswordHash());
        assertEquals(UserRole.RIDER, savedUser.getRole());
        assertTrue(savedUser.isActive());
    }

    @Test
    void register_withExistingEmail_shouldThrowDuplicateValueException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(testUser));

        assertThrows(DuplicateValueException.class, () -> {
            authService.register(request);
        });

        verify(userRepository, never()).save(any());
    }

    @Test
    void register_withoutRole_shouldDefaultToRider() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("newuser@example.com");
        request.setPassword("password123");
        request.setRole(null);

        when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        authService.register(request);

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());

        assertEquals(UserRole.RIDER, userCaptor.getValue().getRole());
    }

    @Test
    void register_withDriverRole_shouldSetDriverRole() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("driver@example.com");
        request.setPassword("password123");
        request.setRole(UserRole.DRIVER);

        when(userRepository.findByEmail("driver@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        authService.register(request);

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());

        assertEquals(UserRole.DRIVER, userCaptor.getValue().getRole());
    }

    // ==================== Login Tests ====================

    @Test
    void login_withValidCredentials_shouldReturnAuthResponse() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("correctPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("correctPassword", "hashedPassword")).thenReturn(true);
        when(jwtService.generateAccessToken(testUser)).thenReturn("access-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
        verify(refreshTokenCacheService).storeToken(anyString(), eq(testUser.getId()), anyLong());
    }

    @Test
    void login_withNonExistentUser_shouldThrowUserNotFoundException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("nonexistent@example.com");
        request.setPassword("password");

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            authService.login(request);
        });
    }

    @Test
    void login_withInactiveUser_shouldThrowUserNotActiveException() {
        testUser.setActive(false);

        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        assertThrows(UserNotActiveException.class, () -> {
            authService.login(request);
        });
    }

    @Test
    void login_withWrongPassword_shouldThrowPasswordNotCorrectException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrongPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        assertThrows(PasswordNotCorrectException.class, () -> {
            authService.login(request);
        });
    }

    // ==================== Refresh Tests ====================

    @Test
    void refresh_withValidToken_shouldReturnNewTokens() {
        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken("valid-refresh-token");

        when(refreshTokenCacheService.getUserIdForToken("valid-refresh-token"))
                .thenReturn(Optional.of(testUser.getId()));
        when(refreshTokenRepository.findByToken("valid-refresh-token"))
                .thenReturn(Optional.of(testRefreshToken));
        when(jwtService.generateAccessToken(testUser)).thenReturn("new-access-token");

        AuthResponse response = authService.refresh(request);

        assertNotNull(response);
        assertEquals("new-access-token", response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        verify(refreshTokenCacheService).deleteToken("valid-refresh-token");
        verify(refreshTokenRepository).save(testRefreshToken);
    }

    @Test
    void refresh_withNonExistentToken_shouldThrowRefreshTokenNotFoundException() {
        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken("non-existent-token");

        when(refreshTokenCacheService.getUserIdForToken("non-existent-token"))
                .thenReturn(Optional.empty());
        when(refreshTokenRepository.findByToken("non-existent-token"))
                .thenReturn(Optional.empty());

        assertThrows(RefreshTokenNotFoundException.class, () -> {
            authService.refresh(request);
        });
    }

    @Test
    void refresh_withRevokedToken_shouldThrowInvalidRefreshTokenException() {
        testRefreshToken.setRevokedAt(Instant.now());

        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken("revoked-token");

        when(refreshTokenCacheService.getUserIdForToken("revoked-token"))
                .thenReturn(Optional.empty());
        when(refreshTokenRepository.findByToken("revoked-token"))
                .thenReturn(Optional.of(testRefreshToken));

        assertThrows(InvalidRefreshTokenException.class, () -> {
            authService.refresh(request);
        });
    }

    @Test
    void refresh_withInactiveUser_shouldThrowUserNotActiveException() {
        testUser.setActive(false);

        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken("valid-token");

        when(refreshTokenCacheService.getUserIdForToken("valid-token"))
                .thenReturn(Optional.of(testUser.getId()));
        when(refreshTokenRepository.findByToken("valid-token"))
                .thenReturn(Optional.of(testRefreshToken));

        assertThrows(UserNotActiveException.class, () -> {
            authService.refresh(request);
        });
    }

    // ==================== Logout Tests ====================

    @Test
    void logout_withValidToken_shouldRevokeToken() {
        LogoutRequest request = new LogoutRequest();
        request.setRefreshToken("valid-logout-token");

        when(refreshTokenRepository.findByToken("valid-logout-token"))
                .thenReturn(Optional.of(testRefreshToken));

        authService.logout(request);

        assertNotNull(testRefreshToken.getRevokedAt());
        verify(refreshTokenRepository).save(testRefreshToken);
        verify(refreshTokenCacheService).deleteToken("valid-logout-token");
    }

    @Test
    void logout_withAlreadyRevokedToken_shouldDoNothing() {
        testRefreshToken.setRevokedAt(Instant.now().minusSeconds(3600));

        LogoutRequest request = new LogoutRequest();
        request.setRefreshToken("already-revoked-token");

        when(refreshTokenRepository.findByToken("already-revoked-token"))
                .thenReturn(Optional.of(testRefreshToken));

        authService.logout(request);

        // According to AuthService implementation, if token is already revoked,
        // it returns early without calling deleteToken or save
        verify(refreshTokenRepository, never()).save(any());
        verify(refreshTokenCacheService, never()).deleteToken(anyString());
    }

    @Test
    void logout_withNonExistentToken_shouldThrowRefreshTokenNotFoundException() {
        LogoutRequest request = new LogoutRequest();
        request.setRefreshToken("non-existent-token");

        when(refreshTokenRepository.findByToken("non-existent-token"))
                .thenReturn(Optional.empty());

        assertThrows(RefreshTokenNotFoundException.class, () -> {
            authService.logout(request);
        });
    }

    // ==================== LogoutAllSessions Tests ====================

    @Test
    void logoutAllSessions_shouldRevokeAllUserTokens() {
        RefreshToken token1 = new RefreshToken(UUID.randomUUID(), testUser, "token1",
                Instant.now().plusSeconds(86400), Instant.now(), null);
        RefreshToken token2 = new RefreshToken(UUID.randomUUID(), testUser, "token2",
                Instant.now().plusSeconds(86400), Instant.now(), null);
        List<RefreshToken> allTokens = Arrays.asList(token1, token2);

        LogoutRequest request = new LogoutRequest();
        request.setRefreshToken("token1");

        when(refreshTokenRepository.findByToken("token1"))
                .thenReturn(Optional.of(token1));
        when(refreshTokenRepository.findAllByUserId(testUser.getId()))
                .thenReturn(allTokens);

        authService.logoutAllSessions(request);

        assertNotNull(token1.getRevokedAt());
        assertNotNull(token2.getRevokedAt());
        verify(refreshTokenRepository).saveAll(allTokens);
        verify(refreshTokenCacheService).deleteToken("token1");
        verify(refreshTokenCacheService).deleteToken("token2");
    }

    @Test
    void logoutAllSessions_withNonExistentToken_shouldThrowRefreshTokenNotFoundException() {
        LogoutRequest request = new LogoutRequest();
        request.setRefreshToken("non-existent-token");

        when(refreshTokenRepository.findByToken("non-existent-token"))
                .thenReturn(Optional.empty());

        assertThrows(RefreshTokenNotFoundException.class, () -> {
            authService.logoutAllSessions(request);
        });
    }

    @Test
    void logoutAllSessions_withAlreadyRevokedTokens_shouldNotRevokeAgain() {
        RefreshToken token1 = new RefreshToken(UUID.randomUUID(), testUser, "token1",
                Instant.now().plusSeconds(86400), Instant.now(), Instant.now().minusSeconds(100));
        RefreshToken token2 = new RefreshToken(UUID.randomUUID(), testUser, "token2",
                Instant.now().plusSeconds(86400), Instant.now(), null);
        List<RefreshToken> allTokens = Arrays.asList(token1, token2);

        LogoutRequest request = new LogoutRequest();
        request.setRefreshToken("token2");

        when(refreshTokenRepository.findByToken("token2"))
                .thenReturn(Optional.of(token2));
        when(refreshTokenRepository.findAllByUserId(testUser.getId()))
                .thenReturn(allTokens);

        Instant originalRevokedAt = token1.getRevokedAt();

        authService.logoutAllSessions(request);

        assertEquals(originalRevokedAt, token1.getRevokedAt());
        assertNotNull(token2.getRevokedAt());
    }
}
