package dev.LimaDevCod3r.PitStopAPI.service;

import dev.LimaDevCod3r.PitStopAPI.dto.CustomerRequestDTO;
import dev.LimaDevCod3r.PitStopAPI.dto.CustomerResponseDTO;
import dev.LimaDevCod3r.PitStopAPI.exception.InactiveResourceException;
import dev.LimaDevCod3r.PitStopAPI.exception.ResourceAlreadyExistsException;
import dev.LimaDevCod3r.PitStopAPI.exception.ResourceNotFoundException;
import dev.LimaDevCod3r.PitStopAPI.mapper.CustomerMapper;
import dev.LimaDevCod3r.PitStopAPI.model.CustomerModel;
import dev.LimaDevCod3r.PitStopAPI.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public Page<CustomerResponseDTO> getAll(Pageable pageable) {
        return customerRepository.findAllByActiveTrue(pageable)
                .map(customerMapper::mapToResponse);
    }

    public CustomerResponseDTO getById(Long id) {
        CustomerModel customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));

        if (!customer.getActive()) {
            throw new InactiveResourceException("Customer", id);
        }

        return customerMapper.mapToResponse(customer);
    }

    public CustomerRequestDTO create(CustomerRequestDTO customerRequestDTO) {
        // Valida se o CPF já existe para evitar duplicidade.
        if (customerRepository.existsByCpf(customerRequestDTO.getCpf())) {
            throw new ResourceAlreadyExistsException("Já existe um cadastro com o CPF informado.");
        }

        // Valida se o E-mail já existe no banco.
        if (customerRepository.existsByEmail(customerRequestDTO.getEmail())) {
            throw new ResourceAlreadyExistsException("Este endereço de e-mail já está vinculado a uma conta cadastrada.");
        }

        // Converte o DTO para Model
        CustomerModel model = customerMapper.mapToModel(customerRequestDTO);

        // Salva no banco e retorna o resultado convertido de volta para DTO.
        CustomerModel persisted = customerRepository.save(model);
        return customerMapper.mapToDTO(persisted);
    }

    public CustomerResponseDTO update(Long id, CustomerRequestDTO customerRequestDTO) {
        // Busca o cliente pelo ID ou lança erro 404 se não encontrar.
        CustomerModel customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));


        // Bloqueia operação se cliente inativo
        if (!customer.getActive()) {
            throw new InactiveResourceException("Customer", id);
        }

        //  Se o CPF mudou, verifica se o novo CPF já pertence a outro usuário.
        if (customerRequestDTO.getCpf() != null && !customerRequestDTO.getCpf().equals(customer.getCpf())) {
            if (customerRepository.existsByCpf(customerRequestDTO.getCpf())) {
                throw new ResourceAlreadyExistsException("CPF já registrado");
            }
            customer.setCpf(customerRequestDTO.getCpf());
        }

        // Se o E-mail mudou, verifica se o novo e-mail já está em uso.
        if (customerRequestDTO.getEmail() != null && !customerRequestDTO.getEmail().equals(customer.getEmail())) {
            if (customerRepository.existsByEmail(customerRequestDTO.getEmail())) {
                throw new ResourceAlreadyExistsException("Email já registrado");
            }
            customer.setEmail(customerRequestDTO.getEmail());
        }

        // Atualiza os demais campos (Nome e Telefone)
        customer.setName(customerRequestDTO.getName());
        customer.setPhone(customerRequestDTO.getPhone());

        // Salva as alterações e retorna o cliente atualizado.
        customerRepository.save(customer);
        return customerMapper.mapToResponse(customer);
    }

    public void deleteById(Long id) {
        CustomerModel customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        customer.deactivate();
        customerRepository.save(customer);
    }
}
