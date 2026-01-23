package dev.LimaDevCod3r.PitStopAPI.Customers;

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

    private List<VehicleModel> vehicleModel;
}
