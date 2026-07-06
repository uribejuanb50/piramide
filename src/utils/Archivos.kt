package src.utils

import java.io.File

fun escribirArchivo(contenido : String, path : File) : String{
    try{
        path.writeText(contenido)
    }catch (e : Exception){
        return "No se pudo escribir en el archivo. \n$e"
    }
    return "Contenido escrito exitosamente."
}