package dev.LimaDevCod3r.PitStopAPI.dto;

import dev.LimaDevCod3r.PitStopAPI.model.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceOrderResponseDTO {
    private Long id;

    private String description;

    private BigDecimal estimatedPrice;

    private ServiceStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;

    private Long customerId;

    private String customerName;

    private Long vehicleId;

    private String vehicleBrand;

    private String vehicleModel;

    private String vehiclePlate;
}
