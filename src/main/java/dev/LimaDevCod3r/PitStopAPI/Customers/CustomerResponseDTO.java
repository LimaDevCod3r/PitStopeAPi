package dev.LimaDevCod3r.PitStopAPI.Customers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.LimaDevCod3r.PitStopAPI.Vehicles.VehicleModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO {
    private Long id;

    private String name;

    private String cpf;

    private String email;

    private String phone;

    @JsonIgnoreProperties("customer")
    private List<VehicleModel> vehicles;
}
