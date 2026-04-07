package dev.LimaDevCod3r.PitStopAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {
    private Long id;

    private String brand;

    private Long customerId;

    private String model;

    private String year;

    private  String plate;

    private String color;
}
