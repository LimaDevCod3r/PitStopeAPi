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
    public VehicleModel getById(@PathVariable("id") Long id) {
        return this.vehicleService.getById(id);
    }

    @PostMapping
    public VehicleModel create(@RequestBody VehicleModel vehicleModel) {
        return this.vehicleService.create(vehicleModel);
    }

    @PutMapping("/{id}")
    public VehicleModel update(@PathVariable("id") Long id, @RequestBody VehicleModel vehicleModel) {
        return this.vehicleService.update(id, vehicleModel);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        this.vehicleService.deleteById(id);
    }
}
