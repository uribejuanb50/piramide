package src

import java.io.File

class Arbol (val path : File){

    val raiz = Nodo(path.name, path)

    fun crearSubDirectorios() : Unit {
        this.raiz.crearSubDirectorios()
    }

    fun nPalabraMasLarga() : Int {
        return this.raiz.calcularMedidaPalabraMasLarga()
    }
}