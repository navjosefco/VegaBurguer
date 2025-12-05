package ies.sequeros.com.dam.pmdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.memoria.FileDependienteRepository
import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //se crea el almacen para el json
        val almacenDatos:AlmacenDatos=  AlmacenDatos(this)
        //se le pasa al repositorio
        val dependienteRepositorio: IDependienteRepositorio =
            FileDependienteRepository(almacenDatos)

        val productoRepositorio = object : ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio {
            override suspend fun getAll(): List<ies.sequeros.com.dam.pmdm.administrador.modelo.Producto> = emptyList()
            override suspend fun getById(id: String): ies.sequeros.com.dam.pmdm.administrador.modelo.Producto? = null
             override suspend fun add(item: ies.sequeros.com.dam.pmdm.administrador.modelo.Producto) {}
             override suspend fun update(item: ies.sequeros.com.dam.pmdm.administrador.modelo.Producto): Boolean = true
             override suspend fun delete(item: ies.sequeros.com.dam.pmdm.administrador.modelo.Producto): Boolean = true
             override suspend fun remove(id: String): Boolean = true
             override suspend fun getByCategoria(idCategoria: String): List<ies.sequeros.com.dam.pmdm.administrador.modelo.Producto> = emptyList()
             override suspend fun getByName(name: String): ies.sequeros.com.dam.pmdm.administrador.modelo.Producto? = null
        }

        val categoriaRepositorio = object : ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio {
            override suspend fun getAll(): List<ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria> = emptyList()
            override suspend fun getById(id: String): ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria? = null
            override suspend fun add(item: ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria) {}
            override suspend fun update(item: ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria): Boolean = true
            override suspend fun remove(id: String): Boolean = true
            override suspend fun delete(item: ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria): Boolean = true
            override suspend fun getByName(name: String): ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria? = null
        }

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            //se crean almacenes de datos y de imagenes propias de la plataforma y se
            //pasan a la aplicación,
            val almacenImagenes:AlmacenDatos=  AlmacenDatos(this)

            App(dependienteRepositorio, productoRepositorio, categoriaRepositorio, almacenImagenes)
        }
    }
}

