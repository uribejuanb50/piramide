package src.arbol

import java.io.File

class Nodo (val nombre : String, val path : File) {

    val listaSubArchivos: MutableList<Nodo> = mutableListOf()
    var letrasPalabraMasLarga : Int = 0

    fun crearSubDirectorios() : Nodo? {

        if(this.path.isFile){
            return this
        }

        val directorio = this.path.listFiles()?: return null

        for(direccion in directorio){

            val nodo = Nodo(direccion.name, direccion)
            val nodoActualizado = nodo.crearSubDirectorios()

            if(nodoActualizado != null)
                this.listaSubArchivos.add(nodoActualizado)
        }

        return this
    }

    //se puede mejorar usando el this
    fun calcularMedidaPalabraMasLarga() : Int{

        if(this.path.isFile) {
            return this.nombre.length
        }
        if(this.listaSubArchivos.isEmpty()){
            return 0
        }

        var palabraMasGrande : Int = 0

        for(nodo in this.listaSubArchivos){

            val mayorSubDirectorio = nodo.calcularMedidaPalabraMasLarga()

            palabraMasGrande = maxOf(mayorSubDirectorio, this.nombre.length, palabraMasGrande)
        }
        return palabraMasGrande
    }

    fun calcularProfundidad(mostrarOcultos: Boolean = false) : Int{
        if(!mostrarOcultos && this.path.isHidden)
            return 0

        if(this.path.isFile)
            return 1

        if(this.listaSubArchivos.isEmpty())
            return 0


        var nivelMasAlto : Int = 0

        for(nodo in this.listaSubArchivos){

            val sumatoria = nodo.calcularProfundidad(mostrarOcultos) + 1
            nivelMasAlto = maxOf(sumatoria, nivelMasAlto)
        }

        return nivelMasAlto
    }

    //para que imprima primero subdirectorios luego archivos

    fun reversarListas() : Unit{
        if(this.path.isFile){
            return
        }
        if(this.listaSubArchivos.isEmpty()){
            return
        }

        this.listaSubArchivos.sortWith(compareByDescending<Nodo> {it.path.isDirectory}.thenBy { it.nombre })

        for (subdirectorio in this.listaSubArchivos){
            subdirectorio.reversarListas()
        }

        return
    }
    fun impresionUltraSencilla(
        mostrarOcultos : Boolean = false,
        espacio : String = "   ",
        nivel : Int = 0) : String
    {

        if(!mostrarOcultos && this.path.isHidden)
            return ""

        if(this.path.isFile)
            return espacio.repeat(nivel) + this.nombre + "\n"

        if(this.listaSubArchivos.isEmpty())
            return espacio.repeat(nivel) + this.nombre + "/\n"

        var devolver : String = espacio.repeat(nivel) + this.nombre + "/\n"

        for(subdirectorio in this.listaSubArchivos){
            devolver += subdirectorio.impresionUltraSencilla(mostrarOcultos, espacio,nivel + 1)
        }

        return devolver
    }

    fun generarArquitectura(
        mdreadme : ArrayList<String>,
        nivel : Int = 0,
        flagUltimoPuesto : Boolean = false,
        mostrarEscondidos : Boolean = false,
        profundidadMax : Int? = null
    ) : String {

        if(profundidadMax == nivel){
            return ""
        }

        if(this.path.isFile){
            val conector = if(flagUltimoPuesto) "└── " else "├── "
            return mdreadme[nivel - 1] + conector + this.nombre + "\n"
        }

        if(!mostrarEscondidos && this.path.isHidden)
            return ""

        if(this.listaSubArchivos.isEmpty()){
            val conector = if(flagUltimoPuesto) "└── " else "├── "
            return mdreadme[nivel - 1] + conector + this.nombre + "/\n"
        }

        var arquitectura = ""

        if(nivel == 0){
            arquitectura = this.nombre + "/\n"
            mdreadme[nivel] = ""
        }
        else{
            val conector = if(flagUltimoPuesto) "└── " else "├── "
            arquitectura += mdreadme[nivel - 1] + conector + this.nombre + "/\n"

            mdreadme[nivel] = mdreadme[nivel - 1] + if(flagUltimoPuesto) "    " else "│   "
        }

        for((indice, subdirectorio) in this.listaSubArchivos.withIndex()) {

            val ultimoPuesto = if(indice == this.listaSubArchivos.lastIndex) true else false

            if(nivel > 0)
                mdreadme[nivel] = mdreadme[nivel - 1] + (if(flagUltimoPuesto) "    " else "│   ")

            arquitectura += subdirectorio.generarArquitectura(mdreadme, nivel + 1, ultimoPuesto, mostrarEscondidos, profundidadMax)
        }
        return arquitectura
    }

    fun buscarArchivosPorNombreYCondicion(
        busqueda : String,
        condicion : (nombre : String, busqueda : String) -> Boolean
    ): ArrayList<String>?{

        if(condicion(this.path.name, busqueda)){
            return arrayListOf(this.path.path)
        }
        if(this.path.isFile){
            return null
        }
        if(this.listaSubArchivos.isEmpty()){
            return null
        }

        var retorno : ArrayList<String> = arrayListOf()

        for(subdirectorios in this.listaSubArchivos){
            val listaRetornante = subdirectorios.buscarArchivosPorNombreYCondicion(busqueda, condicion)

            if(listaRetornante != null)
                retorno.addAll(listaRetornante)
        }

        return retorno
    }

    fun devolverPadresConHijos(mostrarEscondidos : Boolean = false) : ArrayList<String>{
        if(this.path.isFile){
            return arrayListOf()
        }
        if(!mostrarEscondidos && this.path.isHidden)
            return arrayListOf()

        if(this.listaSubArchivos.isEmpty())
            return arrayListOf()

        val lista : ArrayList<String> = arrayListOf()
        lista.add(this.nombre + "/")

        for(subdirectorio in this.listaSubArchivos){
            if(!mostrarEscondidos && subdirectorio.path.isHidden)
                continue

            val strSubdirectorio = if(subdirectorio.path.isDirectory) "${subdirectorio.nombre}/" else subdirectorio.nombre
            lista.add(strSubdirectorio)
        }
        for(subdirectorio in this.listaSubArchivos){
            lista.addAll(subdirectorio.devolverPadresConHijos())
        }
        return lista
    }

    fun eliminarPalabra(palabra : String, nivelMax : Int? = null, mostrarOcultos : Boolean = false, carry : Int = 0) : Boolean {
        if(!mostrarOcultos && this.path.isHidden)
            return false

        if(nivelMax != null && nivelMax == carry)
            return false

        if(this.path.isFile)
            return limpiarArchivoJson(palabra, this.path)

        if(this.listaSubArchivos.isEmpty())
            return false

        var retorno = false

        for(subdirectorio in this.listaSubArchivos) {
            val retornoRecursivo = subdirectorio.eliminarPalabra(palabra, nivelMax, mostrarOcultos, carry)
            retorno = (retorno || retornoRecursivo)
        }

        return retorno
    }

    fun limpiarArchivoJson(palabra : String, archivo: File) : Boolean {
        return try
        {
            // 1. Leer tod o el contenido del archivo como una cadena de texto
            val contenidoOriginal = archivo.readText()

            // 2. Reemplazar el patrón " —" por una cadena vacía
            // Usamos un Regex para asegurar que capture exactamente el guion largo/corto y los paréntesis
            val patron = "\\b${Regex.escape(palabra)}\\b".toRegex()
            val contenidoLimpio = contenidoOriginal.replace(patron, "")

            // 3. Sobrescribir el archivo con el nuevo contenido limpio
            archivo.writeText(contenidoLimpio)

            true
        } catch (e : Exception){
            print("[Nodo] error e : $e")
            false
        }
    }

    init{
        //print("[Nodo] Nombre: ${this.nombre} | path : ${this.path}")
    }
}