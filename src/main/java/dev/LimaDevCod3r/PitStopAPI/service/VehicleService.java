package dev.LimaDevCod3r.PitStopAPI.service;

import dev.LimaDevCod3r.PitStopAPI.dto.VehicleDTO;
import dev.LimaDevCod3r.PitStopAPI.dto.VehicleResponseDTO;
import dev.LimaDevCod3r.PitStopAPI.exception.DuplicateResourceException;
import dev.LimaDevCod3r.PitStopAPI.exception.ResourceNotFoundException;
import dev.LimaDevCod3r.PitStopAPI.mapper.VehicleMapper;
import dev.LimaDevCod3r.PitStopAPI.model.CustomerModel;
import dev.LimaDevCod3r.PitStopAPI.model.VehicleModel;
import dev.LimaDevCod3r.PitStopAPI.repository.CustomerRepository;
import dev.LimaDevCod3r.PitStopAPI.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<VehicleResponseDTO> getAll() {
        return vehicleRepository.findAll().stream()
                .map(vehicleMapper::mapToResponse)
                .toList();
    }

    public VehicleResponseDTO getById(Long id) {
        VehicleModel model = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", id));
        return vehicleMapper.mapToResponse(model);
    }

    public VehicleDTO create(VehicleDTO vehicleDTO) {
        CustomerModel customer = customerRepository.findById(vehicleDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", vehicleDTO.getCustomerId()));

        if (vehicleRepository.existsByPlate(vehicleDTO.getPlate())) {
            throw new DuplicateResourceException("Plate", vehicleDTO.getPlate());
        }

        VehicleModel entity = vehicleMapper.mapToModel(vehicleDTO);
        entity.setCustomer(customer);
        VehicleModel saved = vehicleRepository.save(entity);
        return vehicleMapper.mapToDTO(saved);
    }

    public VehicleResponseDTO update(Long id, VehicleDTO vehicleDTO) {
        VehicleModel vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", id));

        if (vehicleDTO.getCustomerId() != null && !vehicleDTO.getCustomerId().equals(vehicle.getCustomer().getId())) {
            CustomerModel customer = customerRepository.findById(vehicleDTO.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", vehicleDTO.getCustomerId()));
            vehicle.setCustomer(customer);
        }

        if (!vehicleDTO.getPlate().equals(vehicle.getPlate()) && vehicleRepository.existsByPlate(vehicleDTO.getPlate())) {
            throw new DuplicateResourceException("Plate", vehicleDTO.getPlate());
        }

        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setYear(vehicleDTO.getYear());
        vehicle.setColor(vehicleDTO.getColor());
        vehicle.setPlate(vehicleDTO.getPlate());
        vehicleRepository.save(vehicle);
        return vehicleMapper.mapToResponse(vehicle);
    }

    public void deleteById(Long id) {
        vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", id));
        vehicleRepository.deleteById(id);
    }
}
