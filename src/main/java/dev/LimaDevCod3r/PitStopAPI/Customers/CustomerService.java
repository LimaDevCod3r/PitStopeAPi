package dev.LimaDevCod3r.PitStopAPI.Customers;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerModel> getAll() {
        return customerRepository.findAll();
    }

    public CustomerModel getById(Long id) {
        Optional<CustomerModel> customerById = customerRepository.findById(id);
        return customerById.orElse(null);
    }

    public CustomerModel create(CustomerModel customerModel) {
        return customerRepository.save(customerModel);
    }
}
