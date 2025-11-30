package ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.cambiaclave

import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class CambiarClaveDependienteUseCase(
    private val repositorio: IDependienteRepositorio,
    private val almacenDatos: AlmacenDatos
) {
    suspend fun invoke(id: String, oldPass: String, newPass: String) {
        repositorio.cambiarContrasenya(id, oldPass, newPass)
    }
}