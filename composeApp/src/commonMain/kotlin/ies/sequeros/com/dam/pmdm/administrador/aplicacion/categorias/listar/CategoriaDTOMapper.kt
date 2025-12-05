package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar

import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria

fun Categoria.toDTO(): CategoriaDTO {
    return CategoriaDTO(
        id = this.id,
        name = this.name,
        image_path = this.image_path, // Mapeo desde la entidad (image_path)
        enabled = this.enabled
    )
}

fun CategoriaDTO.toCategoria(): Categoria {
    return Categoria(
        id = this.id,
        name = this.name,
        image_path = this.image_path, // Mapeo hacia la entidad (image_path)
        enabled = this.enabled
    )
}
