package src

import java.io.File

class Nodo (val nombre : String, val path : File) {

    val listaSubArchivos: MutableList<Nodo?> = mutableListOf()
    var letrasPalabraMasLarga : Int = 0

    fun crearSubDirectorios() : Nodo? {

        if(this.path.isFile){
            return this
        }

        val directorio = this.path.listFiles()?: return null

        for(direccion in directorio){

            val nodo : Nodo = Nodo(direccion.name, direccion)
            val nodoActualizado = nodo.crearSubDirectorios()
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

            if(nodo == null){
                continue
            }

            val mayorSubDirectorio = nodo.calcularMedidaPalabraMasLarga()

            palabraMasGrande = maxOf(mayorSubDirectorio, this.nombre.length, palabraMasGrande)
        }
        return palabraMasGrande
    }

    /*
    fun imprimirParaREADME(nivel : Int, nodo : Nodo?) : String {
        if(nodo == null) {
            return ""
        }
        if(nodo.listaSubArchivos.size == 1){
            return
        }
    }
    */
    init{
        println("[Nodo] nombre: ${this.nombre}, path: ${this.path}")
    }
}




















