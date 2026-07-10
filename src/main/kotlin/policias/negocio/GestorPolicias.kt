package main.kotlin.policias.negocio

import main.kotlin.policias.dominio.Policia
import main.kotlin.policias.dominio.PoliciaArbol
import main.kotlin.policias.factories.PoliciaFactory
import main.kotlin.policias.repo.PoliciaRepository

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