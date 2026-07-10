package piramide.utils

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

fun escribirArchivo(contenido : String, path : File) : String{
    try{
        path.writeText(contenido)
    }catch (e : Exception){
        return "No se pudo escribir en el archivo. \n$e"
    }
    return "Contenido escrito exitosamente."
}

object Rutas{
    val raizProyecto : Path by lazy{
        val location = Rutas::class.java.protectionDomain.codeSource.location.toURI()
        var actual = Paths.get(location).toAbsolutePath()

        if(Files.isRegularFile(actual))
            actual = actual.parent

        else{
            while(actual != null && !Files.exists(actual.resolve("data")))
                actual = actual.parent
        }

        actual ?: throw IllegalStateException("[Rutas] No se pudo determinar la raíz del proyecto")
    }

    val carpetaPolicias : Path by lazy {
        raizProyecto.resolve("data").resolve("").also{
            Files.createDirectories(it)
        }
    }
}