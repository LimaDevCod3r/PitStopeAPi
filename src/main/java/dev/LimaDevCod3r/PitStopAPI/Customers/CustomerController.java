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
    public CustomerModel getById(@PathVariable("id") Long id) {
        return this.customerService.getById(id);
    }

    @PostMapping
    public CustomerModel create(@RequestBody CustomerModel customer) {
       return this.customerService.create(customer);
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
