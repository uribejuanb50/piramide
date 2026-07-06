package src

import src.arbol.Arbol
import src.utils.toCustomString
import java.io.File
import kotlin.system.exitProcess

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main(args : Array<String>) {
    val (flags, args) = validarFlags(args)
    val opcion = verificarEntrada(args)
    val raiz = generarPath(args)
    val resultado = manejarArbol(raiz, opcion, args, flags)
    println(resultado)
}




fun escribirArchivo(contenido : String, path : File) : String{
    try{
        path.writeText(contenido)
    }catch (e : Exception){
        return "No se pudo escribir en el archivo. \n$e"
    }
    return "Contenido escrito exitosamente."
}