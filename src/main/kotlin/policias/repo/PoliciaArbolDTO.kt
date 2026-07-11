package piramide.policias.repo

import piramide.policias.dominio.Registro
import java.nio.file.Path

data class PoliciaArbolDTO(
    val pathCarpetaGuardando : Path,
    val id : Long,
    val tipo :String,
    val pathRaizArbolUsando : Path?,
    val listaRegistro: ArrayList<Registro>
)
