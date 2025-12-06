package ies.sequeros.com.dam.pmdm.administrador.modelo

data class Pedido (

    val id: String,
    val status: String,
    val customerName: String?,
    val totalPrice: Double,
    val dependienteId: String?
)

/*
CREATE TABLE pedido (
    id VARCHAR(36) PRIMARY KEY,
    status VARCHAR(20) NOT NULL,
    customer_name VARCHAR(100),
    total_price DECIMAL(10, 2) NOT NULL,
    dependiente_id VARCHAR(36),
    FOREIGN KEY (dependiente_id) REFERENCES dependiente(id)
);
* */