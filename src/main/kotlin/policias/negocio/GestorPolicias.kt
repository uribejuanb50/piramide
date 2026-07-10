package piramide.policias.negocio

import piramide.policias.dominio.Policia
import piramide.policias.dominio.PoliciaArbol
import piramide.policias.factories.PoliciaFactory
import piramide.policias.repo.PoliciaRepository

class GestorPolicias(
    val policiaFactory: PoliciaFactory,
    val policiaRepository: PoliciaRepository
) {
    val policiaArbol : PoliciaArbol =
        policiaRepository.cargarPoliciaArbol() ?:
        policiaFactory.crearPoliciaArbol(
            policiaRepository.rutaGuardadoArbol,
            1
        )
    lateinit var listaPoliciaBorrados : ArrayList<Policia>
    lateinit var listaPoliciaReemplazados: ArrayList<Policia>
    lateinit var listaPoliciaAplanados : ArrayList<Policia>

}