package piramide.utils

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

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

        println("[Archivos] actual, primera instancia: ${actual.absolutePathString()}")

        if(Files.isRegularFile(actual))
            actual = actual.parent.also { println("[Archivos] Actual.parent: ${it.absolutePathString()}") }


        else{
            while(actual != null && !Files.exists(actual.resolve("data")))
                actual = actual.parent.also { println("[Archivos] Actual.parent dentro del while: ${it.absolutePathString()}") }
        }

        actual ?: throw IllegalStateException("[Rutas] No se pudo determinar la raíz del proyecto")
    }

    val carpetaPolicias : Path by lazy {
        println("[Archivos] raizProyecto: ${raizProyecto.absolutePathString()}")
        raizProyecto.resolve("data").resolve("policias").resolve("").also{
            Files.createDirectories(it)
        }
    }

    val directorioInvocacion by lazy{
        Path.of(System.getProperty("user.dir")).toAbsolutePath()
    }
}