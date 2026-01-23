package dev.LimaDevCod3r.PitStopAPI.Customers;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public List<CustomerModel> getAll() {
        return customerRepository.findAll();
    }

    public CustomerModel getById(Long id) {
        Optional<CustomerModel> customerById = customerRepository.findById(id);
        return customerById.orElse(null);
    }

    public CustomerDTO create(CustomerDTO customerDTO) {
        CustomerModel customerModelToPersist = customerMapper.map(customerDTO);
        CustomerModel persistedCustomerModel = customerRepository.save(customerModelToPersist);
        return customerMapper.map(persistedCustomerModel);
    }


    public CustomerModel update(Long id, CustomerModel customerModel) {
        Optional<CustomerModel> customerById = customerRepository.findById(id);
        if(customerById.isEmpty()){
            return null;
        }
        CustomerModel customerModelToUpdate = customerById.get();
        customerModelToUpdate.setName(customerModel.getName());
        customerModelToUpdate.setEmail(customerModel.getEmail());
        customerModelToUpdate.setPhone(customerModel.getPhone());
        customerModelToUpdate.setCpf(customerModel.getCpf());
        return customerRepository.save(customerModelToUpdate);
    }

    public void deleteById(Long id){
        customerRepository.deleteById(id);
    }
}
