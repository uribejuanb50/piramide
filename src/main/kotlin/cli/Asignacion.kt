package piramide.cli

import piramide.arbol.Arbol
import piramide.policias.negocio.GestorPolicias
import piramide.utils.escribirArchivo
import piramide.utils.toCustomString
import java.io.File
import java.nio.file.Path

class Asignacion(
    val procesamiento : Procesamiento,
    val gestorPolicias: GestorPolicias
) {

    fun asignarCategoria(
        tarea : Pair<String, Int>, //llega como arbol y la opcion, o policia y la opcion
        raiz: File,
        args : ArrayList<String>,
        flagsExploracion : Map<String, Any?>,
        flagsPolicias: Map<String, Any?>
    ){
        when(tarea.first){
            "arbol" -> manejarArbol(raiz, tarea.second, args, flagsExploracion)
            "policia" -> manejarPolicia(tarea.second, args, flagsPolicias)
        }
    }

    fun generarPathArbol() : File {
        val pathArbolActual =
            gestorPolicias.policiaArbol.pathRaizArbolUsando ?:
            throw IllegalStateException("[Asignacion] No hay arboles disponibles")
        return pathArbolActual.toFile()
    }

    //usar las funciones específicas integrando los flags
    fun manejarArbol(raiz: File, opcion: Int, args : ArrayList<String>, flags : Map<String, Any?>) : String {
        val arbol = Arbol(raiz)
        if(args.size != 2){
            arbol.crearSubDirectorios()
            arbol.nPalabraMasLarga()
        }

        val prueba = flags["prueba"] as? Boolean ?: false
        if(prueba)
            println("prueba")//lanzar prueba (hacer)

        val reversar = flags["reversar"] as? Boolean ?: false
        if(reversar && args.size != 2)
            arbol.reversarListas()

        val simple = flags["simple"] as? Boolean ?: false
        val caracterEspacio = flags["caracterEspacio"] as String
        val nRepeticiones = flags["nRepeticiones"] as Int
        val ocultos = flags["ocultos"] as? Boolean ?: false
        val modulos = flags["modulos"] as? Boolean ?: false
        val readMe = flags["README"] as? Boolean ?: false
        val recortar = flags["recortar"] as? Int
        val nivelMax = flags["nivelMax"] as? Int
        val toArchivo = flags["toArchivo"] as? File

        val descripcion = flags["descripcion"] as? String
        val noReasignar = flags["noReasignar"] as? Boolean ?: false

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

        return "[Asignacion] " + when(opcion) {
            10 -> { //toca cambiar según el arbol asignado para que solo sea $> arbol
                println(warningBusqueda)

                val arquitectura =
                    if(simple)
                        arbol.generarArquitecturaSencilla(caracterEspacio, nRepeticiones, ocultos)
                    else
                        arbol.generarArquitectura(profundidad, ocultos, nivelMax)

                println("arquitectura\n$arquitectura")

                val retorno = procesamiento.procesarFlags(arbol, caracterEspacio, nRepeticiones, ocultos, arquitectura, recortar, readMe, modulos, simple)

                if(toArchivo != null && profundidad > 0)
                    escribirArchivo(retorno, toArchivo)
                else
                    println(
                        "[Main] La profundidad es 0, no se imprimirá nada, revisa si la carpeta raíz está escondida\n" +
                        "       El path es ${raiz.absolutePath}"
                    )
                "\n" + retorno
            }

            20 -> {
                val pathNuevoArbol = File(args[1])

                if(!pathNuevoArbol.exists())
                    throw IllegalArgumentException("El path no existe cabrón")

                gestorPolicias.policiaArbol.registrarArbol(pathNuevoArbol.toPath(), descripcion, !noReasignar)

                "Árbol ${args[1]} incluido correctamente."
            }

            30 -> {
                println(warningExploracion)

                if(!force && ocultos) {
                    println("[Main] ALERTA! --ocultos no es un candidato para esta operación por temas de seguridad")
                    println("       Usa la etiqueta --force")
                }
                val palabraEliminar = args[2] //desde el tres porque el args[1] es el que valida la función
                arbol.eliminarPalabra(palabraEliminar, nivelMax, (force && ocultos))
            }

            31 -> {
                println(warningExploracion)

                val palabraBuscar = args[2] //lomismo del caso anterior
                arbol.buscarArchivosPorNombre(palabraBuscar, condicion)
            }
            40 -> {
                println(warningExploracion)

                val palabraAntigua = args[2]
                val palabraNueva = args[3]
            }
            else -> {
                System.err.println("¿Cómo llegaste aquí? La cagué re duro en algo")
            }
        }
    }

    fun manejarPolicia(opcion: Int, args: ArrayList<String>, flags : Map<String, Any?>) : String{

        val excluir = (flags["excluir"] as? List<*>)?.let { lista ->
            ArrayList(lista.map { it as Path })}

        return "[Asignacion] " + when (opcion) {
            20 ->{
                TODO()
            }
            else->{
                System.err.println("¿Cómo llegaste aquí? La cagué re duro en algo")
            }
        }
    }

}