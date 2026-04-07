package dev.LimaDevCod3r.PitStopAPI.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tb_service_orders")
public class ServiceOrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerModel customer;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleModel vehicle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description; // problema relatado

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedPrice;

    @Column(nullable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private ServiceStatus status;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;
}
