package dev.LimaDevCod3r.PitStopAPI.Vehicles;

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
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping
    public ResponseEntity<List<VehicleResponseDTO>> getAll() {
        return ResponseEntity.ok(this.vehicleService.getAll());
    }


    @Operation(summary = "Busca um veículo por ID", description = "Retorna um veículo com base no ID fornecido")
    @ApiResponse(responseCode = "200", description = "veículo encontrado")
    @ApiResponse(responseCode = "404", description = "veículo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @Parameter(description = "ID do veículo a ser buscado")
            @PathVariable("id") Long id) {
        if (this.vehicleService.getById(id) == null) {
            return ResponseEntity.status(404).body("ID: " + id + " do veículo não encontrado");
        }
        return ResponseEntity.ok(this.vehicleService.getById(id));
    }


    @Operation(summary = "Cria um novo veículo", description = "Cria um novo veículo com as informações fornecidas")
    @ApiResponse(responseCode = "201", description = "veículo veículo com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PostMapping
    public ResponseEntity<String> create(
            @Parameter(description = "Dados do veículo a serem criados")
            @RequestBody VehicleDTO vehicleDTO) {
        var vehicle = this.vehicleService.create(vehicleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Véiculo cadastrado com sucesso ! " + "ID: " + vehicle.getId() + " Brand: " + vehicle.getBrand());
    }


    @Operation(summary = "Atualiza um veículo por ID", description = "Atualiza as informações de um veículo com base no ID fornecido")
    @ApiResponse(responseCode = "200", description = "veículo atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "404", description = "veículo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PutMapping("/{id}")
    public ResponseEntity<String> update(
            @Parameter(description = "ID do veículo a ser buscado")
            @PathVariable("id") Long id,
            @Parameter(description = "Dados do veículo a serem atualizados")
            @RequestBody VehicleDTO vehicleDTO) {

        if (vehicleService.getById(id) != null) {
            this.vehicleService.update(id, vehicleDTO);
            return ResponseEntity.ok().body("Cliente atualizado com sucesso ID: " + id);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado ID: " + id);
    }

    @Operation(summary = "Deleta um veículo por ID", description = "Deleta um veículo com base no ID fornecido")
    @ApiResponse(responseCode = "200", description = "veículo deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "veículo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @Parameter(description = "ID do veículo a ser buscado")
            @PathVariable("id") Long id) {
        if (vehicleService.getById(id) != null) {
            this.vehicleService.deleteById(id);
            return ResponseEntity.ok().body("Cliente deletado com sucesso ID: " + id);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado ID: " + id);
    }
}

