package piramide.cli

import java.io.File
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.system.exitProcess

class Validacion() {

    fun validarFlags(args: Array<String>) : Triple<Map<String, Any?>, Map<String, Any?>, ArrayList<String>> {

        val (mapaCategoriaArbol, nuevosArgs) = validarFlagsExploracion(args) //devuelve el primer mapa de exploracion y el arraylist procesado
        val (mapaCategoriaPolicias, nuevosArgs) = validarFlagsExploracion(args)

        return Triple(mapaCategoriaArbol, mapaCategoriaPolicias, nuevosArgs)
    }
    //pone los flags según lo que digan
    fun validarFlagsExploracion(args : Array<String>) : Pair<Map<String, Any?>, ArrayList<String>> {

        val opcionesValidas = listOf(
            //Caso base explorar/ver arbol
            "--simple","--ocultos", "--reversar", "--recortar",
            "--prueba", "--nivelMax", "--README", "--toArchivo",
            "--ayuda",
            //Asignacion
            "--descripcion", "--noReasignar",

            //Caso búsquedas
            "--condicion", "--force"
        )

        val nuevosArgs : ArrayList<String> = arrayListOf()
        val argsRestar : ArrayList<String> = arrayListOf()

        val diccionarioFlags = mutableMapOf<String, Any?>(
            //Caso base explorar/ver arbol
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
            "modulos" to false,

            //Asignacion
            "descripcion" to null,
            "noReasignar" to false,

            //Caso búsquedas
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
                "--modulos" -> diccionarioFlags["modulos"] = true
                "--noReasignar" -> diccionarioFlags["noReasignar"] = true
                "--descripcion" -> {
                    val siguiente = args.getOrNull(indice + 1)

                    if(siguiente == null || siguiente.contains("--")){
                        System.err.println("[Main] El siguiente argumento después de --descripción es inválido")
                        exitProcess(1)
                    }
                    argsRestar.add(siguiente)
                    diccionarioFlags["descripcion"] = siguiente
                }
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
                        diccionarioFlags["modulos"] = true
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
                    --recortar n               //recorta palabras hasta ese tamaño (falta añadir compatibilidad con --simple usar regex distintos) !!!Error, no cuenta bien, solo en --simple
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
                    (en raíz) kotlinc src -include-runtime -d main.jar
                    (crear alias) @doskey piramid=java -jar main.jar $*
                    (ejecutar) piramid [comando]
                    
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

    fun validarFlagsPolicias(args: ArrayList<String>) : Pair<Map<String, Any?>, ArrayList<String>> {

        val opcionesValidas = listOf(
            "--excluir" //Excluir paths de dirs o archivos, captura varios ej: --excluir path1 path2 .. pathn
        )

        val nuevosArgs : ArrayList<String> = arrayListOf()
        val argsRestar : ArrayList<String> = arrayListOf()

        val diccionarioFlags = mutableMapOf<String, Any?>(
            "excluir" to null, //Set<Path>

        )

        for ((indice, argumento) in args.withIndex()){
            if(argumento !in opcionesValidas){
                nuevosArgs.add(argumento)
                continue
            }

            when(argumento){
                "excluir" -> {

                    var salir : Boolean = true
                    var indiceSumarSiguiente = 1

                    val pathsExcluir : ArrayList<Path> = arrayListOf()

                    while(salir){

                        val siguiente = args.getOrNull(indice + indiceSumarSiguiente)

                        if(siguiente == null || siguiente.contains("--")) {
                            salir = false
                        }
                        else{
                            val path = Path.of(siguiente)

                            if(!path.exists()){
                                throw IllegalArgumentException("[Validacion] El path dado ($siguiente) no es un path existente")
                            }

                            pathsExcluir.add(path)
                            argsRestar.add(siguiente)
                        }
                    }

                    if(pathsExcluir.isEmpty())
                        throw IllegalArgumentException("[Validacion] No había paths después de excluir")
                }
                "ayuda" -> {

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

        return Pair()
    }

    //según la linea de comandos que entre, revisar qué caso es y asignar un número de función
    fun verificarEntrada(args: ArrayList<String>) : Pair<String, Int>{
        if(args.isEmpty()){
            System.err.println("[Main] los argumentos están vacíos.")
            exitProcess(1)
        }

        return args.first() to when(args.first()) { //esto devuelve Pair<"arbol" o "policia" : String, Int (de la opción)
            "arbol" ->
                when {
                //un argumento, simplemente el arbol
                args.size == 1 -> 10 //arbol, imprime el arbol
                args.size == 2 -> 20 //arbol "path" asigna el arbol
                (args.size == 3) && (args[1] == "borrar") -> 30
                (args.size == 3) && (args[1] == "buscar") -> 31
                (args.size == 4) && (args[1] == "reemplazar") -> 40
                else -> {
                    System.err.println("[Main] Los argumentos recibidos no sirven, inserta --ayuda para una guía")
                    exitProcess(1)
                }
            }
            "policia" ->
                when {
                    args.size == 2 -> 3
                    else -> {
                        System.err.println("[Main] Los argumentos recibidos no sirven, inserta --ayuda para una guía")
                        exitProcess(1)
                    }
                }

            else ->{
                System.err.println("[Main] Los argumentos recibidos no sirven, inserta --ayuda para una guía")
                exitProcess(1)
            }
        }
    }

}