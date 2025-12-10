package ies.sequeros.com.dam.pmdm.administrador.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Pedido (

    val id: String,
    val status: String,
    val customerName: String?,
    val totalPrice: Double,
    val dependienteId: String?,

    val lineas: List<LineaPedido> = emptyList() //Lista de los detalles de cada pedido
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