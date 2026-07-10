package src.policias.negocio

import src.policias.dominio.Policia
import src.policias.dominio.PoliciaArbol
import src.policias.dominio.PoliciaReemplazados
import src.policias.factories.PoliciaFactory
import src.policias.repo.PoliciaRepository

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