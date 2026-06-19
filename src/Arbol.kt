package src

import java.io.File

class Arbol (val path : File){

    val raiz = Nodo(path.name, path)

    fun crearSubdirectorios() : Unit {
        val paths = this.path.listFiles()

        val iterar: (path : File) -> Unit = { estructura ->
            val subArchivo = raiz.crearSubdirectorios(estructura)
            raiz.listaSubArchivos.add(subArchivo)
        }

        paths?.forEach(iterar)
    }

    fun nPalabraMasLarga() : Int {
        return this.raiz.calcularMedidaPalabraMasLarga()
    }
}