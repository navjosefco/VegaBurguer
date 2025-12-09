package ies.sequeros.com.dam.pmdm.administrador.aplicacion.login

import ies.sequeros.com.dam.pmdm.administrador.modelo.Dependiente
import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio

class LoginUseCase(private val repositorio: IDependienteRepositorio) {

    suspend operator fun invoke(nombre: String, password: String): Result<Dependiente> {

        return try {

            val usuario = repositorio.login(nombre, password)

            if (usuario != null) {
                
                // Login correcto: Guardamos sesión
                Sesion.iniciar(usuario)
                Result.success(usuario)
            } else {
                // Login fallido: Credenciales o usuario deshabilitado
                Result.failure(Exception("Credenciales incorrectas o usuario inactivo"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
