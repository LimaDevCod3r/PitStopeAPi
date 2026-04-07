package dev.LimaDevCod3r.PitStopAPI.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.LimaDevCod3r.PitStopAPI.model.CustomerModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponseDTO {

    private Long id;

    private String brand;

    private String model;

    private String year;

    private  String plate;

    private String color;

    @JsonIgnoreProperties("vehicles")
    private CustomerModel customer;
}
