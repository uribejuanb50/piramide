package src

import java.io.File

class Arbol (val path : File){

    val raiz = Nodo(path.name, path)
    var profundidad : Int = 0

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
        return "Impresion re sencilla\n" + this.raiz.impresionUltraSencilla() + "\ncomplicada" + this.raiz.imprimirParaREADMEsencillo(this.mdreadme)
    }
}