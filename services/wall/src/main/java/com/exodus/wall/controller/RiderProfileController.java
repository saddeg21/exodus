package com.exodus.wall.controller;

import com.exodus.wall.entity.dto.RiderProfileResponse;
import com.exodus.wall.entity.dto.RiderProfileUpdateRequest;
import com.exodus.wall.service.RiderProfileService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rider/profile")
public class RiderProfileController {
  private final RiderProfileService riderProfileService;

  @Autowired
  public RiderProfileController(RiderProfileService riderProfileService) {
    this.riderProfileService = riderProfileService;
  }

  @GetMapping("/me")
  public ResponseEntity<RiderProfileResponse> getMyProfile(@RequestHeader("X-User-Id")
                                                           UUID authUserId) {
    RiderProfileResponse profile = riderProfileService.getMyProfile(authUserId);
    return ResponseEntity.ok(profile);
  }

  @PutMapping("/me")
  public ResponseEntity<RiderProfileResponse> updateMyProfile(
      @RequestHeader("X-User-Id") UUID authUserId,
      @Valid @RequestBody RiderProfileUpdateRequest request
  ) {
    RiderProfileResponse response = riderProfileService.updateMyProfile(authUserId, request);
    return ResponseEntity.ok(response);
  }
}
