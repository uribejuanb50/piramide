package src.policias

import java.nio.file.Path

class PoliciaArbol (
    pathCarpetaGuardando : Path,
    id : Long,
    tipo : String, //tipo se recibe desde factory tipo = "arbol"
) : Policia(pathCarpetaGuardando, id, tipo){

    var pathRaizArbolUsando : Path? = this.listaRegistro.last().pathOriginal

    fun registrarArbol(pathRaiz : Path, usar : Boolean = true){
        this.listaRegistro.add(pathRaiz)
        if(usar)
            this.pathRaizArbolUsando = pathRaiz
    }
}