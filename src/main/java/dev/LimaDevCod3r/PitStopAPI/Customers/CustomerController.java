package dev.LimaDevCod3r.PitStopAPI.Customers;

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
    public List<CustomerModel> getAll() {
      return  this.customerService.getAll();
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
