package dev.LimaDevCod3r.PitStopAPI.Vehicles;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<VehicleModel> getAll() {
        return this.vehicleService.getAll();
    }

    @GetMapping("/{id}")
    public String getById() {
        throw new UnsupportedOperationException();
    }

    @PostMapping
    public String create() {
        throw new UnsupportedOperationException();
    }

    @PutMapping("/{id}")
    public String update() {
        throw new UnsupportedOperationException();
    }

    @DeleteMapping("/{id}")
    public String delete() {
        throw new UnsupportedOperationException();
    }
}
