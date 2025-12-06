package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar

import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria

fun Categoria.toDTO(path: String = ""): CategoriaDTO {
    return CategoriaDTO(
        id = id,
        name = name,
        image_path = path + image_path,
        enabled = enabled
    )
}

fun CategoriaDTO.toCategoria(): Categoria {
    return Categoria(
        id = id,
        name = name,
        image_path = image_path,
        enabled = enabled
    )
}
