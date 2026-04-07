package dev.LimaDevCod3r.PitStopAPI.controller;

import dev.LimaDevCod3r.PitStopAPI.dto.VehicleDTO;
import dev.LimaDevCod3r.PitStopAPI.dto.VehicleResponseDTO;
import dev.LimaDevCod3r.PitStopAPI.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Veículos", description = "Endpoints para gerenciamento de veículos")
@RestController
@RequestMapping("/veiculos")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Operation(summary = "Busca todos os veículos", description = "Retorna uma lista com todos os veículos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de veículos retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<VehicleResponseDTO>> getAll() {
        return ResponseEntity.ok(vehicleService.getAll());
    }

    @Operation(summary = "Busca um veículo por ID", description = "Retorna um veículo com base no ID fornecido")
    @ApiResponse(responseCode = "200", description = "Veículo encontrado")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> getById(
            @Parameter(description = "ID do veículo a ser buscado")
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(vehicleService.getById(id));
    }

    @Operation(summary = "Cria um novo veículo", description = "Cria um novo veículo com as informações fornecidas")
    @ApiResponse(responseCode = "201", description = "Veículo criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @PostMapping
    public ResponseEntity<String> create(
            @Parameter(description = "Dados do veículo a serem criados")
            @RequestBody VehicleDTO vehicleDTO) {
        var vehicle = vehicleService.create(vehicleDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Véiculo cadastrado com sucesso! ID: " + vehicle.getId() + " Brand: " + vehicle.getBrand());
    }

    @Operation(summary = "Atualiza um veículo por ID", description = "Atualiza as informações de um veículo")
    @ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<String> update(
            @Parameter(description = "ID do veículo a ser atualizado")
            @PathVariable("id") Long id,
            @Parameter(description = "Dados do veículo a serem atualizados")
            @RequestBody VehicleDTO vehicleDTO) {
        vehicleService.update(id, vehicleDTO);
        return ResponseEntity.ok("Veículo atualizado com sucesso ID: " + id);
    }

    @Operation(summary = "Deleta um veículo por ID", description = "Deleta um veículo com base no ID fornecido")
    @ApiResponse(responseCode = "200", description = "Veículo deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @Parameter(description = "ID do veículo a ser deletado")
            @PathVariable("id") Long id) {
        vehicleService.deleteById(id);
        return ResponseEntity.ok("Veículo deletado com sucesso ID: " + id);
    }
}
