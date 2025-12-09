package ies.sequeros.com.dam.pmdm.administrador.aplicacion.login

import ies.sequeros.com.dam.pmdm.administrador.modelo.Dependiente

object Sesion {
    var usuarioActual: Dependiente? = null
        private set

    fun iniciar(usuario: Dependiente) {
        usuarioActual = usuario
    }

    fun cerrar() {
        usuarioActual = null
    }

    fun estaIniciada(): Boolean {
        return usuarioActual != null
    }

    fun esAdmin(): Boolean {
        return usuarioActual?.isAdmin == true
    }
}
