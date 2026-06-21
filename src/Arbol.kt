package src

import java.io.File


class Arbol (val path : File){

    val raiz = Nodo(path.name, path)
    var profundidad : Int = 0

    val mapaCondiciones = mapOf(
        "exacto" to { str1: String, str2: String -> str1 == str2},
        "conteniendo" to {str1, str2 -> str1.contains(str2)}
    )

    lateinit var mdreadme : ArrayList<String> //para programación dinámica

    fun iniciarMDReadME(espacio : Int){
        this.mdreadme = ArrayList(List (espacio) {""})
    }

    fun crearSubDirectorios() : Unit {
        this.raiz.crearSubDirectorios()
    }

    fun nPalabraMasLarga() : Int {
        return this.raiz.calcularMedidaPalabraMasLarga()
    }
    fun calcularProfundidad() : Int{
        val profundidad = this.raiz.calcularProfundidad()
        this.profundidad = profundidad
        return profundidad
    }
    fun imprimirParaREADMEsencillo(profundidad : Int) : String {
        this.iniciarMDReadME(profundidad)
        this.raiz.reversarListas()
        return this.raiz.imprimirParaREADMEsencillo(this.mdreadme)
    }

    fun buscarArchivosPorNombre(busqueda : String, condicion : String) : String{
        return try {
            val metodoBusqueda = this.mapaCondiciones.getValue(condicion)
            this.raiz.buscarArchivosPorNombreYCondicion(busqueda, metodoBusqueda).toCustomString()
        } catch (e: Exception) {
            throw IllegalArgumentException("[Arbol] Las condiciones de busquedas actuales son ${this.mapaCondiciones.keys}")
        }
    }
}

fun ArrayList<String>?.toCustomString() : String {
    if(this == null){
        return "NoEncontrado"
    }
    var retorno = "["

    for((indice, str) in this.withIndex()){

        if(indice != this.lastIndex)
            retorno += "$str, "
        else
            retorno += str
    }

    return "$retorno]"
}
