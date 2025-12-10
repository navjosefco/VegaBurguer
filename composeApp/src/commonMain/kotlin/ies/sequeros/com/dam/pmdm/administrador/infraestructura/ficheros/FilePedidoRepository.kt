package ies.sequeros.com.dam.pmdm.administrador.infraestructura.ficheros

import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import java.io.File

class FilePedidoRepository(
    private val almacenDatos: AlmacenDatos,
    private val fileName: String = "pedidos.json"
) : IPedidoRepositorio {

    private val subdirectory = "/data/"

    init {

    }

    private suspend fun getDirectoryPath(): String {
        val dir = almacenDatos.getAppDataDir()
        val directory = File(dir, subdirectory)

        return directory.absolutePath
    }

    private suspend fun save(items: List<Pedido>) {
        if (!File(this.getDirectoryPath()).exists())
            File(this.getDirectoryPath()).mkdirs()
        this.almacenDatos.writeFile(this.getDirectoryPath() + "/" + this.fileName, Json.encodeToString(items))
    }

    override suspend fun getAll(): List<Pedido> {
        val path = getDirectoryPath() + "/" + this.fileName
        val items = mutableListOf<Pedido>()
        var json = ""
        if (File(path).exists()) {
            json = almacenDatos.readFile(path)
            if (!json.isEmpty())
                try {
                    items.addAll(Json.decodeFromString<List<Pedido>>(json))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
        }
        return items.toList()
    }

    override suspend fun add(item: Pedido) {
        val items = this.getAll().toMutableList()

        if (items.firstOrNull { it.id == item.id } == null) {
            items.add(item)
        } else {
            throw IllegalArgumentException("ALTA:El pedido con id:" + item.id + " ya existe")
        }
        this.save(items)
    }

    override suspend fun delete(item: Pedido): Boolean {
        val items = this.getAll().toMutableList()
        var found = items.firstOrNull { it.id == item.id }
        if (found != null) {
            items.remove(found)
            this.save(items)
            return true
        } else {
            throw IllegalArgumentException(
                "BORRADO:" +
                        " El pedido con id:" + item.id + " NO  existe"
            )
        }
        return true
    }

    override suspend fun update(item: Pedido): Boolean {
        val items = this.getAll().toMutableList()
        val newItems = items.map { if (it.id == item.id) item else it }.toMutableList()
        this.save(newItems)
        return true
    }

    override suspend fun getById(id: String): Pedido? {
        val elements = this.getAll()
        for (element in elements) {
            if (element.id == id)
                return element
        }
        return null;
    }

    override suspend fun getByDependiente(dependienteId: String): List<Pedido> {
        val elements = this.getAll()
        val result = mutableListOf<Pedido>()
        for (element in elements) {
            if (element.dependienteId == dependienteId)
                result.add(element)
        }
        return result
    }
}
