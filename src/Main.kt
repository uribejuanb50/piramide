package src

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
        "--condicion"
        )

    val nuevosArgs : ArrayList<String> = arrayListOf()
    val argsRestar : ArrayList<String> = arrayListOf()

    val diccionarioFlags = mutableMapOf<String, Any?>(
        "simple" to false,
        "ocultos" to false,
        "reversar" to false,
        "recortar" to null,
        "prueba" to false,
        "nivelMax" to null,
        "README" to false,
        "toArchivo" to null,
        "descripcion" to false,
        "condicion" to null
    )

    for((indice, argumento) in args.withIndex()){

        if(argumento !in opcionesValidas) {
            nuevosArgs.add(argumento)
            continue
        }

        when(argumento){
            "--simple" -> diccionarioFlags["simple"] = true
            "--ocultos" -> diccionarioFlags["ocultos"] = true
            "--reversar" -> diccionarioFlags["reversar"] = true
            "--prueba" -> diccionarioFlags["prueba"] = true
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
                    \n
                    flags:
                    --simple                    //imprime el arbol sin caracteres especiales
                    --ocultos                   //mostrar carpetas y archivos ocultos
                    --reversar                  //reversar las listas internas y mostrar primero archivos que directorios
                    --recortar n                //recorta palabras hasta ese tamaño (falta añadir compatibilidad con --simple usar regex distintos)
                    --prueba                    //lanzar prueba
                    --nivelMax n                //hacerlo hasta el nivel max (0 es nada, 1 es la raiz, 2 el segundo nivel ...)
                    --README desc               //crear README (desc es opcional para activar la descripcion)
                    --toArchivo "path_archivo"  //guardar en archivo (borra el contenido)
                    --ayuda                     //Imprime este txt
                    
                    comandos:
                    ...main.jar "path_original" //genera un arbol
                    ...main.jar "path_original" "borrar" "palabra" 				            //elimina esa palabra de todo el arbol (falta implementar)
                    ...main.jar "path_original" "buscar" "palabra" --condicion "condicion" 	//busca archivo que contenga esa palabra
                    						        		                                //lo busca exacto o conteniendo, tiene que tener un flag, o por default
                    									                                    // sale exacto
                    ...main.jar "path_original" "reemplazar" "palabraVieja" "palabraNueva"  //reemplaza palabra dentro de cada archivo
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

    println("[Main] Se está imprimiendo la arquitectura de ${path.path}")

    return path
}

//usar las funciones específicas integrando los flags
fun manejarArbol(raiz: File, opcion: Int, args : ArrayList<String>, flags : Map<String, Any?>) : String {
    val arbol = Arbol(raiz)
    arbol.crearSubDirectorios()
    arbol.nPalabraMasLarga()

    val profundidad = arbol.calcularProfundidad()

    val prueba = flags["prueba"] as? Boolean ?: false
    if(prueba)
        println("prueba")//lanzar prueba (hacer)

    val reversar = flags["reversar"] as? Boolean ?: false
    if(reversar)
        arbol.reversarListas()

    val simple = flags["simple"] as? Boolean ?: false
    val ocultos = flags["ocultos"] as? Boolean ?: false
    val descripcion = flags["descripcion"] as? Boolean ?: false
    val readMe = flags["README"] as? Boolean ?: false
    val recortar = flags["recortar"] as? Int
    val nivelMax = flags["nivelMax"] as? Int
    val toArchivo = flags["toArchivo"] as? File


    return "[Main] " + when(opcion) {
        1 -> {
            val arquitectura =
                if(simple)
                    arbol.generarArquitecturaSencilla()
                else
                    arbol.generarArquitectura(profundidad, ocultos, nivelMax)

            val retorno = procesarFlags(arbol, ocultos, arquitectura, recortar, readMe, descripcion)

            if(toArchivo != null)
                escribirArchivo(retorno, toArchivo)
            else
                retorno
        }

        3 -> {
            val palabraBuscar = args[2] //desde el tres porque el args[1] es el que valida la función
            arbol.eliminarPalabra(palabraBuscar, nivelMax)
        }
        else -> {
            System.err.println("¿Cómo llegaste aquí? La cagué re duro en algo")
        }
    }
}

fun procesarFlags(
    arbol : Arbol,
    ocultos : Boolean,
    arquitectura : String,
    recortar : Int?,
    readMe : Boolean,
    descripcion : Boolean,
) : String{

    val nuevaArq =
        if(recortar != null){
            //cambiar esto ya que solo funciona para el arbol detallado, en el simple se rompe
            //toca asignar uno por uno
            val regex = Regex("""(?<= )(?=[^ /.\n]+[/.])|(?<=[^ /.\n])(?=[/.])""")

            arquitectura
                .split(regex)
                //.filterNot { it.contains(Regex("[─└├│]")) }
                //.filter { it.isNotEmpty() }
                .map{ palabra ->
                    if(palabra.length > recortar && !palabra.contains(Regex("[─└├│]")))
                        palabra.take(recortar) + "..."
                    else
                        palabra
                }
                .toCollection(ArrayList())
                .unirString()
                //.toCustomString()
        }
        else{
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