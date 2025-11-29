package com.exodus.wall.repository;


import com.exodus.wall.entity.VehicleEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<VehicleEntity, UUID> {
  List<VehicleEntity> findByDriverAndActiveTrue(UUID driverId);
}
