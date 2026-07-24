package piramide.cli

import piramide.arbol.Arbol
import piramide.policias.dominio.PoliciaReemplazados
import piramide.policias.negocio.GestorPolicias
import piramide.utils.Rutas
import piramide.utils.escribirArchivo
import piramide.utils.toCustomString
import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString

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
    ) : String {
        return when(tarea.first){
            "arbol" -> manejarArbol(raiz, tarea.second, args, flagsExploracion)
            "policia" -> manejarPolicia(tarea.second, args, flagsPolicias)
            else -> throw IllegalArgumentException("[Asignacion] Los únicos candidatos de llamado después de piramid son arbol o policia")
        }
    }

    fun generarPathArbol() : File {
        val pathArbolActual =
            gestorPolicias.policiaArbol.pathRaizArbolUsando
                ?: Rutas.directorioInvocacion
                    .also { println("[Asignacion] Sin arboles previos ruta default generada en la ubicación donde me ves") }
        return pathArbolActual.toFile()
    }

    //usar las funciones específicas integrando los flags
    fun manejarArbol(raiz: File, opcion: Int, args : ArrayList<String>, flags : Map<String, Any?>) : String {
        val arbol = Arbol(raiz, gestorPolicias)
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
        val regex = flags["regex"] as? Boolean ?: false
        val y = flags["y"] as? Boolean ?: false

        val mapaExploracion : Map<String, Any?> = mapOf(
            "--README" to readMe,
            "--recortar" to recortar,
            "--toArchivo" to toArchivo?.name
        )
        val mapaBusqueda : Map<String, Any?> = mapOf(
            "--condicion" to condicion,
            "--force" to force,
            "--regex" to regex
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

                //println("arquitectura\n$arquitectura")

                val retorno = procesamiento.procesarFlags(arbol, caracterEspacio, nRepeticiones, ocultos, arquitectura, recortar, readMe, modulos, simple)

                if(toArchivo != null && profundidad > 0)
                    escribirArchivo(retorno, toArchivo)
                else
                    if(toArchivo != null)
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

            21 -> {
                val pathnuevoArbol = Rutas.directorioInvocacion

                gestorPolicias.policiaArbol.registrarArbol(pathnuevoArbol, descripcion, !noReasignar)

                "Árbol ${pathnuevoArbol.absolutePathString()} incluido correctamente."
            }

            /*30 -> { Funcio descontinuada, reemplazada por reemplazar, borrar literalmente es cambiar por ""
                println(warningExploracion)

                if(!force && ocultos) {
                    println("[Main] ALERTA! --ocultos no es un candidato para esta operación por temas de seguridad")
                    println("       Usa la etiqueta --force")
                }
                val palabraEliminar = args[2] //desde el tres porque el args[1] es el que valida la función
                //arbol.eliminarPalabra(palabraEliminar, nivelMax, (force && ocultos))
            }*/

            31 -> {
                println(warningExploracion)

                val palabraBuscar = args[2] //lomismo del caso anterior
                arbol.buscarArchivosPorNombre(palabraBuscar, condicion)
            }
            40 -> {
                println(warningExploracion)

                val palabraAntigua =
                    if(regex)
                        args[2].toRegex()
                    else
                        procesamiento.construirPatronConLimite(args[2])
                val palabrareemplazo = args[3]

                if(!y){
                    val continuar: Boolean =
                        generateSequence {
                            print("El arbol donde ocurrirá la operacion es ${raiz.absolutePath} (y/n): "); readlnOrNull()?.trim()?.lowercase()
                        }
                        .filter { it == "y" || it == "n" }
                        .map { it == "y" }
                        .first()
                    if(!continuar)
                        return "Secuencia abortada"
                }

                if(!force && ocultos) {
                    println("[Main] ALERTA! --ocultos no es un candidato para esta operación por temas de seguridad")
                    println("       Usa la etiqueta --force")
                }

                val policiaReemplazados = gestorPolicias.devolverPolicia("reemplazados") as PoliciaReemplazados

                arbol.eliminarPalabra(palabraAntigua, palabrareemplazo, nivelMax, (force && ocultos), policiaReemplazados)
            }
            else -> {
                System.err.println("¿Cómo llegaste aquí? La cagué re duro en algo")
            }
        }
    }

    fun manejarPolicia(opcion: Int, args: ArrayList<String>, flags : Map<String, Any?>) : String{

        val excluir = (flags["excluir"] as? List<*>)?.map{ it as Path }?.toSet()

        val solo = (flags["solo"] as? List<*>)?.let { lista ->
            ArrayList(lista.map { it as String })
        }

        val listaParejaFiltrar = (flags["filtrar"] as? List<*>)?.mapNotNull { item ->
            (item as? Pair<*, *>)?.let { par ->
                val clave = par.first
                val valor = par.second as? String
                if (clave != null && valor != null) clave to valor else null
            }
        }?.let { ArrayList(it) } //Falta la opcion de filtrar por descripcion > --filtrar descripcion str

        val ids = (flags["ids"] as? List<*>)?.let{ lista ->
            ArrayList(lista.map{it as Long})
        }

        println("listaIds $ids")
        return "[Asignacion] " + when (opcion) {
            20 ->{
                gestorPolicias.listarRegistros(solo, listaParejaFiltrar)
            }
            21 ->{
                gestorPolicias.origenArbol()
            }
            41 -> {
                val id : Long = args[3]
                    .toLongOrNull() ?:
                    throw IllegalArgumentException("[Asignacion] El argumento después de asignar no era un número entero")

                gestorPolicias.actualizarOrigenArbol(Pair(id, "id"))

                "Arbol asignado con el path ${gestorPolicias.origenArbol()}"
            }
            42 -> {
                "Del policia arbol " +
                        gestorPolicias.policiaArbol.id +
                        " se eliminaron los registros: " +
                        gestorPolicias.policiaArbol.eliminarRegistros(ids, setOf()) //vacio el set
            }
            else->{
                System.err.println("¿Cómo llegaste aquí? La cagué re duro en algo")
            }
        }
    }

}