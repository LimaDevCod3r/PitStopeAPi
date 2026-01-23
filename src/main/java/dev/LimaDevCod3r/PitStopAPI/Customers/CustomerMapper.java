package dev.LimaDevCod3r.PitStopAPI.Customers;

import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerModel mapToModel(CustomerDTO source) {
        CustomerModel target = new CustomerModel();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setEmail(source.getEmail());
        target.setCpf(source.getCpf());
        target.setPhone(source.getPhone());
        return target;
    }

    public CustomerDTO mapToDTO(CustomerModel source) {
        CustomerDTO target = new CustomerDTO();
        target.setId(source.getId());
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
        target.setVehicleModel(source.getVehicles());
        return target;
    }
}
