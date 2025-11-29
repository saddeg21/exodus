package com.exodus.wall.entity.dto;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverProfileResponse {
  private UUID id;
  private String fullName;
  private String phoneNumber;
  private Double ratingAverage;
  private boolean verified;
  private OffsetDateTime createdAt;

}
