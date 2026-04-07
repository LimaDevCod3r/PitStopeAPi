package dev.LimaDevCod3r.PitStopAPI.controller;

import dev.LimaDevCod3r.PitStopAPI.dto.ServiceOrderRequestDTO;
import dev.LimaDevCod3r.PitStopAPI.dto.ServiceOrderResponseDTO;
import dev.LimaDevCod3r.PitStopAPI.service.ServiceOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Ordens de Serviço", description = "Endpoints para gerenciamento de ordens de serviço")
@RestController
@RequestMapping("/ordens-servico")
public class ServiceOrderController {

    private final ServiceOrderService serviceOrderService;

    public ServiceOrderController(ServiceOrderService serviceOrderService) {
        this.serviceOrderService = serviceOrderService;
    }

    @Operation(summary = "Lista ordens de serviço", description = "Retorna uma página com todas as ordens de serviço")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<Page<ServiceOrderResponseDTO>> getAll(
            @Parameter(description = "Número da página (começa em 0)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Ordenação por campo (ex: id, createdAt)")
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(serviceOrderService.getAll(pageable));
    }

    @Operation(summary = "Busca ordem de serviço por ID", description = "Retorna uma ordem de serviço pelo ID")
    @ApiResponse(responseCode = "200", description = "Ordem encontrada")
    @ApiResponse(responseCode = "404", description = "Ordem de serviço não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrderResponseDTO> getById(
            @Parameter(description = "ID da ordem de serviço")
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(serviceOrderService.getById(id));
    }

    @Operation(summary = "Cria nova ordem de serviço", description = "Cria uma ordem de serviço vinculada a cliente ativo e veículo existente")
    @ApiResponse(responseCode = "201", description = "Ordem de serviço criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Cliente ou veículo não encontrado")
    @ApiResponse(responseCode = "410", description = "Cliente inativo — não é possível criar ordem")
    @PostMapping
    public ResponseEntity<String> create(
            @Valid @RequestBody ServiceOrderRequestDTO dto) {
        serviceOrderService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ordem de serviço criada com sucesso");
    }
}
