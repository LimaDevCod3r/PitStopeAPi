package dev.LimaDevCod3r.PitStopAPI.repository;

import dev.LimaDevCod3r.PitStopAPI.model.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerModel, Long> {
}
