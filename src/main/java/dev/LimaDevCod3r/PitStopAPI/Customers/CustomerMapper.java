package dev.LimaDevCod3r.PitStopAPI.Customers;

import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerModel map(CustomerDTO customerDTO) {
        CustomerModel customerModel = new CustomerModel();
        customerModel.setId(customerDTO.getId());
        customerModel.setName(customerDTO.getName());
        customerModel.setEmail(customerDTO.getEmail());
        customerModel.setCpf(customerDTO.getCpf());
        customerModel.setPhone(customerDTO.getPhone());

        return customerModel;
    }

    public CustomerDTO map(CustomerModel customerModel){
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customerModel.getId());
        customerDTO.setName(customerModel.getName());
        customerDTO.setEmail(customerModel.getEmail());
        customerDTO.setCpf(customerModel.getCpf());
        customerDTO.setPhone(customerModel.getPhone());
        return customerDTO;
    }
}
