package dev.LimaDevCod3r.PitStopAPI.repository;

import dev.LimaDevCod3r.PitStopAPI.model.ServiceOrderModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrderModel, Long> {
}
