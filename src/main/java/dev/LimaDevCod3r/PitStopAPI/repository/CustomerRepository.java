package dev.LimaDevCod3r.PitStopAPI.repository;

import dev.LimaDevCod3r.PitStopAPI.model.CustomerModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerModel, Long> {
    // retorna 'true' se já existir um cliente com o CPF informado
    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    Page<CustomerModel> findAll(Pageable pageable);
}
