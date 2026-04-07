package dev.LimaDevCod3r.PitStopAPI.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequestDTO {

    @NotBlank(message = "Marca é obrigatória")
    @Size(max = 50, message = "Marca deve ter no máximo 50 caracteres")
    private String brand;

    @NotNull(message = "ID do cliente é obrigatório")
    private Long customerId;

    @NotBlank(message = "Modelo é obrigatório")
    @Size(max = 50, message = "Modelo deve ter no máximo 50 caracteres")
    private String model;

    @NotBlank(message = "Ano é obrigatório")
    @Pattern(regexp = "\\d{4}", message = "Ano deve ter 4 dígitos (ex: 2024)")
    private String year;

    @NotBlank(message = "Placa é obrigatória")
    @Pattern(regexp = "[A-Z]{3}\\d[A-Z]\\d{2}|[A-Z]{3}\\d{4}",
            message = "Placa inválida — use o formato Mercosul (ABC1D23) ou antigo (ABC1234)")
    @Size(max = 10)
    private String plate;

    @NotBlank(message = "Cor é obrigatória")
    @Size(max = 30, message = "Cor deve ter no máximo 30 caracteres")
    private String color;
}