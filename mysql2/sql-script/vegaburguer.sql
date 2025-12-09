CREATE DATABASE vegaburguer DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_unicode_ci;

USE vegaburguer;

CREATE TABLE categoria (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    image_path VARCHAR(255),
    enabled BOOLEAN NOT NULL
);

CREATE TABLE dependiente ( 
    id VARCHAR(36) PRIMARY KEY, 
    name VARCHAR(100) NOT NULL, 
    email VARCHAR(100) NOT NULL, 
    password VARCHAR(100) NOT NULL, 
    image_path VARCHAR(255), 
    enabled BOOLEAN NOT NULL, 
    is_admin BOOLEAN NOT NULL 
);

CREATE TABLE producto (
    id VARCHAR(36) PRIMARY KEY,
    categoria_id VARCHAR(36) NOT NULL,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    image_path VARCHAR(255),
    enabled BOOLEAN NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

CREATE TABLE pedido (
    id VARCHAR(36) PRIMARY KEY,
    status VARCHAR(20) NOT NULL, 
    customer_name VARCHAR(100),
    total_price DECIMAL(10, 2) NOT NULL,
    dependiente_id VARCHAR(36), 
    FOREIGN KEY (dependiente_id) REFERENCES dependiente(id)
);

CREATE TABLE linea_pedido (
    id VARCHAR(36) PRIMARY KEY,
    pedido_id VARCHAR(36) NOT NULL,
    producto_id VARCHAR(36) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

INSERT INTO dependiente (id, name, email, password, image_path, enabled, is_admin)
VALUES ('fa4cb2c5-08a2-41c3-9f42-84c75fb09ea0', 'admin', 'admin@vegaburguer.com', '1Yi6Y3N9izp+mRRKwt42KlAaTgF7WIk5x+qOlpvjHc19+W7wPKvDVK3Dp5hEb7DP', '358885e9-0b9a-4007-8aac-a81883a77b06.png', 1, 1);