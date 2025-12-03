package ies.sequeros.com.dam.pmdm.dependientes.cambiarclave

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.cambiaclave.CambiarClaveDependienteUseCase
import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test


//Documentacion : https://cursokotlin.com/testing-en-android-test-unitarios/

/*¿Qué es un mock?
Un Mock es básicamente un objeto falso de una clase.
Con él podemos trucar o alterar el resultado de funciones para poder testear lo que nos interese. Imaginemos que tenemos una clase la cual llama a
otra para devolverle un true o un false. Si nosotros mockeamos esa clase podemos decirle antes de cada test que queremos que nos devuelva,
si un true o un false.*/


class CambiarClaveDependienteUserCaseTest{

    //Simulamos las dependencias
    private val repositorio: IDependienteRepositorio = mockk()
    private val almacenDatos: AlmacenDatos = mockk()

    //Traemos la clase que vamos a probar
    private val useCase = CambiarClaveDependienteUseCase(repositorio, almacenDatos)

    @Test
    fun cambiarContrasenyaText() = runTest {

        val id = "user123"
        val oldPass = "1234"
        val newPass = "Abcd1234"

        // Given: Configuramos el mock para que no haga nada (Unit) cuando le llamen
        coEvery { repositorio.cambiarContrasenya(any(), any(), any()) } returns Unit

        // WHEN (Cuando)
        useCase.invoke(id, oldPass, newPass)

        // THEN (Entonces)
        // Verificamos que se llamó al método exacto con los parámetros exactos
        coVerify(exactly = 1) {
            repositorio.cambiarContrasenya(id, oldPass, newPass)
        }
    }
}