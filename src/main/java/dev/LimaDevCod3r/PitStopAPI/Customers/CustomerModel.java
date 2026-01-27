package dev.LimaDevCod3r.PitStopAPI.Customers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.LimaDevCod3r.PitStopAPI.Vehicles.VehicleModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tb_customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "vehicles")
public class CustomerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Um cliente pode ter muitos veículos
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("customer")
    private List<VehicleModel> vehicles;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 11, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

}
