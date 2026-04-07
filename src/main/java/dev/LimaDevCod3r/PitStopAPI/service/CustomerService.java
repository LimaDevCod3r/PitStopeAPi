package dev.LimaDevCod3r.PitStopAPI.service;

import dev.LimaDevCod3r.PitStopAPI.dto.CustomerDTO;
import dev.LimaDevCod3r.PitStopAPI.dto.CustomerResponseDTO;
import dev.LimaDevCod3r.PitStopAPI.exception.ResourceNotFoundException;
import dev.LimaDevCod3r.PitStopAPI.mapper.CustomerMapper;
import dev.LimaDevCod3r.PitStopAPI.model.CustomerModel;
import dev.LimaDevCod3r.PitStopAPI.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public List<CustomerResponseDTO> getAll() {
        return customerRepository.findAll().stream()
                .map(customerMapper::mapToResponse)
                .toList();
    }

    public CustomerResponseDTO getById(Long id) {
        CustomerModel customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        return customerMapper.mapToResponse(customer);
    }

    public CustomerDTO create(CustomerDTO customerDTO) {
        CustomerModel model = customerMapper.mapToModel(customerDTO);
        CustomerModel persisted = customerRepository.save(model);
        return customerMapper.mapToDTO(persisted);
    }

    public CustomerResponseDTO update(Long id, CustomerDTO customerDTO) {
        CustomerModel customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setCpf(customerDTO.getCpf());
        customerRepository.save(customer);
        return customerMapper.mapToResponse(customer);
    }

    public void deleteById(Long id) {
        customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        customerRepository.deleteById(id);
    }
}
