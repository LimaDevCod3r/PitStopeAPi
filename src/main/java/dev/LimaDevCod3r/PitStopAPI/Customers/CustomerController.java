package dev.LimaDevCod3r.PitStopAPI.Customers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAll() {
        return ResponseEntity.ok().body(this.customerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getById(@PathVariable("id") Long id) {
        if(customerService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(this.customerService.getById(id));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody CustomerDTO customerDTO) {
        var customer = this.customerService.create(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente criado com sucesso: " + customer.getName() + " ID: " + customer.getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Long id, @RequestBody CustomerDTO customerModel) {
        if(customerService.getById(id) != null) {
          this.customerService.update(id, customerModel);
          return ResponseEntity.ok().body("Cliente atualizado com sucesso ID: " + id);
        }
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado ID: " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
      if(customerService.getById(id) != null) {
        this.customerService.deleteById(id);
          return ResponseEntity.ok().body("Cliente deletado com sucesso ID: " + id);
      }
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado ID: " + id);
    }
}
