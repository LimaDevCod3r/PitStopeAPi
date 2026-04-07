package dev.LimaDevCod3r.PitStopAPI.controller;

import dev.LimaDevCod3r.PitStopAPI.dto.VehicleRequestDTO;
import dev.LimaDevCod3r.PitStopAPI.dto.VehicleResponseDTO;
import dev.LimaDevCod3r.PitStopAPI.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Veículos", description = "Endpoints para gerenciamento de veículos")
@RestController
@RequestMapping("/veiculos")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Operation(summary = "Busca todos os veículos ativos", description = "Retorna uma página com apenas veículos de clientes ativos")
    @ApiResponse(responseCode = "200", description = "Lista de veículos retornada com sucesso")
    @GetMapping
    public ResponseEntity<Page<VehicleResponseDTO>> getAll(
            @Parameter(description = "Número da página (começa em 0)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Ordenação por campo (ex: brand, model)")
            @RequestParam(defaultValue = "brand") String sortBy) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by(sortBy));
        return ResponseEntity.ok(vehicleService.getAll(pageable));
    }

    @Operation(summary = "Busca um veículo por ID", description = "Retorna um veículo ativo com base no ID fornecido")
    @ApiResponse(responseCode = "200", description = "Veículo encontrado")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @ApiResponse(responseCode = "410", description = "Veículo vinculado a cliente inativo")
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> getById(
            @Parameter(description = "ID do veículo a ser buscado")
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(vehicleService.getById(id));
    }

    @Operation(summary = "Cria um novo veículo", description = "Cria um novo veículo vinculado a um cliente ativo")
    @ApiResponse(responseCode = "201", description = "Veículo criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos — verifique os campos obrigatórios")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @ApiResponse(responseCode = "409", description = "Placa já cadastrada")
    @ApiResponse(responseCode = "410", description = "Cliente inativo — não é possível vincular veículo")
    @PostMapping
    public ResponseEntity<String> create(
            @Valid
            @RequestBody VehicleRequestDTO vehicleRequestDTO) {
        vehicleService.create(vehicleRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Veículo criado com sucesso");
    }

    @Operation(summary = "Atualiza um veículo por ID", description = "Atualiza os dados de um veículo existente")
    @ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Veículo ou cliente não encontrado")
    @ApiResponse(responseCode = "409", description = "Placa já pertence a outro veículo")
    @ApiResponse(responseCode = "410", description = "Veículo ou cliente inativo")
    @PutMapping("/{id}")
    public ResponseEntity<String> update(
            @PathVariable("id") Long id,
            @Valid
            @RequestBody VehicleRequestDTO vehicleRequestDTO) {
        vehicleService.update(id, vehicleRequestDTO);
        return ResponseEntity.ok("Veículo atualizado com sucesso ID: " + id);
    }

    @Operation(summary = "Deleta um veículo por ID", description = "Remove um veículo com base no ID fornecido")
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
