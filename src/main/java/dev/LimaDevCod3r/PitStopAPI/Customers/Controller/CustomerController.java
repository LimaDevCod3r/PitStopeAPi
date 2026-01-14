package dev.LimaDevCod3r.PitStopAPI.Customers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clientes")
public class CustomerController {

    @GetMapping("/teste")
    public String getCustomer() {
        return "Cliente teste 123";
    }
}
