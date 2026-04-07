package dev.LimaDevCod3r.PitStopAPI.controller;

import dev.LimaDevCod3r.PitStopAPI.dto.CustomerDTO;
import dev.LimaDevCod3r.PitStopAPI.dto.CustomerResponseDTO;
import dev.LimaDevCod3r.PitStopAPI.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Clientes", description = "Endpoints para gerenciamento de clientes")
@RestController
@RequestMapping("/clientes")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @Operation(summary = "Busca todos os clientes", description = "Retorna uma lista com todos os clientes cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAll() {
        return ResponseEntity.ok().body(this.customerService.getAll());
    }


    @Operation(summary = "Busca um cliente por ID", description = "Retorna um cliente com base no ID fornecido")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getById(
            @Parameter(description = "ID do cliente a ser buscado")
            @PathVariable("id") Long id) {
        if(customerService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(this.customerService.getById(id));
    }




    @Operation(summary = "Cria um novo cliente", description = "Cria um novo cliente com as informações fornecidas")
    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PostMapping
    public ResponseEntity<String> create(
            @Parameter(description = "Dados do cliente a serem criados")
            @RequestBody CustomerDTO customerDTO) {
        var customer = this.customerService.create(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente criado com sucesso: " + customer.getName() + " ID: " + customer.getId());
    }


    @Operation(summary = "Atualiza um cliente por ID", description = "Atualiza as informações de um cliente com base no ID fornecido")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PutMapping("/{id}")
    public ResponseEntity<String> update(
            @Parameter(description = "ID do cliente a ser atualizado")
            @PathVariable("id") Long id,
            @Parameter(description = "Dados do cliente a serem atualizados")
            @RequestBody CustomerDTO customerModel) {
        if(customerService.getById(id) != null) {
          this.customerService.update(id, customerModel);
          return ResponseEntity.ok().body("Cliente atualizado com sucesso ID: " + id);
        }
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado ID: " + id);
    }


    @Operation(summary = "Deleta um cliente por ID", description = "Deleta um cliente com base no ID fornecido")
    @ApiResponse(responseCode = "200", description = "Cliente deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @Parameter(description = "ID do cliente a ser deletado")
            @PathVariable("id") Long id) {
      if(customerService.getById(id) != null) {
        this.customerService.deleteById(id);
          return ResponseEntity.ok().body("Cliente deletado com sucesso ID: " + id);
      }
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado ID: " + id);
    }
}
