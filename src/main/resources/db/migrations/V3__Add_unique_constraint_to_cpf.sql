-- V3 Migration: add NOT NULL and UNIQUE constraint to cpf in tb_customers
ALTER TABLE tb_customers
ALTER COLUMN cpf SET NOT NULL;

ALTER TABLE tb_customers
ADD CONSTRAINT uk_tb_customers_cpf UNIQUE (cpf);
