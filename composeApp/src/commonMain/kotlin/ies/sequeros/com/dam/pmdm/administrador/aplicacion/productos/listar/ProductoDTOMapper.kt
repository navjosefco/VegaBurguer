package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar

import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto


fun Producto.toDTO(path: String="") = ProductoDTO(

    id = id,
    categoria_id = categoria_id,
    name = name,
    description = description,
    price = price,
    image_path = path+image_path,
    enabled=enabled
)

fun ProductoDTO.toProducto()= Producto(

    id = id,
    categoria_id = categoria_id,
    name = name,
    description = description,
    price = price,
    image_path = image_path,
    enabled
)

