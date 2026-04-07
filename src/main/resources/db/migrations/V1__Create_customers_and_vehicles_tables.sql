-- Create customers table
CREATE TABLE tb_customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(120) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL
);

-- Create vehicles table
CREATE TABLE tb_vehicles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    manufacture_year VARCHAR(4) NOT NULL,
    plate VARCHAR(10) NOT NULL UNIQUE,
    color VARCHAR(30),
    CONSTRAINT fk_vehicles_customer FOREIGN KEY (customer_id) REFERENCES tb_customers (id)
);
