package dev.LimaDevCod3r.PitStopAPI.service;

import dev.LimaDevCod3r.PitStopAPI.dto.ServiceOrderRequestDTO;
import dev.LimaDevCod3r.PitStopAPI.dto.ServiceOrderResponseDTO;
import dev.LimaDevCod3r.PitStopAPI.exception.InactiveResourceException;
import dev.LimaDevCod3r.PitStopAPI.exception.ResourceNotFoundException;
import dev.LimaDevCod3r.PitStopAPI.mapper.ServiceOrderMapper;
import dev.LimaDevCod3r.PitStopAPI.model.CustomerModel;
import dev.LimaDevCod3r.PitStopAPI.model.ServiceOrderModel;
import dev.LimaDevCod3r.PitStopAPI.model.VehicleModel;
import dev.LimaDevCod3r.PitStopAPI.repository.CustomerRepository;
import dev.LimaDevCod3r.PitStopAPI.repository.ServiceOrderRepository;
import dev.LimaDevCod3r.PitStopAPI.repository.VehicleRepository;
import dev.LimaDevCod3r.PitStopAPI.model.ServiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final ServiceOrderMapper serviceOrderMapper;

    public ServiceOrderService(ServiceOrderRepository serviceOrderRepository,
                               CustomerRepository customerRepository,
                               VehicleRepository vehicleRepository,
                               ServiceOrderMapper serviceOrderMapper) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
        this.serviceOrderMapper = serviceOrderMapper;
    }

    public ServiceOrderResponseDTO create(ServiceOrderRequestDTO dto) {
        CustomerModel customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", dto.getCustomerId()));

        if (!customer.getActive()) {
            throw new InactiveResourceException("Customer", dto.getCustomerId());
        }

        VehicleModel vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", dto.getVehicleId()));

        ServiceOrderModel entity = serviceOrderMapper.mapToModel(dto);
        entity.setCustomer(customer);
        entity.setVehicle(vehicle);
        entity.setStatus(ServiceStatus.OPEN);
        entity.setCreatedAt(LocalDateTime.now());

        ServiceOrderModel saved = serviceOrderRepository.save(entity);
        return serviceOrderMapper.mapToResponse(saved);
    }

    public Page<ServiceOrderResponseDTO> getAll(Pageable pageable) {
        return serviceOrderRepository.findAll(pageable)
                .map(serviceOrderMapper::mapToResponse);
    }

    public ServiceOrderResponseDTO getById(Long id) {
        ServiceOrderModel model = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceOrder", id));
        return serviceOrderMapper.mapToResponse(model);
    }
}
