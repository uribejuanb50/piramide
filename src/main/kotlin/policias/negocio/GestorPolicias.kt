package piramide.policias.negocio

import piramide.policias.dominio.Policia
import piramide.policias.dominio.PoliciaArbol
import piramide.policias.factories.PoliciaFactory
import piramide.policias.repo.PoliciaRepository
import piramide.utils.toCustomString

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
    var listaPoliciaBorrados : ArrayList<Policia>? = null
    var listaPoliciaReemplazados: ArrayList<Policia>? = null
    var listaPoliciaAplanados : ArrayList<Policia>? = null

    fun listarRegistros(
        solo : ArrayList<String>?, //tipos arbol, borrados, aplanados, reemplazados
        listaParejaFiltrar : ArrayList<Pair<Any, String>>?
    ) : String{

        val objetosListar = solo ?: arrayListOf("arbol", "borrados", "aplanados", "reemplazados")
        val listaParejaFiltrar = listaParejaFiltrar ?: arrayListOf(Pair("todos", "todos"))

        var retorno = "["

        for(objeto in objetosListar){
            when(objeto){
                "arbol" ->{}
            }
        }
        return "Incompleto"
    }

    fun listarRegistrosArbol() : String{
        var retorno = "====================== REGISTROS ARBOL ${policiaArbol.id} =========================\n"
        retorno += policiaArbol.listarRegistros().toCustomString()

        return retorno
    }

    fun cerrarGestor(){
        policiaRepository.guardarPoliciaArbol(policiaArbol)
        //para guardar las listas solo se guardan los que no estén en null así mismo como se cargan según si se llaman
        //en vez de cargar todo de una vez,
        //el policia arbol si se carga porque es necesario para las operaciones tipo arbol
    }
}