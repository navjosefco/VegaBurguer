package ies.sequeros.com.dam.pmdm.administrador.infraestructura.ficheros

import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import java.io.File

class FileCategoriaRepository(
    private val almacenDatos: AlmacenDatos,
    private val fileName: String = "categorias.json"
) : ICategoriaRepositorio {

    private val subdirectory = "/data/"

    init {

    }

    private suspend fun getDirectoryPath(): String {
        val dir = almacenDatos.getAppDataDir()
        val directory = File(dir, subdirectory)

        return directory.absolutePath
    }

    private suspend fun save(items: List<Categoria>) {
        if (!File(this.getDirectoryPath()).exists())
            File(this.getDirectoryPath()).mkdirs()
        this.almacenDatos.writeFile(this.getDirectoryPath() + "/" + this.fileName, Json.encodeToString(items))
    }

    override suspend fun add(item: Categoria) {
        val items = this.getAll().toMutableList()

        if (items.firstOrNull { it.name == item.name } == null) {
            items.add(item)
        } else {
            throw IllegalArgumentException("ALTA:La categoria con id:" + item.id + " ya existe")
        }
        this.save(items)
    }

    override suspend fun delete(item: Categoria): Boolean {
       return this.remove(item.id)
    }

    override suspend fun remove(id: String): Boolean {
        val items = this.getAll().toMutableList()
        var item = items.firstOrNull { it.id == id }
        if (item != null) {
            items.remove((item))
            this.save(items)
            return true
        } else {
            throw IllegalArgumentException(
                "BORRADO:" +
                        " La categoria con id:" + id + " NO  existe"
            )
        }
        return true
    }

    override suspend fun update(item: Categoria): Boolean {
        val items = this.getAll().toMutableList()
        val newItems = items.map { if (it.id == item.id) item else it }.toMutableList()
        this.save(newItems)
        return true
    }

    override suspend fun getAll(): List<Categoria> {
        val path = getDirectoryPath() + "/" + this.fileName
        val items = mutableListOf<Categoria>()
        var json = ""
        if (File(path).exists()) {
            json = almacenDatos.readFile(path)
            if (!json.isEmpty())
                items.addAll(Json.decodeFromString<List<Categoria>>(json))
        }
        return items.toList()
    }

    override suspend fun getByName(name: String): Categoria? {
        val elements = this.getAll()
        for (element in elements) {
            if (element.name == name)
                return element
        }
        return null;
    }

    override suspend fun getById(id: String): Categoria? {
        val elements = this.getAll()
        for (element in elements) {
            if (element.id == id)
                return element
        }
        return null;
    }
}
