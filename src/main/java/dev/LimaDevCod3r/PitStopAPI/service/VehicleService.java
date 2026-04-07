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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final VehicleMapper vehicleMapper;

    public VehicleService(VehicleRepository vehicleRepository, CustomerRepository customerRepository, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
        this.vehicleMapper = vehicleMapper;
    }

    public Page<VehicleResponseDTO> getAll(Pageable pageable) {
        return vehicleRepository.findAllByCustomerActiveTrue(pageable)
                .map(vehicleMapper::mapToResponse);
    }

    public VehicleResponseDTO getById(Long id) {
        VehicleModel model = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", id));

        if (model.getCustomer() == null || !model.getCustomer().getActive()) {
            throw new InactiveResourceException("Vehicle", id);                                                                 }

        return vehicleMapper.mapToResponse(model);
    }

    public VehicleRequestDTO create(VehicleRequestDTO vehicleRequestDTO) {
        // Verifica se o cliente existe
        CustomerModel customer = customerRepository.findById(vehicleRequestDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", vehicleRequestDTO.getCustomerId()));

        //  Verifica se o cliente está ativo
        if (!customer.getActive()) {
            throw new InactiveResourceException("Customer", vehicleRequestDTO.getCustomerId());
        }

        //  Verifica se a placa já existe
        if (vehicleRepository.existsByPlate(vehicleRequestDTO.getPlate())) {
            throw new DuplicateResourceException("Plate", vehicleRequestDTO.getPlate());
        }

        VehicleModel entity = vehicleMapper.mapToModel(vehicleRequestDTO);
        entity.setCustomer(customer);
        VehicleModel saved = vehicleRepository.save(entity);
        return vehicleMapper.mapToDTO(saved);
    }

    public VehicleResponseDTO update(Long id, VehicleRequestDTO vehicleRequestDTO) {
        // Busca o veículo pelo ID ou lança erro 404 se não existir.
        VehicleModel vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", id));

        //  Se o ID do cliente mudou, busca o novo cliente e verifica se ele está ativo.
        if (vehicleRequestDTO.getCustomerId() != null
                && vehicle.getCustomer() != null
                && !vehicleRequestDTO.getCustomerId().equals(vehicle.getCustomer().getId())) {
            CustomerModel customer = customerRepository.findById(vehicleRequestDTO.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", vehicleRequestDTO.getCustomerId()));

            if (!customer.getActive()) {
                throw new InactiveResourceException("Customer", vehicleRequestDTO.getCustomerId());
            }
            vehicle.setCustomer(customer);
        }

        // Se a placa mudou, valida se a nova placa já pertence a outro veículo.
        if (vehicleRequestDTO.getPlate() != null && !vehicleRequestDTO.getPlate().isBlank()
                && !vehicleRequestDTO.getPlate().equals(vehicle.getPlate())
                && vehicleRepository.existsByPlate(vehicleRequestDTO.getPlate())) {
            throw new DuplicateResourceException("Plate", vehicleRequestDTO.getPlate());
        }

        vehicle.setBrand(vehicleRequestDTO.getBrand());
        vehicle.setModel(vehicleRequestDTO.getModel());
        vehicle.setYear(vehicleRequestDTO.getYear());
        vehicle.setColor(vehicleRequestDTO.getColor());
        vehicle.setPlate(vehicleRequestDTO.getPlate());
        vehicleRepository.save(vehicle);
        return vehicleMapper.mapToResponse(vehicle);
    }

    public void deleteById(Long id) {
        vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", id));
        vehicleRepository.deleteById(id);
    }
}
