package com.exodus.dome.service;

import com.exodus.dome.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private final SecretKey secretKey;
  private final String issuer;
  private final long accessTokenValiditySeconds;

  public JwtService(
      @Value("${auth.jwt.secret}") String secret,
      @Value("${auth.jwt.issuer}") String issuer,
      @Value("${auth.jwt.access-token-validity-seconds}") long accessTokenValiditySeconds
  ) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    this.issuer = issuer;
    this.accessTokenValiditySeconds = accessTokenValiditySeconds;
  }

  public String generateAccessToken(UserEntity user) {
    Instant now = Instant.now();
    Instant expiry = now.plusSeconds(accessTokenValiditySeconds);

    Map<String, Object> claims = new HashMap<>();
    claims.put("role", user.getRole().name());

    return Jwts.builder()
        .setSubject(user.getId().toString())
        .setIssuer(issuer)
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(expiry))
        .addClaims(claims)
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public Jws<Claims> validate(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .requireIssuer(issuer)
          .build()
          .parseClaimsJws(token);
    } catch (JwtException e) {
      throw new IllegalArgumentException("Invalid JWT", e);
    }
  }
}
