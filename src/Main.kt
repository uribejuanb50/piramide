package src

import java.io.File
import kotlin.system.exitProcess

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main(args : Array<String>) {

    val opcion = verificarEntrada(args)
    val raiz = generarPath(args)
    val resultado = manejarArbol(raiz, opcion, args)
    println(resultado)
}



//pone los flags según lo que digan
fun validarFlags(args : Array<String>) : Pair<Map<String, Any?>, ArrayList<String>> {

    val opcionesValidas = listOf(
        "--escondidos", "--reversar", "--recortar", "--prueba", "--nivelMax", "--README", "--toArchivo", "--ayuda"
        )
    val nuevosArgs : ArrayList<String> = arrayListOf()
    val diccionarioFlags = mutableMapOf<String, Any?>(
        "escondidos" to false,
        "reversar" to false,
        "recortar" to false,
        "prueba" to true,
        "nivelMax" to null,
        "README" to false,
        "toArchivo" to null
    )

    for((indice, argumento) in args.withIndex()){

        if(argumento !in opcionesValidas) {
            nuevosArgs.add(argumento)
            continue
        }

        when(argumento){
            "--escondidos" -> diccionarioFlags["escondidos"] = true

            "--reversar" -> diccionarioFlags["reversar"] = true
            "--recortar" -> diccionarioFlags["recortar"] = true
            "--prueba" -> diccionarioFlags["prueba"] = true
            "--README" -> diccionarioFlags["README"] = true
            "--nivelMax" -> {
                val siguiente = args.getOrNull(indice + 1)?.toIntOrNull()

                if(siguiente == null) {
                    println("[Main] después de --nivelMax no había número")
                    exitProcess(1)
                }

                diccionarioFlags["nivelMax"] = siguiente
            }
            "--toArchivo" -> {
                val siguiente = args.getOrNull(indice + 1)

                if(siguiente == null){
                    println("[Main] Hubo un error al leer el archivo después de --toArchivo")
                    exitProcess(1)
                }

                val path = File(siguiente)
                if(!path.exists()){
                    println("[Main] el archivo a escribir no existe")
                    exitProcess(1)
                }

                diccionarioFlags["toArchivo"] = path
            }
            "--ayuda" -> {
                println(
                    """
                    flags:
                    --escondidos                //mostrar carpetas y archivos ocultos
                    --reversar                  //reversar las listas internas y mostrar primero archivos que directorios
                    --recortar                  //recorta palabras
                    --prueba                    //lanzar prueba
                    --nivelMax n                //hacerlo hasta el nivel max
                    --README                    //crear README
                    --toArchivo "path_archivo"  //guardar en archivo
                    --ayuda                     //Imprime este txt
                """.trimIndent()
                )
                exitProcess(1)
            }
        }


    }

    return Pair(diccionarioFlags, nuevosArgs)
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

    if(args[1] == "--prueba")
        return -1

    if(args.size == 2 && args.contains("--ocultos"))
        return 2

    if(args.size == 2 && args.contains("--eliminar"))
        return 4

    if(args.size == 3) //3 argumentos, devuelve la ubicación de un archivo, según la comparacion elegida
        return 3

    throw IllegalArgumentException("[Main] Los argumentos exceden la cantidad máxima")
}

fun generarPath(args : Array<String>) : File {
    val path = File(args.first())

    if(!path.exists())
        throw IllegalArgumentException("[Main] El path no existe cabrón")

    if(path.isFile && !args.contains("--prueba"))
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
            arbol.generarREADME(arquitectura, descripciones)
        }

        2 -> {
            val profundidad = arbol.calcularProfundidad()
            val arquitectura = arbol.generarArquitectura(profundidad, true)
            val descripciones = arbol.organizarDescripciones(true)
            arbol.generarREADME(arquitectura, descripciones)
        }

        3 -> {
            val busqueda = args[1]
            val condicionBusqueda = args[2]
            "Ubicación(es) de aparición: " + arbol.buscarArchivosPorNombre(busqueda, condicionBusqueda)
        }

        4 -> {
            //arbol.eliminarPalabra()
            ""
        }
        -1 ->{
            //arbol.eliminarPalabra()
        }
        else -> "[Main]No existe esta opción aún"
    }
}
