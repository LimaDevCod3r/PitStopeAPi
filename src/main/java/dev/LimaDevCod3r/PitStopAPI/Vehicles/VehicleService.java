package dev.LimaDevCod3r.PitStopAPI.Vehicles;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<VehicleModel> getAll() {
        return this.vehicleRepository.findAll();
    }

    public VehicleModel getById(Long id) {
        return this.vehicleRepository.findById(id).orElse(null);
    }

    public VehicleModel create(VehicleModel vehicleModel) {
        return this.vehicleRepository.save(vehicleModel);
    }

    public VehicleModel update(Long id, VehicleModel vehicleModel) {
        VehicleModel vehicle = this.vehicleRepository.findById(id).orElse(null);
        if (vehicle == null) {
            return null;
        }
        vehicle.setBrand(vehicleModel.getBrand());
        vehicle.setModel(vehicleModel.getModel());
        vehicle.setYear(vehicleModel.getYear());
        vehicle.setColor(vehicleModel.getColor());
        vehicle.setPlate(vehicleModel.getPlate());
        return this.vehicleRepository.save(vehicle);
    }

     public void deleteById(Long id) {
        this.vehicleRepository.deleteById(id);
    }
}
