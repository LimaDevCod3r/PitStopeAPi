package dev.LimaDevCod3r.PitStopAPI.Vehicles;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<VehicleModel, Long> {
    boolean existsByPlate(String plate);
}
