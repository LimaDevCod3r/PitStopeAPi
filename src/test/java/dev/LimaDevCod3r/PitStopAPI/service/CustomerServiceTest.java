package dev.LimaDevCod3r.PitStopAPI.service;

import dev.LimaDevCod3r.PitStopAPI.dto.CustomerRequestDTO;
import dev.LimaDevCod3r.PitStopAPI.dto.CustomerResponseDTO;
import dev.LimaDevCod3r.PitStopAPI.exception.InactiveResourceException;
import dev.LimaDevCod3r.PitStopAPI.exception.ResourceAlreadyExistsException;
import dev.LimaDevCod3r.PitStopAPI.exception.ResourceNotFoundException;
import dev.LimaDevCod3r.PitStopAPI.mapper.CustomerMapper;
import dev.LimaDevCod3r.PitStopAPI.model.CustomerModel;
import dev.LimaDevCod3r.PitStopAPI.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Spy
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    private CustomerRequestDTO requestDTO;
    private CustomerModel customerModel;

    @BeforeEach
    void setUp() {
        requestDTO = new CustomerRequestDTO();
        requestDTO.setName("João Silva");
        requestDTO.setCpf("12345678901");
        requestDTO.setEmail("joao@email.com");
        requestDTO.setPhone("11999999999");

        customerModel = new CustomerModel();
        customerModel.setId(1L);
        customerModel.setName("João Silva");
        customerModel.setCpf("12345678901");
        customerModel.setEmail("joao@email.com");
        customerModel.setPhone("11999999999");
        customerModel.setActive(true);
    }

    @Nested
    @DisplayName("findAll - paginação")
    class FindAll {

        @Test
        @DisplayName("Retorna página de clientes quando existem registros")
        void shouldReturnPageOfCustomers() {
            // Setup: Define uma página fake com 1 registro para simular o banco.
            Pageable pageable = PageRequest.of(0, 10);
            Page<CustomerModel> page = new PageImpl<>(List.of(customerModel), pageable, 1);

            // Configura o repositório para retornar a página fake apenas de clientes ativos.
            when(customerRepository.findAllByActiveTrue(pageable)).thenReturn(page);

            // Chama o método do Service que será testado.
            Page<CustomerResponseDTO> result = customerService.getAll(pageable);

            // Garante que o resultado não é vazio, tem o tamanho certo e os dados batem.
            assertThat(result).isNotEmpty();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().getFirst().getName()).isEqualTo("João Silva");

            //  Confirma que o repositório foi chamado com o método de ativos.
            verify(customerRepository).findAllByActiveTrue(pageable);
        }

        @Test
        @DisplayName("Retorna página vazia quando não existem registros")
        void shouldReturnEmptyPage() {
            // Cria uma página vazia
            Pageable pageable = PageRequest.of(0, 10);
            Page<CustomerModel> page = Page.empty(pageable);

            // Configura o repositório para devolver essa página vazia de clientes ativos
            when(customerRepository.findAllByActiveTrue(pageable)).thenReturn(page);

            Page<CustomerResponseDTO> result = customerService.getAll(pageable);


            // Garante que o resultado da lista está realmente vazio.
            assertThat(result.getContent()).isEmpty();

            verify(customerRepository).findAllByActiveTrue(pageable);
        }
    }

    @Nested
    @DisplayName("create - cadastro de cliente")
    class Create {

        @Test
        @DisplayName("Deve criar cliente com sucesso")
        void shouldCreateCustomer() {
            // Simula que o CPF e o E-mail NÃO existem no banco
            when(customerRepository.existsByCpf(anyString())).thenReturn(false);
            when(customerRepository.existsByEmail(anyString())).thenReturn(false);
            when(customerRepository.save(any(CustomerModel.class))).thenAnswer(invocation -> {
                CustomerModel model = invocation.getArgument(0);
                model.setId(1L);
                return model;
            });

            CustomerRequestDTO result = customerService.create(requestDTO);

            // Garante que os dados retornados batem com o que foi enviado.
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("João Silva");
            assertThat(result.getCpf()).isEqualTo("12345678901");
            verify(customerRepository).save(any(CustomerModel.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando CPF já existe")
        void shouldThrowWhenCpfAlreadyExists() {
            //  Simula que o CPF já existe no banco de dados.
            when(customerRepository.existsByCpf(anyString())).thenReturn(true);

            // Tenta criar o cliente e espera que  a exceção ResourceAlreadyExistsException seja lançada.
            assertThatThrownBy(() -> customerService.create(requestDTO))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessageContaining("CPF");

            // Garante que o método save NUNCA foi chamado
            verify(customerRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando E-mail já existe")
        void shouldThrowWhenEmailAlreadyExists() {
            // CPF não existe (passa na 1ª validação), mas o E-mail já existe (falha na 2ª)
            when(customerRepository.existsByCpf(anyString())).thenReturn(false);
            when(customerRepository.existsByEmail(anyString())).thenReturn(true);

            assertThatThrownBy(() -> customerService.create(requestDTO))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessageContaining("e-mail");

            verify(customerRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("update - atualização de cliente")
    class Update {

        @Test
        @DisplayName("Deve atualizar nome e telefone sem alterar CPF/email")
        void shouldUpdateNameAndPhone() {
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customerModel));
            when(customerRepository.save(any(CustomerModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

            CustomerRequestDTO updateDTO = new CustomerRequestDTO();
            updateDTO.setName("João Atualizado");
            updateDTO.setCpf("12345678901");
            updateDTO.setEmail("joao@email.com");
            updateDTO.setPhone("11888888888");

            CustomerResponseDTO result = customerService.update(1L, updateDTO);

            assertThat(result.getName()).isEqualTo("João Atualizado");
            assertThat(result.getPhone()).isEqualTo("11888888888");
            verify(customerRepository).save(any(CustomerModel.class));
        }

        @Test
        @DisplayName("Deve atualizar CPF quando mudou e o novo CPF não existe")
        void shouldUpdateCpfWhenNewCpfIsNotTaken() {
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customerModel));
            when(customerRepository.existsByCpf("98765432109")).thenReturn(false);
            when(customerRepository.save(any(CustomerModel.class))).thenAnswer(invocation -> {
                CustomerModel model = invocation.getArgument(0);
                model.setId(1L);
                return model;
            });

            CustomerRequestDTO updateDTO = new CustomerRequestDTO();
            updateDTO.setName(customerModel.getName());
            updateDTO.setCpf("98765432109");
            updateDTO.setEmail(customerModel.getEmail());
            updateDTO.setPhone(customerModel.getPhone());

            customerService.update(1L, updateDTO);

            verify(customerRepository).save(any(CustomerModel.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar alterar CPF para um já existente")
        void shouldThrowWhenNewCpfAlreadyExists() {
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customerModel));
            when(customerRepository.existsByCpf("98765432109")).thenReturn(true);

            CustomerRequestDTO updateDTO = new CustomerRequestDTO();
            updateDTO.setName(customerModel.getName());
            updateDTO.setCpf("98765432109");
            updateDTO.setEmail(customerModel.getEmail());
            updateDTO.setPhone(customerModel.getPhone());

            assertThatThrownBy(() -> customerService.update(1L, updateDTO))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessageContaining("CPF");

            verify(customerRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve atualizar Email quando mudou e o novo Email não existe")
        void shouldUpdateEmailWhenNewEmailIsNotTaken() {
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customerModel));
            when(customerRepository.existsByEmail("novo@email.com")).thenReturn(false);
            when(customerRepository.save(any(CustomerModel.class))).thenAnswer(invocation -> {
                CustomerModel model = invocation.getArgument(0);
                model.setId(1L);
                return model;
            });

            CustomerRequestDTO updateDTO = new CustomerRequestDTO();
            updateDTO.setName(customerModel.getName());
            updateDTO.setCpf(customerModel.getCpf());
            updateDTO.setEmail("novo@email.com");
            updateDTO.setPhone(customerModel.getPhone());

            customerService.update(1L, updateDTO);

            verify(customerRepository).existsByEmail("novo@email.com");
            verify(customerRepository).save(any(CustomerModel.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar alterar email para um já existente")
        void shouldThrowWhenNewEmailAlreadyExists() {
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customerModel));
            when(customerRepository.existsByEmail("outro@email.com")).thenReturn(true);

            CustomerRequestDTO updateDTO = new CustomerRequestDTO();
            updateDTO.setName(customerModel.getName());
            updateDTO.setCpf(customerModel.getCpf());
            updateDTO.setEmail("outro@email.com");
            updateDTO.setPhone(customerModel.getPhone());

            assertThatThrownBy(() -> customerService.update(1L, updateDTO))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessageContaining("Email");

            verify(customerRepository, never()).save(any());
        }

        @Test
        @DisplayName("Não deve validar CPF/email quando mantiver os mesmos valores")
        void shouldNotValidateIfCpfAndEmailUnchanged() {
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customerModel));
            when(customerRepository.save(any(CustomerModel.class))).thenReturn(customerModel);

            CustomerRequestDTO updateDTO = new CustomerRequestDTO();
            updateDTO.setName(customerModel.getName());
            updateDTO.setCpf(customerModel.getCpf());
            updateDTO.setEmail(customerModel.getEmail());
            updateDTO.setPhone(customerModel.getPhone());

            customerService.update(1L, updateDTO);

            verify(customerRepository, never()).existsByCpf(anyString());
            verify(customerRepository, never()).existsByEmail(anyString());
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException se cliente não existe")
        void shouldThrowWhenCustomerNotFound() {
            when(customerRepository.findById(1L)).thenReturn(Optional.empty());

            CustomerRequestDTO updateDTO = new CustomerRequestDTO();
            updateDTO.setName("Test");
            updateDTO.setCpf("12345678901");
            updateDTO.setEmail("test@email.com");
            updateDTO.setPhone("11999999999");

            assertThatThrownBy(() -> customerService.update(1L, updateDTO))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("não encontrado");
        }

        @Test
        @DisplayName("Deve lançar InactiveResourceException se cliente inativo")
        void shouldThrowWhenCustomerIsInactive() {
            CustomerModel inactiveModel = new CustomerModel();
            inactiveModel.setId(1L);
            inactiveModel.setName("João Desativado");
            inactiveModel.setCpf("12345678901");
            inactiveModel.setEmail("joao@email.com");
            inactiveModel.setPhone("11999999999");
            inactiveModel.setActive(false);

            when(customerRepository.findById(1L)).thenReturn(Optional.of(inactiveModel));

            CustomerRequestDTO updateDTO = new CustomerRequestDTO();
            updateDTO.setName("João Atualizado");
            updateDTO.setCpf("12345678901");
            updateDTO.setEmail("joao@email.com");
            updateDTO.setPhone("11888888888");

            assertThatThrownBy(() -> customerService.update(1L, updateDTO))
                    .isInstanceOf(InactiveResourceException.class)
                    .hasMessageContaining("inativo");
        }
    }

    @Nested
    @DisplayName("getById - busca por ID")
    class GetById {

        @Test
        @DisplayName("Deve retornar cliente quando ativo")
        void shouldReturnCustomerWhenActive() {
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customerModel));

            CustomerResponseDTO result = customerService.getById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("João Silva");
        }

        @Test
        @DisplayName("Deve lançar InactiveResourceException quando cliente inativo")
        void shouldThrowWhenCustomerIsInactive() {
            CustomerModel inactiveModel = new CustomerModel();
            inactiveModel.setId(1L);
            inactiveModel.setName("João Desativado");
            inactiveModel.setCpf("11122233344");
            inactiveModel.setEmail("inactive@email.com");
            inactiveModel.setPhone("11999999999");
            inactiveModel.setActive(false);

            when(customerRepository.findById(1L)).thenReturn(Optional.of(inactiveModel));

            assertThatThrownBy(() -> customerService.getById(1L))
                    .isInstanceOf(InactiveResourceException.class)
                    .hasMessageContaining("inativo");
        }
    }

    @Nested
    @DisplayName("deleteById - soft delete")
    class DeleteById {

        @Test
        @DisplayName("Deve desativar cliente com sucesso")
        void shouldDeactivateCustomer() {
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customerModel));
            when(customerRepository.save(any(CustomerModel.class))).thenReturn(customerModel);

            customerService.deleteById(1L);

            assertThat(customerModel.getActive()).isFalse();
            verify(customerRepository).save(customerModel);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException se cliente não existe")
        void shouldThrowWhenCustomerNotFound() {
            when(customerRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> customerService.deleteById(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("não encontrado");
        }
    }
}
