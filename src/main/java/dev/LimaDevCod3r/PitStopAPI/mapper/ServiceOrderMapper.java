package dev.LimaDevCod3r.PitStopAPI.mapper;

import dev.LimaDevCod3r.PitStopAPI.dto.ServiceOrderRequestDTO;
import dev.LimaDevCod3r.PitStopAPI.dto.ServiceOrderResponseDTO;
import dev.LimaDevCod3r.PitStopAPI.model.ServiceOrderModel;
import org.springframework.stereotype.Component;

@Component
public class ServiceOrderMapper {

    public ServiceOrderModel mapToModel(ServiceOrderRequestDTO source) {
        if (source == null) {
            return null;
        }
        ServiceOrderModel model = new ServiceOrderModel();
        model.setDescription(source.getDescription());
        model.setEstimatedPrice(source.getEstimatedPrice());
        model.setStatus(source.getStatus());
        return model;
    }

    public ServiceOrderResponseDTO mapToResponse(ServiceOrderModel source) {
        if (source == null) {
            return null;
        }
        ServiceOrderResponseDTO dto = new ServiceOrderResponseDTO();
        dto.setId(source.getId());
        dto.setDescription(source.getDescription());
        dto.setEstimatedPrice(source.getEstimatedPrice());
        dto.setStatus(source.getStatus());
        dto.setCreatedAt(source.getCreatedAt());
        dto.setFinishedAt(source.getFinishedAt());

        if (source.getCustomer() != null) {
            dto.setCustomerId(source.getCustomer().getId());
            dto.setCustomerName(source.getCustomer().getName());
        }

        if (source.getVehicle() != null) {
            dto.setVehicleId(source.getVehicle().getId());
            dto.setVehicleBrand(source.getVehicle().getBrand());
            dto.setVehicleModel(source.getVehicle().getModel());
            dto.setVehiclePlate(source.getVehicle().getPlate());
        }

        return dto;
    }
}
