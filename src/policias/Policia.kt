package src.policias

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeParseException
import kotlin.system.exitProcess

abstract class Policia (
    val pathCarpetaGuardando : Path, val id : Long,
    val tipo : String
){

    val listaRegistro : ArrayList<Registro> = arrayListOf()
    val mapaMetodoBusqueda : Map<Any, (Any, Registro) -> Registro?> = mapOf(
        "file" to { pathBuscar , registro  -> //busca por path original y retorna
            val pathBuscar = pathBuscar as Path

            if(pathBuscar == registro.pathOriginal)
                registro
            else
                null
        },
        "fecha" to { fechaBuscar, registro ->
            val fechaBuscar = fechaBuscar as LocalDate

            if(fechaBuscar == registro.fecha)
                registro
            else
                null
        },
        "hora" to { horaBuscar, registro ->
            val horaBuscar = horaBuscar as LocalTime

            if(horaBuscar == registro.hora)
                registro
            else
                null
        },
        "id" to { idBuscar, registro ->
            val idBuscar = idBuscar as Long

            if(idBuscar == registro.id)
                registro
            else
                null
        },
        "eliminar" to { pathbuscar, registro ->
            val pathBuscar = pathbuscar as Path

            if(pathBuscar == registro.pathOriginal)
                registro
            else
                null
        },
        "todos" to { pathBuscar, registro ->
            registro
        }
    )

    abstract fun nuevoSeguimiento()
    abstract fun devolverFormatoListaRegistro(listaRegistro: ArrayList<Registro>) : ArrayList<String>
    abstract fun transdormarListaToSetPorAtributo(lista : ArrayList<Registro>) : Set<Path> //tranformar por registro.pathActual opathOriginal

    //para implementar en el cli
    fun buscarPor(listaPareja : ArrayList<Pair<Any, String>>) : ArrayList<String> { //el primero es el tipo dato de la busqueda
                                                                        //el segundo el string del metodoBusqueda
        //lo que quiero hacer: cada iteracion es la busqueda y su metodo de busqeuda,
        val log = "[Policia$tipo]" //Esto acá es para usar las funciones de repeat y lenght
        val charEspacio = " "      //dentro del $ ya que no se puede usar "" dentro de este sin romper la cadena

        val retorno : ArrayList<String> = arrayListOf()
        val setResultados : MutableSet<String> = mutableSetOf()

        for(pareja in listaPareja) {

            val busqueda = pareja.first //busqueda ej-> id : Long, fecha : LocalDate, hora : LocalTime
            val metodoBusqueda = pareja.second
            val resultadosFuncion : ArrayList<String> = arrayListOf()



            when {
                ((metodoBusqueda == "file") && (busqueda is Path))
                        || ((metodoBusqueda == "fecha") && (busqueda is LocalDate))
                        || ((metodoBusqueda == "hora") && (busqueda is LocalTime))
                        || ((metodoBusqueda == "id") && (busqueda is Long)) ->
                            {
                                val funcionBusqueda = mapaMetodoBusqueda[metodoBusqueda]?:throw IllegalArgumentException(
                                    "[Policia$tipo] Tipo de búsqueda no estaba en mapaMetodosBusqueda"
                                )
                                val registrosResultado = ejecutarBusqueda(busqueda, funcionBusqueda) //devuelve lista registro

                                resultadosFuncion.addAll(
                                    devolverFormatoListaRegistro(registrosResultado) //transforma los registros dependiendo de la clase de policia
                                ) //los devuelve los resultados de la búsqueda
                            }

                    else -> throw IllegalArgumentException(
                        "$log La búsqueda es tipo ${busqueda::class.qualifiedName}\n" +
                        "${charEspacio.repeat(log.length)} Y el método de búsqueda es $metodoBusqueda"
                    )
                }

            for(resultado in resultadosFuncion){
                if(setResultados.contains(resultado))
                    continue

                setResultados.add(resultado)
                retorno.add(resultado)
            }

        }

        return retorno
    }

    //mirar un flag --todos, así devuelve todo, colocar en el mapa
    //mira desde pathOriginal si viene de buscarPor y retorna todos los registros de ese pathOriginal
    //  por ejemplo si busco E:/pdf puede salir E:/pdf-1, E:/pdf-2 que son los guardados de o palabras borradas o reemplazadas, incliso de aplanadas
    fun ejecutarBusqueda(busqueda : Any, funcionBusqueda : (Any, Registro) -> Registro?) : ArrayList<Registro>{

        if(listaRegistro.isEmpty()){
            println("[Policia$tipo] La lista de registros está vacía")
            exitProcess(1)
        }

        val retorno : ArrayList<Registro> = arrayListOf()

        for(registro in listaRegistro){
            val resultado = funcionBusqueda(busqueda, registro)
            if(resultado != null)
                retorno.add(resultado)
        }

        return retorno
    }

    fun eliminarRegistros( //básicamente elimina los registros que no existan o hayan sido movidos o que se quieran eliminar, borra es
        listaRegistroEliminar : ArrayList<Path>?, //eliminar uno solo, o varios, conectarlos desde el cli
        listaExclusiones : ArrayList<Path> //viene o vacio o con exclusiones
    ) : ArrayList<String> {
        //se usan los paths actuales
        val listaRegistrosEliminar =
            listaRegistroEliminar ?: this.listaRegistro

        val eliminacion : ArrayList<Registro> = arrayListOf()
        val funcEliminacion = mapaMetodoBusqueda["eliminar"]?: throw IllegalArgumentException(
            "[Policia$tipo] Tipo de búsqueda no estaba en mapaMetodosBusqueda"
        )

        for(registro in listaRegistrosEliminar){
            val listaRegistroResultado = ejecutarBusqueda(registro, funcEliminacion)
            eliminacion.addAll(listaRegistroResultado)
        }



        val setEliminar = transdormarListaToSetPorAtributo(eliminacion) //implementar en cada

        this.listaRegistro
            .filterNot { it.pathOriginal in setEliminar }
            .toCollection(ArrayList())

        return arrayListOf("[Policia$tipo] ") //am validar eliminados supongo
    }

    init{

    }
}