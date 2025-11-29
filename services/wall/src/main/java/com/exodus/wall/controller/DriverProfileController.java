package com.exodus.wall.controller;

import com.exodus.wall.entity.dto.DriverProfileResponse;
import com.exodus.wall.entity.dto.DriverProfileUpdateRequest;
import com.exodus.wall.service.DriverProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/profile/driver")
public class DriverProfileController {

  private final DriverProfileService driverProfileService;

  public DriverProfileController(DriverProfileService driverProfileService) {
    this.driverProfileService = driverProfileService;
  }

  // GET /api/profile/driver/me
  @GetMapping("/me")
  public ResponseEntity<DriverProfileResponse> getMyProfile(
      @RequestHeader("X-User-Id") UUID authUserId
  ) {
    DriverProfileResponse response = driverProfileService.getMyProfile(authUserId);
    return ResponseEntity.ok(response);
  }

  // PUT /api/profile/driver/me
  @PutMapping("/me")
  public ResponseEntity<DriverProfileResponse> updateMyProfile(
      @RequestHeader("X-User-Id") UUID authUserId,
      @Valid @RequestBody DriverProfileUpdateRequest request
  ) {
    DriverProfileResponse response = driverProfileService.updateMyProfile(authUserId, request);
    return ResponseEntity.ok(response);
  }
}
