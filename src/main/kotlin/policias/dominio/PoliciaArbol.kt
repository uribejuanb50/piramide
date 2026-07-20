package piramide.policias.dominio

import piramide.arbol.Arbol
import piramide.policias.factories.RegistroFactory
import piramide.utils.toCustomString
import java.nio.file.Path

class PoliciaArbol (
    pathCarpetaGuardando : Path,
    id : Long,
    tipo : String, //tipo se recibe desde factory tipo = "arbol"
    val registroFactory : RegistroFactory,
    var pathRaizArbolUsando : Path? = null
) : Policia(pathCarpetaGuardando, id, tipo){

    @Transient
    override val devolverFormatoRegistro: (registro: Registro) -> String = { registro ->
        var retornar = "\n"
        retornar += "ID: ${registro.id}\n"
        retornar += "Tipo: ${registro.tipo}\n"
        retornar += "Descrpcion: ${registro.desc}\n"
        retornar += "Path raíz: ${registro.pathOriginal}\n"
        retornar += "Fecha: ${registro.fecha}"
        "$retornar---------------------------------------------\n"
    }

    fun registrarArbol(pathRaiz : Path, descripcion : String?, usar : Boolean = true){

        val ultimoID : Long =
            if(!this.listaRegistro.isEmpty()) this.listaRegistro.last().id else 0

        this.listaRegistro.add(
            //Quitar lo de los nodos, literal son puras ganas de joder jaja
            registroFactory.generarRegistroArbol(ultimoID, "arbol", descripcion, pathRaiz)
        )
        if(usar)
            this.pathRaizArbolUsando = pathRaiz
    }

    //@JvmName("toStringClassPoliciaArbol")
    fun toCustomString(mostrarLista : Boolean = false) : String{
        var retorno = ""

        retorno += "pathRaizUsando: ${pathRaizArbolUsando?.toAbsolutePath()?:"null"}\n"
        retorno += "pathCarpetaGuardando : ${pathCarpetaGuardando.toAbsolutePath()}\n"
        retorno += "id : $id\n"
        retorno += "tipo : $tipo\n"
        retorno +=
            if(mostrarLista)
                this.listaRegistro.map(devolverFormatoRegistro).toCollection(ArrayList()).toCustomString()
            else
                ""

        return retorno
    }

}