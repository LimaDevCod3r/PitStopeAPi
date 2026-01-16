package dev.LimaDevCod3r.PitStopAPI.Vehicles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.LimaDevCod3r.PitStopAPI.Customers.CustomerModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muitos veiculos para um Cliente
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnoreProperties("vehicles")
    private CustomerModel customer;

    @Column(name = "customer_id", insertable = false, updatable = false)
    private Long customerId;

    @Column(nullable = false, length = 50)
    private String brand;

    @Column(nullable = false, length = 50)
    private String model;

    @Column(name = "manufacture_year", nullable = false)
    private String year;

    @Column(nullable = false, unique = true, length = 10)
    private  String plate;

    @Column(length = 30)
    private String color;
}
