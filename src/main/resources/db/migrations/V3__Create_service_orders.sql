CREATE TABLE tb_service_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    customer_id BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,

    description VARCHAR(255) NOT NULL,

    estimated_price DECIMAL(10,2) NOT NULL,

    status VARCHAR(20) NOT NULL,

    created_at DATETIME NOT NULL,
    finished_at DATETIME NULL,

    CONSTRAINT fk_service_orders_customer
        FOREIGN KEY (customer_id) REFERENCES tb_customers (id),

    CONSTRAINT fk_service_orders_vehicle
        FOREIGN KEY (vehicle_id) REFERENCES tb_vehicles (id)
);