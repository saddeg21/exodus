package com.exodus.wall.repository;

import com.exodus.wall.entity.DriverEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<DriverEntity, UUID> {
  Optional<DriverEntity> findByAuthUserId(UUID authUserId);
}
