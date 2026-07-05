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


//pone los flags según lo que digan
fun validarFlags(args : Array<String>) : Pair<Map<String, Any?>, ArrayList<String>> {

    val opcionesValidas = listOf(
        //Caso base explorar/ver arbol
        "--simple","--ocultos", "--reversar", "--recortar",
        "--prueba", "--nivelMax", "--README", "--toArchivo",
        "--ayuda", "--descripcion",

        //Caso búsquedas
        "--condicion", "--force"
        )

    val nuevosArgs : ArrayList<String> = arrayListOf()
    val argsRestar : ArrayList<String> = arrayListOf()

    val diccionarioFlags = mutableMapOf<String, Any?>(
        "simple" to false,
        "caracterEspacio" to " ",
        "nRepeticiones" to 3,
        "ocultos" to false,
        "reversar" to false,
        "recortar" to null,
        "prueba" to false,
        "nivelMax" to null,
        "README" to false,
        "toArchivo" to null,
        "descripcion" to false,
        "condicion" to null,
        "force" to false
    )

    for((indice, argumento) in args.withIndex()){

        if(argumento !in opcionesValidas) {
            nuevosArgs.add(argumento)
            continue
        }

        when(argumento){
            "--ocultos" -> diccionarioFlags["ocultos"] = true
            "--reversar" -> diccionarioFlags["reversar"] = true
            "--prueba" -> diccionarioFlags["prueba"] = true
            "--force" -> diccionarioFlags["force"] = true
            "--simple" -> {
                val siguiente = args.getOrNull(indice + 1)

                if(siguiente != null && siguiente.length == 1){
                    argsRestar.add(siguiente)
                    diccionarioFlags["caracterEspacio"] = siguiente

                    val siguientesiguiente = args.getOrNull(indice + 2)?.toIntOrNull()

                    if(siguientesiguiente != null){
                        argsRestar.add(siguientesiguiente.toString())
                        diccionarioFlags["nRepeticiones"] = siguientesiguiente
                    }
                }

                diccionarioFlags["simple"] = true
            }
            "--condicion" -> {
                val siguiente = args.getOrNull(indice + 1)

                if(siguiente == null){
                    System.err.println("[Main] Después de --condicion no había palabra")
                    exitProcess(1)
                }

                diccionarioFlags["condicion"] = siguiente
                argsRestar.add(siguiente)
            }
            "--README" -> {
                val siguiente = args.getOrNull(indice + 1)
                diccionarioFlags["README"] = true

                if(siguiente == "desc") {
                    diccionarioFlags["descripcion"] = true
                    argsRestar.add(siguiente)
                }

            }
            "--recortar" -> {
                val siguiente = args.getOrNull(indice + 1)?.toIntOrNull()
                val siguienteEliminar = args.getOrNull(indice + 1)  //este es para eliminar el numero de los args
                                                                            //para que no de problemas de cantidad de args y se meta en
                                                                            //otras opciones

                if(siguiente == null || siguienteEliminar == null) {
                    System.err.println("[Main] Después de --recortar no había número")
                    exitProcess(1)
                }

                diccionarioFlags["recortar"] = siguiente
                argsRestar.add(siguienteEliminar)
            }
            "--nivelMax" -> {
                val siguiente = args.getOrNull(indice + 1)?.toIntOrNull()
                val siguienteEliminar = args.getOrNull(indice + 1)  //este es para eliminar el numero de los args
                                                                            //para que no de problemas de cantidad de args y se meta en
                                                                            //otras opciones

                if(siguiente == null || siguienteEliminar == null) {
                    System.err.println("[Main] después de --nivelMax no había número")
                    exitProcess(1)
                }

                diccionarioFlags["nivelMax"] = siguiente
                argsRestar.add(siguienteEliminar)
            }
            "--toArchivo" -> {
                val siguiente = args.getOrNull(indice + 1)
                val siguienteEliminar = args.getOrNull(indice + 1)  //este es para eliminar el numero de los args
                                                                            //para que no de problemas de cantidad de args y se meta en
                                                                            //otras opciones

                if(siguiente == null || siguienteEliminar == null) {
                    System.err.println("[Main] Hubo un error al leer el archivo después de --toArchivo")
                    exitProcess(1)
                }

                val path = File(siguiente)
                if(!path.exists() && !path.isFile){
                    System.err.println("[Main] el path a escribir no existe o no es un archivo")
                    exitProcess(1)
                }

                diccionarioFlags["toArchivo"] = path
                argsRestar.add(siguiente)
            }
            "--ayuda" -> {
                println(
                    """
                    
                    flags: (entre [] caracteres opcionales) 
                    --simple [caracter] [n]    //imprime el arbol sin caracteres especiales, puedes colocar el char que quieras y que tanto se repiten
                    --ocultos                  //mostrar carpetas y archivos ocultos
                    --reversar                 //reversar las listas internas y mostrar primero archivos que directorios
                    --recortar n               //recorta palabras hasta ese tamaño (falta añadir compatibilidad con --simple usar regex distintos)
                    --prueba                   //lanzar prueba
                    --nivelMax n               //hacerlo hasta el nivel max (0 es nada, 1 es la raiz, 2 el segundo nivel ...)
                    --README [desc]            //crear README (desc es opcional para activar la descripcion) (mejorar integracion con readmes ya existentes)
                    --toArchivo "path_archivo" //guardar en archivo (borra el contenido)
                    --ayuda                    //Imprime este txt
                    
                    comandos:
                    ...main.jar "path_original" //genera un arbol
                    ...main.jar "path_original" "borrar" "palabra" 				            //elimina esa palabra de todo el arbol (falta implementar)
                    ...main.jar "path_original" "buscar" "palabra" --condicion "condicion" 	//busca archivo que contenga esa palabra
                    						        		                                //lo busca exacto o conteniendo, tiene que tener un flag, o por default
                    									                                    // sale exacto
                    ...main.jar "path_original" "reemplazar" "palabraVieja" "palabraNueva"  //reemplaza palabra dentro de cada archivo
                    
                    compilar:
                    kotlinc *.kt -include-runtime -d main.jar
                """.trimIndent()
                )
                exitProcess(1)
            }
        }


    }

    val iniciarRestar = 0
    var iterador = 0
    while (iterador <= nuevosArgs.size){
        if(argsRestar.isEmpty())
            break

        if(nuevosArgs[iterador] == argsRestar[iniciarRestar]){
            nuevosArgs.removeAt(iterador)
            argsRestar.removeAt(iniciarRestar)
        }
        else
            iterador++
    }

    return Pair(diccionarioFlags, nuevosArgs)
}

//según la linea de comandos que entre, revisar qué caso es y asignar un número de función
fun verificarEntrada(args: ArrayList<String>) : Int{
    if(args.isEmpty()){
        System.err.println("[Main] los argumentos están vacíos.")
        exitProcess(1)
    }

    return when{
        //un argumento, simplemente el arbol
        args.size == 1 -> 1
        args.size == 2 -> 2
        (args.size == 3) && (args[1] == "borrar") -> 3
        (args.size == 3) && (args[1] == "buscar") -> 4
        (args.size == 4) && (args[1] == "reemplazar") -> 5
        else -> {
            System.err.println("[Main] Los argumentos recibidos no sirven, inserta --ayuda para una guía")
            exitProcess(1)
        }
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

            val retorno = procesarFlags(arbol, caracterEspacio, nRepeticiones, ocultos, arquitectura, recortar, readMe, descripcion)

            if(toArchivo != null && profundidad > 0)
                escribirArchivo(retorno, toArchivo)
            else
                println("[Main] La profundidad es 0, no se imprimirá nada, revisa si la carpeta raíz está escondida")
                retorno
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

fun procesarFlags(
    arbol : Arbol,
    caracterEspacio : String,
    nRepeticiones : Int,
    ocultos : Boolean,
    arquitectura : String,
    recortar : Int?,
    readMe : Boolean,
    descripcion : Boolean,
) : String{

    val nuevaArq =
        if (recortar != null) {
            arquitectura.lines().joinToString("\n") { linea ->
                // 1. Separar indentación del nombre
                val indentLen = linea.length - linea.trimStart(caracterEspacio[0]).length
                val indent    = linea.substring(0, indentLen)
                val filename  = linea.substring(indentLen)

                // 2. Encontrar el separador (. o /) — divide nombre de extensión/tipo
                val sepIdx = filename.indexOfLast { it == '.' || it == '/' }

                if (sepIdx == -1 || filename.substring(0, sepIdx).length <= recortar) {
                    linea  // no necesita recorte
                } else {
                    val name = filename.substring(0, sepIdx)
                    val ext  = filename.substring(sepIdx)
                    indent + name.take(recortar) + "..." + ext
                }
            }
        } else {
            arquitectura
        }

    val nuevaDescripcion =
        if(descripcion)
            arbol.organizarDescripciones(ocultos)
        else
            ""

    if(readMe)
        return arbol.generarREADME(nuevaArq, nuevaDescripcion)

    return nuevaArq
}

fun escribirArchivo(contenido : String, path : File) : String{
    try{
        path.writeText(contenido)
    }catch (e : Exception){
        return "No se pudo escribir en el archivo. \n$e"
    }
    return "Contenido escrito exitosamente."
}