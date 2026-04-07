package dev.LimaDevCod3r.PitStopAPI.dto;

import dev.LimaDevCod3r.PitStopAPI.model.ServiceStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceOrderRequestDTO {

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String description;

    @NotNull(message = "Preço estimado é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço estimado deve ser maior que zero")
    @Digits(integer = 8, fraction = 2, message = "Preço estimado inválido")
    private BigDecimal estimatedPrice;

    @NotNull(message = "ID do veículo é obrigatório")
    private Long vehicleId;

    @NotNull(message = "ID do cliente é obrigatório")
    private Long customerId;

    private ServiceStatus status = ServiceStatus.OPEN;
}
