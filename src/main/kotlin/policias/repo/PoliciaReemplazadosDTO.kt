package piramide.policias.repo

import piramide.policias.dominio.Registro
import java.nio.file.Path

data class PoliciaReemplazadosDTO(
    val pathCarpetaGuardando : Path,
    val id : Long,
    val tipo : String,
    val carpetaGuardandoRespaldos : Path,
    val listaRegistro : ArrayList<Registro>
)
