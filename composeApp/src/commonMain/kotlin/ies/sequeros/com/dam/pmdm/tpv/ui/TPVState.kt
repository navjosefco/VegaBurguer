package ies.sequeros.com.dam.pmdm.tpv.ui

data class TPVState(
    val customerName: String = "",
    val items: List<ItemCarrito> = emptyList()
) {

    val total: Double get() = items.sumOf { it.total }
    val itemCount: Int get() = items.sumOf { it.quantity }
}
