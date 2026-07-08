package src.policias

import java.nio.file.Path

class PoliciaFactory(
    val registroFactory : RegistroFactory = RegistroFactory
) {
    fun crearPoliciaArbol(pathCarpetaGuardado : Path, id : Long) : Policia {
        return PoliciaArbol( pathCarpetaGuardado, id, "arbol", registroFactory)
    }

    //crear los otros tipos de policia

}