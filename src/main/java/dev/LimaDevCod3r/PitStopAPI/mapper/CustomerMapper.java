package dev.LimaDevCod3r.PitStopAPI.mapper;

import dev.LimaDevCod3r.PitStopAPI.dto.CustomerRequestDTO;
import dev.LimaDevCod3r.PitStopAPI.dto.CustomerResponseDTO;
import dev.LimaDevCod3r.PitStopAPI.model.CustomerModel;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerModel mapToModel(CustomerRequestDTO source) {
        CustomerModel target = new CustomerModel();
        target.setName(source.getName());
        target.setEmail(source.getEmail());
        target.setCpf(source.getCpf());
        target.setPhone(source.getPhone());
        return target;
    }

    public CustomerRequestDTO mapToDTO(CustomerModel source) {
        CustomerRequestDTO target = new CustomerRequestDTO();
        target.setName(source.getName());
        target.setEmail(source.getEmail());
        target.setCpf(source.getCpf());
        target.setPhone(source.getPhone());
        return target;
    }

    public CustomerResponseDTO mapToResponse(CustomerModel source) {
        CustomerResponseDTO target = new CustomerResponseDTO();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setEmail(source.getEmail());
        target.setCpf(source.getCpf());
        target.setPhone(source.getPhone());
        target.setVehicles(source.getVehicles());
        return target;
    }
}
