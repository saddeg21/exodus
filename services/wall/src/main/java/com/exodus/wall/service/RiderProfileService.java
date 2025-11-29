package com.exodus.wall.service;

import com.exodus.wall.entity.RiderEntity;
import com.exodus.wall.entity.dto.RiderProfileResponse;
import com.exodus.wall.entity.dto.RiderProfileUpdateRequest;
import com.exodus.wall.repository.RiderRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RiderProfileService {
  private final RiderRepository riderRepository;

  @Autowired
  public RiderProfileService(RiderRepository riderRepository) {
    this.riderRepository = riderRepository;
  }

  @Transactional(readOnly = true)
  public RiderProfileResponse getMyProfile(UUID authUserId) {
    RiderEntity rider = riderRepository.findByAuthUserId(authUserId)
        .orElseThrow(() -> new RuntimeException("Rider not found")); // This is temporary, until we have proper exception handling

    RiderProfileResponse response = new RiderProfileResponse();
    response.setId(rider.getId());
    response.setFullName(rider.getFullName());
    response.setPhoneNumber(rider.getPhoneNumber());
    response.setRatingAverage(rider.getRatingAverage());
    response.setCreatedAt(rider.getCreatedAt());

    return response;
  }

  @Transactional
  public RiderProfileResponse updateMyProfile(UUID authUserId, RiderProfileUpdateRequest updateRequest) {
    RiderEntity rider = riderRepository.findByAuthUserId(authUserId)
        .orElseThrow(() -> new RuntimeException("Rider not found")); // This is temporary, until we have proper exception handling

    rider.setFullName(updateRequest.getFullName());
    rider.setPhoneNumber(updateRequest.getPhoneNumber());
    // Note: We typically wouldn't allow updating ratingAverage or createdAt through profile update

    RiderEntity updatedRider = riderRepository.save(rider);

    RiderProfileResponse response = new RiderProfileResponse();
    response.setId(updatedRider.getId());
    response.setFullName(updatedRider.getFullName());
    response.setPhoneNumber(updatedRider.getPhoneNumber());
    response.setRatingAverage(updatedRider.getRatingAverage());
    response.setCreatedAt(updatedRider.getCreatedAt());

    return response;
  }
}
