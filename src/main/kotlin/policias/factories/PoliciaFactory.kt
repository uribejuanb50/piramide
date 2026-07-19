package piramide.policias.factories

import piramide.policias.dominio.PoliciaArbol
import piramide.policias.dominio.PoliciaReemplazados
import java.nio.file.Path

class PoliciaFactory(
    val registroFactory : RegistroFactory = RegistroFactory
) {
    fun crearPoliciaArbol(pathCarpetaGuardando : Path, id : Long) : PoliciaArbol {
        return PoliciaArbol(pathCarpetaGuardando, id, "arbol", registroFactory)
    }

    //crear los otros tipos de policia
    fun crearPoliciaReemplazados(
        pathCarpetaGuardando: Path,
        id : Long,
        pathCarpetaRespaldoReemplazados: Path
    ) : PoliciaReemplazados{
        return PoliciaReemplazados(pathCarpetaGuardando, id, "reemplazados", pathCarpetaRespaldoReemplazados, registroFactory)
    }
}