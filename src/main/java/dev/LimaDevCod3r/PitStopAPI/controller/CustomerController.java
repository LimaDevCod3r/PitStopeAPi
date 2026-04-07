package dev.LimaDevCod3r.PitStopAPI.controller;

import dev.LimaDevCod3r.PitStopAPI.dto.CustomerRequestDTO;
import dev.LimaDevCod3r.PitStopAPI.dto.CustomerResponseDTO;
import dev.LimaDevCod3r.PitStopAPI.service.CustomerService;
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

@Tag(name = "Clientes", description = "Endpoints para gerenciamento de clientes")
@RestController
@RequestMapping("/clientes")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Busca todos os clientes ativos", description = "Retorna uma página com apenas clientes ativos")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    @GetMapping
    public ResponseEntity<Page<CustomerResponseDTO>> getAll(
            @Parameter(description = "Número da página (começa em 0)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Ordenação por campo (ex: name, cpf)")
            @RequestParam(defaultValue = "name") String sortBy) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by(sortBy));
        return ResponseEntity.ok(customerService.getAll(pageable));
    }

    @Operation(summary = "Busca um cliente por ID", description = "Retorna os dados de um cliente ativo pelo ID")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @ApiResponse(responseCode = "410", description = "Cliente desativado")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getById(
            @Parameter(description = "ID do cliente")
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(customerService.getById(id));
    }

    @Operation(summary = "Cria um novo cliente", description = "Cria um novo cliente ativo com as informações fornecidas")
    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos — verifique os campos obrigatórios")
    @ApiResponse(responseCode = "409", description = "CPF ou email já existem")
    @PostMapping
    public ResponseEntity<String> create(
            @Valid
            @RequestBody CustomerRequestDTO customerRequestDTO) {
        var customer = customerService.create(customerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Cliente criado com sucesso: " + customer.getName());
    }

    @Operation(summary = "Atualiza um cliente por ID", description = "Atualiza os dados de um cliente ativo")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @ApiResponse(responseCode = "409", description = "CPF ou email já pertencem a outro cliente")
    @ApiResponse(responseCode = "410", description = "Cliente desativado — não é possível atualizar")
    @PutMapping("/{id}")
    public ResponseEntity<String> update(
            @PathVariable("id") Long id,
            @RequestBody CustomerRequestDTO customerRequestDTO) {
        customerService.update(id, customerRequestDTO);
        return ResponseEntity.ok("Cliente atualizado com sucesso ID: " + id);
    }

    @Operation(summary = "Desativa um cliente por ID", description = "Realiza soft delete — o cliente é desativado mas permanece no banco")
    @ApiResponse(responseCode = "200", description = "Cliente desativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @Parameter(description = "ID do cliente a ser desativado")
            @PathVariable("id") Long id) {
        customerService.deleteById(id);
        return ResponseEntity.ok("Cliente desativado com sucesso ID: " + id);
    }
}
