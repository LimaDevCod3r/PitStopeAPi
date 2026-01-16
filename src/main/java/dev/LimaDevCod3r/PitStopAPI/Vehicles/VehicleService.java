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
}
