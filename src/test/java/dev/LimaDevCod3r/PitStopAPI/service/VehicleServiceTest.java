package dev.LimaDevCod3r.PitStopAPI.service;

import dev.LimaDevCod3r.PitStopAPI.dto.VehicleRequestDTO;
import dev.LimaDevCod3r.PitStopAPI.dto.VehicleResponseDTO;
import dev.LimaDevCod3r.PitStopAPI.exception.DuplicateResourceException;
import dev.LimaDevCod3r.PitStopAPI.exception.InactiveResourceException;
import dev.LimaDevCod3r.PitStopAPI.exception.ResourceNotFoundException;
import dev.LimaDevCod3r.PitStopAPI.mapper.VehicleMapper;
import dev.LimaDevCod3r.PitStopAPI.model.CustomerModel;
import dev.LimaDevCod3r.PitStopAPI.model.VehicleModel;
import dev.LimaDevCod3r.PitStopAPI.repository.CustomerRepository;
import dev.LimaDevCod3r.PitStopAPI.repository.VehicleRepository;
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
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Spy
    private VehicleMapper vehicleMapper;

    @InjectMocks
    private VehicleService vehicleService;

    private VehicleRequestDTO requestDTO;
    private VehicleModel vehicleModel;
    private CustomerModel activeCustomer;
    private CustomerModel inactiveCustomer;

    @BeforeEach
    void setUp() {
        activeCustomer = new CustomerModel();
        activeCustomer.setId(1L);
        activeCustomer.setName("Joao Silva");
        activeCustomer.setCpf("12345678901");
        activeCustomer.setEmail("joao@email.com");
        activeCustomer.setPhone("11999999999");
        activeCustomer.setActive(true);

        inactiveCustomer = new CustomerModel();
        inactiveCustomer.setId(1L);
        inactiveCustomer.setName("Joao Desativado");
        inactiveCustomer.setCpf("12345678901");
        inactiveCustomer.setEmail("joao@email.com");
        inactiveCustomer.setPhone("11999999999");
        inactiveCustomer.setActive(false);

        requestDTO = new VehicleRequestDTO();
        requestDTO.setBrand("Honda");
        requestDTO.setModel("Civic");
        requestDTO.setYear("2024");
        requestDTO.setPlate("ABC1234");
        requestDTO.setColor("Prata");
        requestDTO.setCustomerId(1L);

        vehicleModel = new VehicleModel();
        vehicleModel.setId(1L);
        vehicleModel.setBrand("Honda");
        vehicleModel.setModel("Civic");
        vehicleModel.setYear("2024");
        vehicleModel.setPlate("ABC1234");
        vehicleModel.setColor("Prata");
        vehicleModel.setCustomer(activeCustomer);
    }

    @Nested
    @DisplayName("getAll - paginação com cliente ativo")
    class GetAll {

        @Test
        @DisplayName("Deve retornar página de veículos de clientes ativos")
        void shouldReturnPageOfVehiclesFromActiveCustomers() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<VehicleModel> page = new PageImpl<>(List.of(vehicleModel), pageable, 1);

            when(vehicleRepository.findAllByCustomerActiveTrue(pageable)).thenReturn(page);

            Page<VehicleResponseDTO> result = vehicleService.getAll(pageable);

            assertThat(result).isNotEmpty();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().getFirst().getBrand()).isEqualTo("Honda");
            verify(vehicleRepository).findAllByCustomerActiveTrue(pageable);
        }

        @Test
        @DisplayName("Deve retornar página vazia quando não há veículos de clientes ativos")
        void shouldReturnEmptyPageWhenNoActiveVehicles() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<VehicleModel> page = Page.empty(pageable);

            when(vehicleRepository.findAllByCustomerActiveTrue(pageable)).thenReturn(page);

            Page<VehicleResponseDTO> result = vehicleService.getAll(pageable);

            assertThat(result.getContent()).isEmpty();
            verify(vehicleRepository).findAllByCustomerActiveTrue(pageable);
        }
    }

    @Nested
    @DisplayName("getById - busca por ID")
    class GetById {

        @Test
        @DisplayName("Deve retornar veículo quando cliente está ativo")
        void shouldReturnVehicleWhenCustomerIsActive() {
            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicleModel));

            VehicleResponseDTO result = vehicleService.getById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getBrand()).isEqualTo("Honda");
            assertThat(result.getPlate()).isEqualTo("ABC1234");
        }

        @Test
        @DisplayName("Deve lançar InactiveResourceException quando cliente está inativo")
        void shouldThrowWhenCustomerIsInactive() {
            vehicleModel.setCustomer(inactiveCustomer);
            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicleModel));

            assertThatThrownBy(() -> vehicleService.getById(1L))
                    .isInstanceOf(InactiveResourceException.class)
                    .hasMessageContaining("inativo");
        }

        @Test
        @DisplayName("Deve lançar InactiveResourceException quando veículo não tem cliente")
        void shouldThrowWhenVehicleHasNoCustomer() {
            vehicleModel.setCustomer(null);
            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicleModel));

            assertThatThrownBy(() -> vehicleService.getById(1L))
                    .isInstanceOf(InactiveResourceException.class);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando veículo não existe")
        void shouldThrowWhenVehicleNotFound() {
            when(vehicleRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> vehicleService.getById(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Vehicle");
        }
    }

    @Nested
    @DisplayName("create - cadastro de veículo")
    class Create {

        @Test
        @DisplayName("Deve criar veículo com sucesso")
        void shouldCreateVehicle() {
            when(customerRepository.findById(1L)).thenReturn(Optional.of(activeCustomer));
            when(vehicleRepository.existsByPlate("ABC1234")).thenReturn(false);
            when(vehicleRepository.save(any(VehicleModel.class))).thenAnswer(invocation -> {
                VehicleModel model = invocation.getArgument(0);
                model.setId(1L);
                return model;
            });

            VehicleRequestDTO result = vehicleService.create(requestDTO);

            assertThat(result).isNotNull();
            assertThat(result.getBrand()).isEqualTo("Honda");
            assertThat(result.getPlate()).isEqualTo("ABC1234");
            verify(vehicleRepository).save(any(VehicleModel.class));
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando cliente não existe")
        void shouldThrowWhenCustomerNotFound() {
            when(customerRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> vehicleService.create(requestDTO))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Customer");
        }

        @Test
        @DisplayName("Deve lançar InactiveResourceException quando cliente está inativo")
        void shouldThrowWhenCustomerIsInactive() {
            when(customerRepository.findById(1L)).thenReturn(Optional.of(inactiveCustomer));

            assertThatThrownBy(() -> vehicleService.create(requestDTO))
                    .isInstanceOf(InactiveResourceException.class)
                    .hasMessageContaining("inativo");
        }

        @Test
        @DisplayName("Deve lançar DuplicateResourceException quando placa já existe")
        void shouldThrowWhenPlateAlreadyExists() {
            when(customerRepository.findById(1L)).thenReturn(Optional.of(activeCustomer));
            when(vehicleRepository.existsByPlate("ABC1234")).thenReturn(true);

            assertThatThrownBy(() -> vehicleService.create(requestDTO))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("Plate");

            verify(vehicleRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("update - atualização de veículo")
    class Update {

        @Test
        @DisplayName("Deve atualizar veículo com sucesso")
        void shouldUpdateVehicle() {
            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicleModel));
            when(vehicleRepository.save(any(VehicleModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

            VehicleRequestDTO updateDTO = new VehicleRequestDTO();
            updateDTO.setBrand("Toyota");
            updateDTO.setModel("Corolla");
            updateDTO.setYear("2025");
            updateDTO.setPlate("ABC1234");
            updateDTO.setColor("Preto");
            updateDTO.setCustomerId(1L);

            VehicleResponseDTO result = vehicleService.update(1L, updateDTO);

            assertThat(result.getBrand()).isEqualTo("Toyota");
            assertThat(result.getPlate()).isEqualTo("ABC1234");
            verify(vehicleRepository).save(any(VehicleModel.class));
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando veículo não existe")
        void shouldThrowWhenVehicleNotFound() {
            when(vehicleRepository.findById(999L)).thenReturn(Optional.empty());

            VehicleRequestDTO updateDTO = new VehicleRequestDTO();

            assertThatThrownBy(() -> vehicleService.update(999L, updateDTO))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Vehicle");
        }

        @Test
        @DisplayName("Deve atualizar cliente quando mudou e novo cliente está ativo")
        void shouldUpdateCustomerWhenChangedAndActive() {
            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicleModel));
            when(vehicleRepository.save(any(VehicleModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

            CustomerModel newCustomer = new CustomerModel();
            newCustomer.setId(2L);
            newCustomer.setName("Maria Santos");
            newCustomer.setCpf("98765432100");
            newCustomer.setEmail("maria@email.com");
            newCustomer.setPhone("11888888888");
            newCustomer.setActive(true);

            VehicleRequestDTO updateDTO = new VehicleRequestDTO();
            updateDTO.setBrand(vehicleModel.getBrand());
            updateDTO.setModel(vehicleModel.getModel());
            updateDTO.setYear(vehicleModel.getYear());
            updateDTO.setPlate(vehicleModel.getPlate());
            updateDTO.setColor(vehicleModel.getColor());
            updateDTO.setCustomerId(2L);

            when(customerRepository.findById(2L)).thenReturn(Optional.of(newCustomer));

            vehicleService.update(1L, updateDTO);

            assertThat(vehicleModel.getCustomer()).isEqualTo(newCustomer);
            verify(vehicleRepository).save(any(VehicleModel.class));
        }

        @Test
        @DisplayName("Deve lançar InactiveResourceException ao tentar asociar veículo a cliente inativo")
        void shouldThrowWhenNewCustomerIsInactive() {
            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicleModel));

            VehicleRequestDTO updateDTO = new VehicleRequestDTO();
            updateDTO.setBrand(vehicleModel.getBrand());
            updateDTO.setModel(vehicleModel.getModel());
            updateDTO.setYear(vehicleModel.getYear());
            updateDTO.setPlate(vehicleModel.getPlate());
            updateDTO.setColor(vehicleModel.getColor());
            updateDTO.setCustomerId(2L);

            when(customerRepository.findById(2L)).thenReturn(Optional.of(inactiveCustomer));

            assertThatThrownBy(() -> vehicleService.update(1L, updateDTO))
                    .isInstanceOf(InactiveResourceException.class)
                    .hasMessageContaining("inativo");

            verify(vehicleRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar DuplicateResourceException ao tentar alterar placa para uma já existente")
        void shouldThrowWhenNewPlateAlreadyExists() {
            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicleModel));
            when(vehicleRepository.existsByPlate("XYZ5678")).thenReturn(true);

            VehicleRequestDTO updateDTO = new VehicleRequestDTO();
            updateDTO.setBrand(vehicleModel.getBrand());
            updateDTO.setModel(vehicleModel.getModel());
            updateDTO.setYear(vehicleModel.getYear());
            updateDTO.setPlate("XYZ5678");
            updateDTO.setColor(vehicleModel.getColor());
            updateDTO.setCustomerId(1L);

            assertThatThrownBy(() -> vehicleService.update(1L, updateDTO))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("Plate");

            verify(vehicleRepository, never()).save(any());
        }

        @Test
        @DisplayName("Não deve validar placa quando manteve o mesmo valor")
        void shouldNotValidateWhenPlateUnchanged() {
            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicleModel));
            when(vehicleRepository.save(any(VehicleModel.class))).thenReturn(vehicleModel);

            VehicleRequestDTO updateDTO = new VehicleRequestDTO();
            updateDTO.setBrand(vehicleModel.getBrand());
            updateDTO.setModel(vehicleModel.getModel());
            updateDTO.setYear(vehicleModel.getYear());
            updateDTO.setPlate(vehicleModel.getPlate());
            updateDTO.setColor(vehicleModel.getColor());
            updateDTO.setCustomerId(1L);

            vehicleService.update(1L, updateDTO);

            verify(vehicleRepository, never()).existsByPlate(anyString());
        }
    }

    @Nested
    @DisplayName("deleteById - exclusão de veiculo")
    class DeleteById {

        @Test
        @DisplayName("Deve deletar veículo com sucesso")
        void shouldDeleteVehicle() {
            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicleModel));

            vehicleService.deleteById(1L);

            verify(vehicleRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando veículo não existe")
        void shouldThrowWhenVehicleNotFound() {
            when(vehicleRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> vehicleService.deleteById(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Vehicle");
        }
    }
}
