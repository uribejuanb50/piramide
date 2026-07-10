package main.kotlin.policias.factories

import main.kotlin.policias.dominio.PoliciaArbol
import java.nio.file.Path

class PoliciaFactory(
    val registroFactory : RegistroFactory = RegistroFactory
) {
    fun crearPoliciaArbol(pathCarpetaGuardado : Path, id : Long) : PoliciaArbol {
        return PoliciaArbol(pathCarpetaGuardado, id, "arbol", registroFactory)
    }

    //crear los otros tipos de policia

}