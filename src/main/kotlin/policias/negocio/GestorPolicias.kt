package piramide.policias.negocio

import piramide.policias.dominio.Policia
import piramide.policias.dominio.PoliciaArbol
import piramide.policias.dominio.PoliciaReemplazados
import piramide.policias.factories.PoliciaFactory
import piramide.policias.repo.PoliciaRepository
import piramide.utils.toCustomString
import java.nio.file.Path
import kotlin.io.path.absolutePathString

class GestorPolicias(
    val policiaFactory: PoliciaFactory,
    val policiaRepository: PoliciaRepository
) {
    val policiaArbol : PoliciaArbol =
        policiaRepository.cargarPoliciaArbol() ?:
        policiaFactory.crearPoliciaArbol(
            policiaRepository.rutaGuardadoObjetoArbol,
            1
        )

    private val delegadorReemplazados = lazy {
        policiaRepository.cargarListaPoliciaReemplazados()
    }
    val listaPoliciaReemplazados: ArrayList<PoliciaReemplazados>
        get() = delegadorReemplazados.value

    var listaPoliciaAplanados : ArrayList<Policia> = arrayListOf() //

    fun listarRegistros(
        solo : ArrayList<String>?, //tipos arbol, borrados, aplanados, reemplazados
        listaParejaFiltrar : ArrayList<Pair<Any, String>>?
    ) : String{

        val objetosListar = solo ?: arrayListOf("arbol", "borrados", "aplanados", "reemplazados")
        val listaParejaFiltrar = listaParejaFiltrar ?: arrayListOf<Pair<Any, String>>(Pair("todos", "todos"))

        var retorno = ""

        for(objeto in objetosListar){
            when(objeto){
                "arbol" ->{
                    var retornoArbol = "============================= ARBOL ===============================================\n"
                    retornoArbol += policiaArbol.toCustomString() +"\n"
                    retornoArbol += "====================== REGISTROS ARBOL ${policiaArbol.id} =========================\n"
                    retornoArbol += listaRegistrosToPolimorficString(this.policiaArbol, listaParejaFiltrar) + "\n"
                    retorno += "$retornoArbol\n"
                }
                "reemplazados" -> {
                    var retornoReemplazados = "================================ REEMPLAZADOS ========================================="
                    retornoReemplazados +=
                        this.listaPoliciaReemplazados
                            .joinToString (separator = ""){ policia  ->
                                var str = "-------------------------------------\n"
                                str += policia.toCustomString() + "\n"
                                str += "---------------- Registros ---------------\n"
                                str += listaRegistrosToPolimorficString(policia, listaParejaFiltrar) + "\n"
                                str
                            }
                    retorno += "$retornoReemplazados\n"
                }
            }
        }
        return retorno
    }

    fun listaRegistrosToPolimorficString(policia : Policia, listaParejaFiltrar: ArrayList<Pair<Any, String>>) : String{
        val listaRegistrosFiltrada = policia.buscarPor(listaParejaFiltrar)
        return listaRegistrosFiltrada
            .map(policia.devolverFormatoRegistro)
            .toCollection(ArrayList())
            .toCustomString()
    }

    fun origenArbol() : String{
        return this.policiaArbol.pathRaizArbolUsando?.absolutePathString() ?: "No existe path en el arbol"
    }

    fun actualizarOrigenArbol(busquedaPorID : Pair<Long, String>){
        val registroBuscado = this.policiaArbol.buscarPor(arrayListOf<Pair<Any, String>>(busquedaPorID))

        if(registroBuscado.isEmpty())
            throw IllegalStateException("[GestorPolicias] No había registros con el id ${busquedaPorID.first} | fun actualizarOrigenArbol()")

        this.policiaArbol.pathRaizArbolUsando = registroBuscado.first().pathOriginal
    }

    fun cerrarGestor(){
        policiaRepository.guardarPoliciaArbol(policiaArbol)

        if(delegadorReemplazados.isInitialized())
            policiaRepository.guardarListaPoliciaReemplazados(this.listaPoliciaReemplazados)
        //para guardar las listas solo se guardan los que no estén en null así mismo como se cargan según si se llaman
        //en vez de cargar todo de una vez,
        //el policia arbol si se carga porque es necesario para las operaciones tipo arbol
    }

    fun devolverPolicia(tipo : String) : Policia{
        val pathArbol =
            this.policiaArbol.pathRaizArbolUsando
                ?: throw IllegalStateException("[GestorPolicias] El valor pathRaizArbolUsando está en nulo | fun devolverPolicia()")

        val busqueda: Pair<Path, String> = Pair(pathArbol, "file")
        val registrosDevueltos = this.policiaArbol.buscarPor(arrayListOf<Pair<Any, String>>(busqueda))

        val idArbolUsando =
            //el id del registro del policia arbol, es decir el id del arbol actual en los registros de Policia Arbol
            registrosDevueltos.first().id

        return when(tipo){
            "reemplazados" -> devolverPoliciaReemplazados(idArbolUsando)
            else -> throw IllegalArgumentException("[GestorPolicias] tipo : $tipo no est+a incluido en los tipos de lista | fun devolverPolicia")
        }
    }

    fun devolverPoliciaReemplazados(idArbolUsando : Long) : PoliciaReemplazados{

        println("[GestorPolicias] fun devolverPoliciaReemplazados")
        println("                 this.listaPoliciasReemplazados: ${this.listaPoliciaReemplazados.toCustomString()}")
        val policiaReemplazados =
            if(this.listaPoliciaReemplazados.isEmpty()) //si no existe lo crea y lo guarda
                policiaFactory.crearPoliciaReemplazados(
                    policiaRepository.rutaGuardadoObjetoReemplazados,
                    idArbolUsando,
                    policiaRepository.rutaGuardadoRespaldosReemplazados
                ).also {
                    println("                 .also: ${it.toCustomString(true)}")
                    this.listaPoliciaReemplazados.add(it)
                }
            else //si existe lo devuelve
                this.listaPoliciaReemplazados
                    .find{
                        println("                 En find it : ${it.id} | idArbolUsando: $idArbolUsando")
                        it.id == idArbolUsando
                    }
                    ?: policiaFactory.crearPoliciaReemplazados( //en caso de que no encuentre policiasreemplazados en lista, genera uno
                        policiaRepository.rutaGuardadoObjetoReemplazados,
                        idArbolUsando,
                        policiaRepository.rutaGuardadoRespaldosReemplazados
                    )


        return policiaReemplazados as PoliciaReemplazados //por alguna razon no lo encuentra a veces
    }
}