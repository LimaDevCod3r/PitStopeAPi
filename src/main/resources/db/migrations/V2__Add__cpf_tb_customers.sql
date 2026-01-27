-- V2 Migrations: Add cpf to tb_customers
ALTER TABLE tb_customers
ADD COLUMN cpf VARCHAR(11);
