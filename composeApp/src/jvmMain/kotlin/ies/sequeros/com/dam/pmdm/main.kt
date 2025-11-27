package ies.sequeros.com.dam.pmdm

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.BBDDDependienteRepository
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.dependientes.BBDDRepositorioDependientesJava
import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection
import java.io.FileInputStream
import java.util.logging.LogManager

fun main() = application {

    //Aqui se inicializa la base de datos con los datos del archivo app.properties
    val connection = DataBaseConnection() // creo una instancia de la clase DataBaseConnection
    connection.config_path = "./app.properties" // le doy la ruta del archivo app.properties
    connection.open() // abro la conexion

    //Se pasa la conexion a la clase DependienteRepositorioJava 
    val dependienteRepositorioJava=BBDDRepositorioDependientesJava(connection)

    //Se inicializa el repositorio con la base de datos

    val dependienteRepositorio: IDependienteRepositorio = BBDDDependienteRepository(dependienteRepositorioJava )


    //Se inicializa el logging con el archivo logging.properties
    configureExternalLogging("./logging.properties")

    //Se inicializa la ventana
    Window(

        onCloseRequest = {
            //se cierra la conexion
            connection.close()
            exitApplication()},
        title = "VegaBurguer",
    ) {
        //se envuelve el repositorio en java en uno que exista en Kotlin
        App(dependienteRepositorio,AlmacenDatos())
    }
}

//Se configura el logging
fun configureExternalLogging(path: String) {

    try {

        FileInputStream(path).use { fis ->
            LogManager.getLogManager().readConfiguration(fis)
            println("Logging configurado desde: $path")
        }

    } catch (e: Exception) {
        
        println("⚠️ No se pudo cargar logging.properties externo: $path")
        e.printStackTrace()
    }
}