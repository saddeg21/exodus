package com.exodus.dome.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
  @Id
  @Column(columnDefinition = "uuid")
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, columnDefinition = "uuid")
  private UserEntity user;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(name = "expires_at", nullable = false)
  private Instant expiresAt;

  @Column(name="created_at", nullable = false)
  private Instant createdAt;

  @Column(name="revoked_at")
  private Instant revokedAt;

  public boolean isExpired() {
    return Instant.now().isAfter(expiresAt);
  }

  public boolean isRevoked() {
    return revokedAt != null;
  }
}
