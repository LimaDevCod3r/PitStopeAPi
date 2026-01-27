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
    public List<VehicleResponseDTO> getAll() {
        return this.vehicleService.getAll();
    }

    @GetMapping("/{id}")
    public VehicleResponseDTO getById(@PathVariable("id") Long id) {
        return this.vehicleService.getById(id);
    }

    @PostMapping
    public VehicleDTO create(@RequestBody VehicleDTO vehicleDTO) {
        return this.vehicleService.create(vehicleDTO);
    }

    @PutMapping("/{id}")
    public VehicleResponseDTO update(@PathVariable("id") Long id, @RequestBody VehicleDTO vehicleDTO) {
        return this.vehicleService.update(id, vehicleDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        this.vehicleService.deleteById(id);
    }
}
