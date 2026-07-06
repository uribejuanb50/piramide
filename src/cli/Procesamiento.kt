package src.cli

import src.arbol.Arbol

class Procesamiento() {
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
}