package dev.LimaDevCod3r.PitStopAPI.Customers;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public List<CustomerResponseDTO> getAll() {
        List<CustomerModel> customers = customerRepository.findAll();
        return customers.stream()
                .map(customerMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public CustomerResponseDTO getById(Long id) {
        Optional<CustomerModel> customerById = customerRepository.findById(id);
        return customerById.map(customerMapper::mapToResponse).orElse(null);
    }

    public CustomerDTO create(CustomerDTO customerDTO) {
        CustomerModel customerModelToPersist = customerMapper.mapToModel(customerDTO);
        CustomerModel persistedCustomerModel = customerRepository.save(customerModelToPersist);
        return customerMapper.mapToDTO(persistedCustomerModel);
    }


    public CustomerResponseDTO update(Long id, CustomerDTO customerDTO) {
        Optional<CustomerModel> customerById = customerRepository.findById(id);
        if (customerById.isEmpty()) {
            return null;
        }
        CustomerModel customerModelToUpdate = customerById.get();
        customerModelToUpdate.setName(customerDTO.getName());
        customerModelToUpdate.setEmail(customerDTO.getEmail());
        customerModelToUpdate.setPhone(customerDTO.getPhone());
        customerModelToUpdate.setCpf(customerDTO.getCpf());
        customerRepository.save(customerModelToUpdate);

        return customerMapper.mapToResponse(customerModelToUpdate);
    }

    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }
}
