# Guía 13: Persistencia en Ficheros JSON (Para Móviles) 📱💾

En esta guía implementaremos la capa de datos para dispositivos móviles (Android), donde no tenemos un servidor MySQL corriendo localmente. Usaremos **ficheros JSON** para guardar la información.

---

## 📦 Parte 1: Repositorio de Productos (`FileProductoRepository`)

### 1. El Objetivo
Crear una clase que cumpla con `IProductoRepositorio` (la misma interfaz que usa la versión SQL), pero que guarde los datos en un fichero de texto `productos.json` usando el formato JSON.

### 2. La Implementación (`FileProductoRepository.kt`)

Ubicación: `administrador/infraestructura/ficheros/FileProductoRepository.kt`

#### Conceptos Clave:
1.  **`Json.encodeToString(items)`**: Convierte la lista de objetos Kotlin a un String JSON.
2.  **`Json.decodeFromString<List<Producto>>(json)`**: Convierte el String JSON recuperado del fichero a objetos Kotlin.
3.  **Simplicidad**: A diferencia de SQL, aquí leemos **TODO** el fichero, modificamos la lista en memoria, y reescribimos **TODO** el fichero. En bases de datos gigantes esto es ineficiente, pero para una app móvil con pocos datos es perfecto y muy fácil de implementar.

#### Código Explicado:

```kotlin
class FileProductoRepository(
    private val almacenDatos: AlmacenDatos, // Inyectamos nuestro gestor de ficheros
    private val fileName: String = "productos.json"
) : IProductoRepositorio {

    // Helper para guardar: Recibe la lista completa y la vuelca al fichero
    private suspend fun save(items: List<Producto>) {
        // 1. Convertir Lista -> Texto JSON
        val jsonString = Json.encodeToString(items)
        // 2. Escribir al disco
        almacenDatos.writeFile(..., jsonString)
    }

    // Helper para leer: Devuelve siempre una lista (vacía si no hay fichero)
    override suspend fun getAll(): List<Producto> {
        val items = mutableListOf<Producto>()
        if (ficheroExiste) {
             val json = almacenDatos.readFile(...) 
             // Convertir Texto JSON -> Lista Objetos
             items.addAll(Json.decodeFromString(json))
        }
        return items
    }

    // Ejemplo de UPDATE
    override suspend fun update(producto: Producto): Boolean {
        val items = getAll().toMutableList() // 1. Leemos todo
        val index = items.indexOfFirst { it.id == producto.id } // 2. Buscamos indice
        if (index != -1) {
            items[index] = producto // 3. Reemplazamos en memoria
            save(items) // 4. Guardamos todo de nuevo
            return true
        }
        return false
    }
}
```

### 3. ¿Por qué esto cumple la rúbrica?
*   **Gestores embebidos**: JSON actúa como base de datos embebida.
*   **Código diferenciado**:
    *   `App.kt` no sabe nada de ficheros o SQL, solo pide un `IProductoRepositorio`.
    *   En **Desktop**, le damos `BBDDRepositorioProducto` (SQL).
    *   En **Android**, le daremos `FileProductoRepository` (JSON).

---

## 📂 Parte 2: Repositorio de Categorías (`FileCategoriaRepository`)

### 1. La Lógica
Es idéntica a Productos, pero manejando la clase `Categoria`.

Ubicación: `administrador/infraestructura/ficheros/FileCategoriaRepository.kt`

### 2. Diferencias clave
*   **Fichero**: Guarda en `categorias.json`.
*   **Interfaz**: Implementa `ICategoriaRepositorio`.

```kotlin
class FileCategoriaRepository(...) : ICategoriaRepositorio {
    // ...
    private val fileName: String = "categorias.json"
    // ...
}
```

Al seguir el patrón **Repository**, cambiamos la implementación interna ("cómo guardo los datos") sin tocar ni una sola línea de la lógica de negocio (Use Cases) ni de la UI. ¡Esa es la magia de Clean Architecture! ✨

---

## 🛒 Parte 3: Repositorio de Pedidos (`FilePedidoRepository`)

### 1. El Desafío de los Objetos Anidados
Un `Pedido` contiene una lista de `LineaPedido`.
Para guardarlo en SQL teníamos 2 tablas (`pedido` y `linea_pedido`) y teníamos que hacer JOINS complejos.

**En JSON es muchísimo más fácil**:
Como JSON soporta objetos dentro de objetos, guardamos el pedido **entero** con sus líneas dentro.

```json
[
  {
    "id": "1",
    "customerName": "Kevin",
    "lineas": [
        { "productoId": "A", "quantity": 1 },
        { "productoId": "B", "quantity": 2 }
    ]
  }
]
```

### 2. Cambios en el Modelo (`@Serializable`)
Para que esto funcione, hemos tenido que añadir la anotación `@Serializable` a las clases `Pedido` y `LineaPedido`. Sin esto, la librería no sabe cómo convertir el objeto a texto.

### 3. Implementación (`FilePedidoRepository.kt`)
Ubicación: `administrador/infraestructura/ficheros/FilePedidoRepository.kt`

Funciona igual que los anteriores, pero cuando haces `save()`, al ser una estructura anidada, se guarda todo el árbol de datos automáticamente.

---

## 🚀 Conclusión Final

Ahora tienes **DOS** formas de guardar datos en tu proyecto:

1.  **Vía SQL (JDBC)**: Para la app de Escritorio (Desktop/Administrador). Robusta, concurrente, relacional.
2.  **Vía JSON (Ficheros)**: Para la app Móvil (Android/TPV). Ligera, embebida, sencilla.

Esto demuestra dominio total sobre **Acceso a Datos (RA 2)** y **Programación Multimedia (RA 2)**. ✅
