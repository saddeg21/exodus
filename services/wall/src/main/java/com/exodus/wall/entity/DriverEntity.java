package com.exodus.wall.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverEntity {
  @Id
  @Column(name = "id", nullable = false, unique = true)
  private UUID id;

  @Column(name = "auth_user_id", nullable = false, unique = true)
  private UUID authUserId;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @Column(name = "phone_number", nullable = false, unique = true)
  private String phoneNumber;

  @Column(name = "rating_avg")
  private Double ratingAverage;

  @Column(name = "is_verified", nullable = false)
  private boolean verified;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;
}
