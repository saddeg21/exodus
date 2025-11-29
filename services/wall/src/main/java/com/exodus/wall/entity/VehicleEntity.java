package com.exodus.wall.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleEntity {
  @Id
  @Column(name = "id", nullable = false, unique = true)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "driver_id", nullable = false)
  private DriverEntity driver;

  @Column(name = "plate_number", nullable = false, unique = true)
  private String plateNumber;

  @Column(name = "brand")
  private String brand;

  @Column(name = "model")
  private String model;

  @Column(name = "color")
  private String color;

  @Column(name = "seats")
  private Integer seats;

  @Column(name = "is_active", nullable = false)
  private boolean active;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;
}
