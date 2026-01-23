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
    public CustomerDTO create(@RequestBody CustomerDTO customerDTO) {
       return this.customerService.create(customerDTO);
    }

    @PutMapping("/{id}")
    public CustomerModel update(@PathVariable("id") Long id, @RequestBody CustomerModel customerModel) {
        return this.customerService.update(id, customerModel);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        this.customerService.deleteById(id);
    }
}
