package src

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

            val nodo : Nodo = Nodo(direccion.name, direccion)
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

    fun calcularProfundidad() : Int{
        if(this.path.isFile){
            return 1
        }
        if(this.listaSubArchivos.isEmpty()){
            return 0
        }

        var nivelMasAlto : Int = 0

        for(nodo in this.listaSubArchivos){

            val sumatoria = nodo.calcularProfundidad() + 1
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

        this.listaSubArchivos.reverse()

        for (subdirectorio in this.listaSubArchivos){
            subdirectorio.reversarListas()
        }
    }
    fun impresionUltraSencilla(nivel : Int = 0) : String {
        val espacio : String = "   "

        if(this.path.isFile){
            return espacio.repeat(nivel) + this.nombre + "\n"
        }
        if(this.listaSubArchivos.isEmpty()){
            return "\n"
        }
        var devolver : String = espacio.repeat(nivel) + this.nombre + "/\n"

        for(subdirectorio in this.listaSubArchivos){
            devolver += subdirectorio.impresionUltraSencilla(nivel + 1)
        }

        return devolver
    }

    fun imprimirParaREADMEsencillo(mdreadme : ArrayList<String>, nivel : Int = 0, flagUltimoPuesto : Boolean = false) : String {

        if(this.path.isFile){
            val conector = if(flagUltimoPuesto) "└── " else "├── "
            return mdreadme[nivel - 1] + conector + this.nombre + "\n"
        }
        if(this.listaSubArchivos.isEmpty()){
            val conector = if(flagUltimoPuesto) "└── " else "├── "
            return mdreadme[nivel - 1] + conector + this.nombre + "/\n"
        }

        var arquitectura : String = ""

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

            arquitectura += subdirectorio.imprimirParaREADMEsencillo(mdreadme, nivel + 1, ultimoPuesto)
        }
        return arquitectura
    }

    init{
        println("[Nodo] nombre: ${this.nombre}, path: ${this.path}")
    }
}




















