package dev.LimaDevCod3r.PitStopAPI.repository;

import dev.LimaDevCod3r.PitStopAPI.model.VehicleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<VehicleModel, Long> {
    boolean existsByPlate(String plate);
    Page<VehicleModel> findAllByCustomerActiveTrue(Pageable pageable);
}
