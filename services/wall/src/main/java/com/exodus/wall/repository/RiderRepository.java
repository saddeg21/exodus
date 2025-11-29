package com.exodus.wall.repository;

import com.exodus.wall.entity.RiderEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderRepository extends JpaRepository<RiderEntity, UUID> {
  Optional<RiderEntity> findByAuthUserId(UUID authUserId);
}
