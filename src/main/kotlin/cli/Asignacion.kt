package piramide.cli

import piramide.arbol.Arbol
import piramide.utils.escribirArchivo
import piramide.utils.toCustomString
import java.io.File

class Asignacion(
    val procesamiento : Procesamiento
) {
    fun asignarCategoria(
        tarea : Pair<String, Int>,
        raiz: File, opcion: Int,
        args : ArrayList<String>,
        flags : Map<String, Any?>
    ){
        when(tarea.first){
            "arbol"
        }
    }

    //generar el path del primer argumento
    fun generarPath(args : ArrayList<String>) : File {
        val path = File(args.first())

        if(!path.exists())
            throw IllegalArgumentException("[Main] El path no existe cabrón")

        if(path.isFile && !args.contains("--prueba"))
            throw IllegalArgumentException("[Main] El path es un archivo, no un directorio")

        println("[Main] Ubicación de la arquitectura: ${path.path}")

        return path
    }

    //usar las funciones específicas integrando los flags
    fun manejarArbol(raiz: File, opcion: Int, args : ArrayList<String>, flags : Map<String, Any?>) : String {
        val arbol = Arbol(raiz)
        arbol.crearSubDirectorios()
        arbol.nPalabraMasLarga()

        val prueba = flags["prueba"] as? Boolean ?: false
        if(prueba)
            println("prueba")//lanzar prueba (hacer)

        val reversar = flags["reversar"] as? Boolean ?: false
        if(reversar)
            arbol.reversarListas()

        val simple = flags["simple"] as? Boolean ?: false
        val caracterEspacio = flags["caracterEspacio"] as String
        val nRepeticiones = flags["nRepeticiones"] as Int
        val ocultos = flags["ocultos"] as? Boolean ?: false
        val descripcion = flags["descripcion"] as? Boolean ?: false
        val readMe = flags["README"] as? Boolean ?: false
        val recortar = flags["recortar"] as? Int
        val nivelMax = flags["nivelMax"] as? Int
        val toArchivo = flags["toArchivo"] as? File

        val condicion = flags["condicion"] as? String
        val force = flags["force"] as? Boolean ?: false
        //val ocultosBusqueda = force && ocultos  //por defecto no se debería eliminar palabras de carpetas ocultas, ya que suelen ser bibliotecas
        //y por el estilo

        val mapaExploracion : Map<String, Any?> = mapOf(
            "--README" to readMe,
            "--recortar" to recortar,
            "--toArchivo" to toArchivo?.name
        )
        val mapaBusqueda : Map<String, Any?> = mapOf(
            "--condicion" to condicion,
            "--force" to force
        )

        //el custom devuelve primero el string de operaciones que son contrarias a "negativas" (nulls o falses)
        //y el second es la cantidad de negativas que aparecen, si hay uno o más tira el warning
        val warningExploracion =
            if(mapaExploracion.toCustomString().second > 0) {
                "[Main] ALERTA!! este(os) flag(s) ${mapaExploracion.toCustomString().first} no son válidos para esta operación"
            } else ""
        val warningBusqueda =
            if(mapaBusqueda.toCustomString().second > 0) {
                "[Main] ALERTA!! este(os) flag(s) ${mapaBusqueda.toCustomString().first} no son válidos para esta operación"
            } else ""

        val profundidad = arbol.calcularProfundidad()

        return "[Main] " + when(opcion) {
            1 -> {
                println(warningBusqueda)

                val arquitectura =
                    if(simple)
                        arbol.generarArquitecturaSencilla(caracterEspacio, nRepeticiones, ocultos)
                    else
                        arbol.generarArquitectura(profundidad, ocultos, nivelMax)

                val retorno = procesamiento.procesarFlags(arbol, caracterEspacio, nRepeticiones, ocultos, arquitectura, recortar, readMe, descripcion, simple)

                if(toArchivo != null && profundidad > 0)
                    escribirArchivo(retorno, toArchivo)
                else
                    println("[Main] La profundidad es 0, no se imprimirá nada, revisa si la carpeta raíz está escondida")
                "\n" + retorno
            }

            3 -> {
                println(warningExploracion)

                if(!force && ocultos) {
                    println("[Main] ALERTA! --ocultos no es un candidato para esta operación por temas de seguridad")
                    println("       Usa la etiqueta --force")
                }
                val palabraEliminar = args[2] //desde el tres porque el args[1] es el que valida la función
                arbol.eliminarPalabra(palabraEliminar, nivelMax, (force && ocultos))
            }

            4 -> {
                println(warningExploracion)

                val palabraBuscar = args[2] //lomismo del caso anterior
                arbol.buscarArchivosPorNombre(palabraBuscar, condicion)
            }
            5 -> {
                println(warningExploracion)

                val palabraAntigua = args[2]
                val palabraNueva = args[3]
            }
            else -> {
                System.err.println("¿Cómo llegaste aquí? La cagué re duro en algo")
            }
        }
    }

    fun manejarPolicia(){}

}