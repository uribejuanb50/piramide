package src.policias

import src.arbol.Arbol
import java.nio.file.Path

class PoliciaArbol (
    pathCarpetaGuardando : Path,
    id : Long,
    tipo : String, //tipo se recibe desde factory tipo = "arbol"
    val registroFactory : RegistroFactory,
) : Policia(pathCarpetaGuardando, id, tipo){

    var pathRaizArbolUsando : Path? = this.listaRegistro.last().pathOriginal

    fun registrarArbol(pathRaiz : Path, descripcion : String?, usar : Boolean = true){

        val ultimoID : Long = this.listaRegistro.last().id

        this.listaRegistro.add(
            //Quitar lo de los nodos, literal son puras ganas de joder jaja
            registroFactory.generarRegistroArbol(ultimoID, "arbol", descripcion, pathRaiz)
        )
        if(usar)
            this.pathRaizArbolUsando = pathRaiz
    }

    fun devolverArbolFuncionando() : Arbol{

        val pathArbol =
           this.pathRaizArbolUsando?.toFile() ?: throw IllegalStateException("[PoliciaArbol] No había árboles disponibles")

        val arbol : Arbol = Arbol(pathArbol)
        arbol.crearSubDirectorios()

        return arbol
    }

    override fun nuevoSeguimiento() {
        TODO("Not yet implemented")
    }

    override fun devolverFormatoListaRegistro(listaRegistro: ArrayList<Registro>): ArrayList<String> {
        TODO("Not yet implemented")
    }

    override fun transdormarListaToSetPorAtributo(lista: ArrayList<Registro>): Set<Path> {
        TODO("Not yet implemented")
    }
}