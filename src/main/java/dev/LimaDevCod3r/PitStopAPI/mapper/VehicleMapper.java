package dev.LimaDevCod3r.PitStopAPI.mapper;

import dev.LimaDevCod3r.PitStopAPI.dto.VehicleRequestDTO;
import dev.LimaDevCod3r.PitStopAPI.dto.VehicleResponseDTO;
import dev.LimaDevCod3r.PitStopAPI.model.VehicleModel;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {
    public VehicleModel mapToModel(VehicleRequestDTO source) {
     if(source == null) {
         return null;
     }
        VehicleModel model = new VehicleModel();
        model.setBrand(source.getBrand());
        model.setModel(source.getModel());
        model.setYear(source.getYear());
        model.setPlate(source.getPlate());
        model.setCustomerId(source.getCustomerId());
        model.setColor(source.getColor());
        return model;
    }

    public VehicleRequestDTO mapToDTO(VehicleModel source) {
        if(source == null) {
            return null;
        }
        VehicleRequestDTO vehicleRequestDTO = new VehicleRequestDTO();
        vehicleRequestDTO.setBrand(source.getBrand());
        vehicleRequestDTO.setModel(source.getModel());
        vehicleRequestDTO.setYear(source.getYear());
        vehicleRequestDTO.setPlate(source.getPlate());
        vehicleRequestDTO.setColor(source.getColor());

        if(source.getCustomerId() != null) {
            vehicleRequestDTO.setCustomerId(source.getCustomerId());
        }
        return vehicleRequestDTO;

    }

    public VehicleResponseDTO mapToResponse(VehicleModel source) {
        VehicleResponseDTO target = new VehicleResponseDTO();
        target.setId(source.getId());
        target.setBrand(source.getBrand());
        target.setModel(source.getModel());
        target.setYear(source.getYear());
        target.setColor(source.getColor());
        target.setPlate(source.getPlate());
        target.setCustomer(source.getCustomer());
        return target;
    }
}
