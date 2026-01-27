package dev.LimaDevCod3r.PitStopAPI.Vehicles;

import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {
    public VehicleModel mapToModel(VehicleDTO source) {
     if(source == null) {
         return null;
     }
        VehicleModel model = new VehicleModel();
        model.setId(source.getId());
        model.setBrand(source.getBrand());
        model.setModel(source.getModel());
        model.setYear(source.getYear());
        model.setPlate(source.getPlate());
        model.setCustomerId(source.getCustomerId());
        model.setColor(source.getColor());
        return model;
    }

    public VehicleDTO mapToDTO(VehicleModel source) {
        if(source == null) {
            return null;
        }
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(source.getId());
        vehicleDTO.setBrand(source.getBrand());
        vehicleDTO.setModel(source.getModel());
        vehicleDTO.setYear(source.getYear());
        vehicleDTO.setPlate(source.getPlate());
        vehicleDTO.setColor(source.getColor());

        if(source.getCustomerId() != null) {
            vehicleDTO.setCustomerId(source.getCustomerId());
        }
        return vehicleDTO;

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
