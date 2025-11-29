package com.exodus.wall.service;

import com.exodus.wall.entity.dto.DriverProfileResponse;
import com.exodus.wall.entity.dto.DriverProfileUpdateRequest;
import com.exodus.wall.entity.DriverEntity;
import com.exodus.wall.repository.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DriverProfileService {

  private final DriverRepository driverRepository;

  public DriverProfileService(DriverRepository driverRepository) {
    this.driverRepository = driverRepository;
  }

  @Transactional(readOnly = true)
  public DriverProfileResponse getMyProfile(UUID authUserId) {
    DriverEntity driver = driverRepository.findByAuthUserId(authUserId)
        .orElseThrow(() -> new RuntimeException("Driver profile not found"));

    DriverProfileResponse resp = new DriverProfileResponse();
    resp.setId(driver.getId());
    resp.setFullName(driver.getFullName());
    resp.setPhoneNumber(driver.getPhoneNumber());
    resp.setRatingAverage(driver.getRatingAverage());
    resp.setVerified(driver.isVerified());
    resp.setCreatedAt(driver.getCreatedAt());
    return resp;
  }

  @Transactional
  public DriverProfileResponse updateMyProfile(UUID authUserId, DriverProfileUpdateRequest request) {
    DriverEntity driver = driverRepository.findByAuthUserId(authUserId)
        .orElseThrow(() -> new RuntimeException("Driver profile not found"));

    driver.setFullName(request.getFullName());
    driver.setPhoneNumber(request.getPhoneNumber());

    DriverEntity saved = driverRepository.save(driver);

    DriverProfileResponse resp = new DriverProfileResponse();
    resp.setId(saved.getId());
    resp.setFullName(saved.getFullName());
    resp.setPhoneNumber(saved.getPhoneNumber());
    resp.setRatingAverage(saved.getRatingAverage());
    resp.setVerified(saved.isVerified());
    resp.setCreatedAt(saved.getCreatedAt());
    return resp;
  }
}
