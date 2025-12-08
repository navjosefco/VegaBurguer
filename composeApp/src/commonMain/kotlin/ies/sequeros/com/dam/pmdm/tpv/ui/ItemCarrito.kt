package ies.sequeros.com.dam.pmdm.tpv.ui

data class ItemCarrito(

    val productoId: String,
    val productoNombre: String,
    val quantity: Int,
    val unitPrice: Double,
    val imagen: String
) {

    val total: Double get() = quantity * unitPrice
}
