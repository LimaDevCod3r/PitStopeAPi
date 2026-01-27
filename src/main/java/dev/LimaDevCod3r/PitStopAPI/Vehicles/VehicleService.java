package dev.LimaDevCod3r.PitStopAPI.Vehicles;

import dev.LimaDevCod3r.PitStopAPI.Customers.CustomerModel;
import dev.LimaDevCod3r.PitStopAPI.Customers.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return this.vehicleRepository.findAll()
                .stream()
                .map(this.vehicleMapper::mapToResponse).toList();
    }

    public VehicleResponseDTO getById(Long id) {
        Optional<VehicleModel> vehicleById = this.vehicleRepository.findById(id);
        return vehicleById.map(this.vehicleMapper::mapToResponse).orElse(null);
    }

    public VehicleDTO create(VehicleDTO vehicleDTO) {
        CustomerModel customer = customerRepository.findById(vehicleDTO.getCustomerId()).orElseThrow(
                () -> new RuntimeException("Customer not found with id: " + vehicleDTO.getCustomerId())
        );

        if(this.vehicleRepository.existsByPlate(vehicleDTO.getPlate())) {
            throw new RuntimeException("Vehicle with plate " + vehicleDTO.getPlate() + " already exists");
        }

        var entity = this.vehicleMapper.mapToModel(vehicleDTO);
        entity.setCustomer(customer);
        var savedVehicle = vehicleRepository.save(entity);
        return this.vehicleMapper.mapToDTO(savedVehicle);
    }

    public VehicleResponseDTO update(Long id, VehicleDTO vehicleDTO) {
      VehicleModel vehicleToUpdate = vehicleRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Vehicle not found with id: " + id)
        );

        if(vehicleDTO.getCustomerId() != null && !vehicleDTO.getCustomerId().equals(vehicleToUpdate.getCustomer().getId())) {
            CustomerModel customer = customerRepository.findById(vehicleDTO.getCustomerId()).orElseThrow(
                    () -> new RuntimeException("Customer not found with id: " + vehicleDTO.getCustomerId())
            );
            vehicleToUpdate.setCustomer(customer);
        }

      if(!vehicleDTO.getPlate().equals(vehicleToUpdate.getPlate()) && this.vehicleRepository.existsByPlate(vehicleDTO.getPlate())) {
          throw new RuntimeException("Vehicle with plate " + vehicleDTO.getPlate() + " already exists");
      }


      vehicleToUpdate.setBrand(vehicleDTO.getBrand());
      vehicleToUpdate.setModel(vehicleDTO.getModel());
      vehicleToUpdate.setYear(vehicleDTO.getYear());
      vehicleToUpdate.setColor(vehicleDTO.getColor());
      vehicleToUpdate.setPlate(vehicleDTO.getPlate());
      this.vehicleRepository.save(vehicleToUpdate);
      return this.vehicleMapper.mapToResponse(vehicleToUpdate);
    }

    public void deleteById(Long id) {
        this.vehicleRepository.deleteById(id);
    }
}
