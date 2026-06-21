package src

import java.io.File

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main(args : Array<String>) {

    val opcion = verificarEntrada(args)
    val raiz = generarPath(args)
    val resultado = manejarArbol(raiz, opcion, args)
    println(resultado)
}

fun verificarEntrada(args : Array<String>) : Int {
    if(args.isEmpty())
        throw IllegalArgumentException("[Main] Los argumentos de entrada están vacíos mani")

    if(args.first() == "ayuda" || args.first() == "help")
        throw IllegalArgumentException(
            "[Main] los comandos disponibles son:\n"
            + "TODO"
        )

    if(args.size == 1) //1 argumento, devuelve el arbol sencillo de readme, no el baseline del ultra sencillo
        return 1

    if(args[1] == "prueba")
        return -1

    if(args.size == 2 && args.contains("--ocultos"))
        return 2

    if(args.size == 3) //3 argumentos, devuelve la ubicación de un archivo, según la comparacion elegida
        return 3

    throw IllegalArgumentException("[Main] Los argumentos exceden la cantidad máxima")
}

fun generarPath(args : Array<String>) : File {
    val path = File(args.first())

    if(!path.exists())
        throw IllegalArgumentException("[Main] El path no existe cabrón")

    if(path.isFile)
        throw IllegalArgumentException("[Main] El path es un archivo, no un directorio")

    println("[Main] Se está imprimiendo la arquitectura de ${path.path}")

    return path
}

fun manejarArbol(raiz: File, opcion: Int, args : Array<String>) : String {
    val arbol = Arbol(raiz)
    arbol.crearSubDirectorios()
    arbol.nPalabraMasLarga()

    return "[Main] " + when(opcion) {
        1 -> {
            val profundidad = arbol.calcularProfundidad()
            val arquitectura = arbol.generarArquitectura(profundidad)
            val descripciones = arbol.organizarDescripciones()
        }

        2 -> {
            val profundidad = arbol.calcularProfundidad()
            val arquitectura = arbol.generarArquitectura(profundidad, true)
            val descripciones = arbol.organizarDescripciones()
        }

        3 -> {
            val busqueda = args[1]
            val condicionBusqueda = args[2]
            "Ubicación(es) de aparición: " + arbol.buscarArchivosPorNombre(busqueda, condicionBusqueda)
        }
        -1 ->{
            arbol.generarArquitecturaSencilla() + "\nDirectorio aplanado:" + arbol.organizarDescripciones()
        }
        else -> "[Main]No existe esta opción aún"
    }
}
