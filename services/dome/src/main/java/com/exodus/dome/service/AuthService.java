package com.exodus.dome.service;

import com.exodus.dome.entity.RefreshToken;
import com.exodus.dome.entity.UserEntity;
import com.exodus.dome.entity.dto.AuthResponse;
import com.exodus.dome.entity.dto.LoginRequest;
import com.exodus.dome.entity.dto.LogoutRequest;
import com.exodus.dome.entity.dto.RefreshRequest;
import com.exodus.dome.entity.dto.RegisterRequest;
import com.exodus.dome.enums.UserRole;
import com.exodus.dome.exception.DuplicateValueException;
import com.exodus.dome.exception.InvalidRefreshTokenException;
import com.exodus.dome.exception.PasswordNotCorrectException;
import com.exodus.dome.exception.RefreshTokenNotFoundException;
import com.exodus.dome.exception.UserNotActiveException;
import com.exodus.dome.exception.UserNotFoundException;
import com.exodus.dome.repository.RefreshTokenRepository;
import com.exodus.dome.repository.UserRepository;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final long refreshTokenValiditySeconds;
  private final SecureRandom secureRandom = new SecureRandom();
  private RefreshTokenCacheService refreshTokenCacheService;
  // For generating secure random values

  @Autowired
  public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository,
                     PasswordEncoder passwordEncoder,
                     JwtService jwtService, @Value("${auth.jwt.refresh-token-validity-seconds}")
                     long refreshTokenValiditySeconds,
                     RefreshTokenCacheService refreshTokenCacheService) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    this.refreshTokenCacheService = refreshTokenCacheService;
  }

  public void register(RegisterRequest request) {
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new DuplicateValueException("Email already in use by another account.");
    }

    // it is arbitrary, it will be changed.
    UserRole role = request.getRole() != null ? request.getRole() : UserRole.RIDER;

    UserEntity user = new UserEntity(
        UUID.randomUUID(),
        request.getEmail(),
        passwordEncoder.encode(request.getPassword()),
        role,
        true,
        Instant.now(),
        null
    );

    userRepository.save(user);
  }

  public AuthResponse login(LoginRequest request) {
    UserEntity user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new UserNotFoundException("User with given email not found."));

    if (!user.isActive()) {
      throw new UserNotActiveException("User account is not active.");
    }

    if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
      throw new PasswordNotCorrectException("Provided password is not correct.");
    }

    String accessToken = jwtService.generateAccessToken(user);
    String refreshToken = generateAndStoreRefreshToken(user);

    return new AuthResponse(accessToken, refreshToken, refreshTokenValiditySeconds);
  }

  @Transactional
  public AuthResponse refresh(RefreshRequest request) {
    String tokenValue = request.getRefreshToken();

    // 1) First check redis if token exists
    Optional<UUID> userIdFromCache = refreshTokenCacheService.getUserIdForToken(tokenValue);

    RefreshToken storedToken;

    if (userIdFromCache.isEmpty()) {
      // 2) If not in cache, check DB
      storedToken = refreshTokenRepository.findByToken(request.getRefreshToken())
          .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh Token not found!"));

      // if token is suspicious, throw error
      if (storedToken.isRevoked() || storedToken.isExpired()) {
        throw new InvalidRefreshTokenException("Refresh token is expired or revoked");
      }

      // cache miss :///
    } else {
      // cache hit :)
      storedToken = refreshTokenRepository.findByToken(request.getRefreshToken())
          .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh Token not found!"));
    }

    // Delete token from redis if it does exist
    refreshTokenCacheService.deleteToken(tokenValue);

    // if cache hit, then check db for suspicious token
    if (storedToken.isRevoked() || storedToken.isExpired()) {
      throw new InvalidRefreshTokenException("Refresh token is expired or revoked");
    }

    //**
    // Because I made user lazy loading, need to fetch user to check active status
    // but it causes LazyInitializationException if not in transaction
    // so, either make it EAGER or use @Transactional. EAGER is not recommended lol :)
    //**
    UserEntity user = storedToken.getUser();
    if (!user.isActive()) {
      throw new UserNotActiveException("User account is not active.");
    }

    // Refresh Token Generation
    // 1) Revoke the old refresh token
    storedToken.setRevokedAt(Instant.now());
    refreshTokenRepository.save(storedToken); // Revoked so the deadline is over

    // 2) Generate new tokens (access + refresh)
    String accessToken = jwtService.generateAccessToken(user);
    String newRefreshToken = generateAndStoreRefreshToken(user);

    return new AuthResponse(accessToken,
        newRefreshToken,
        refreshTokenValiditySeconds);
  }

  public void logout(LogoutRequest logoutRequest) {
    String tokenValue = logoutRequest.getRefreshToken();

    // 1) Find token in DB
    RefreshToken storedToken = refreshTokenRepository.findByToken(tokenValue)
        .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh Token not found!"));

    if (storedToken != null) {
      // Check if it revoked, if it is then do nothing
      if (storedToken.isRevoked() || storedToken.isExpired()) {
        return; // already revoked or expired
      } else {
        storedToken.setRevokedAt(Instant.now());
        refreshTokenRepository.save(storedToken);
      }
    }

    // 2) Remove token from redis cache
    refreshTokenCacheService.deleteToken(tokenValue);
  }

  public void logoutAllSessions(LogoutRequest request) {
    String tokenValue = request.getRefreshToken();

    RefreshToken currentToken = refreshTokenRepository.findByToken(tokenValue)
        .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh Token not found!"));

    UserEntity user = currentToken.getUser();
    UUID userId = user.getId();

    List<RefreshToken> allTokens = refreshTokenRepository.findAllByUserId(userId);

    Instant now = Instant.now();

    for (RefreshToken rt : allTokens) {
      if (!rt.isRevoked() && !rt.isExpired()) {
        rt.setRevokedAt(now);
      }

      refreshTokenCacheService.deleteToken(rt.getToken());
    }

    refreshTokenRepository.saveAll(allTokens);
  }

  private String generateAndStoreRefreshToken(UserEntity user) {
    String tokenValue = generateSecureRandomToken();
    Instant now = Instant.now();
    Instant expiry = now.plusSeconds(refreshTokenValiditySeconds);

    // 1) Token is being stored in DB for reuse detection and etc.
    RefreshToken refreshToken = new RefreshToken(
        UUID.randomUUID(),
        user,
        tokenValue,
        expiry,
        now,
        null
    );

    refreshTokenRepository.save(refreshToken);

    // 2) Also store it in cache for fast validation
    refreshTokenCacheService.storeToken(tokenValue, user.getId(), refreshTokenValiditySeconds);

    return tokenValue;
  }

  private String generateSecureRandomToken() {
    byte[] bytes = new byte[64]; // 64 byte
    secureRandom.nextBytes(bytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }
}
