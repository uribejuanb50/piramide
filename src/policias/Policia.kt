package src.policias

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeParseException
import kotlin.system.exitProcess

abstract class Policia (val path : Path){

    abstract val tipo : String
    abstract val id : String

    val listaRegistro : ArrayList<Registro> = arrayListOf()
    val mapaMetodoBusqueda : Map<Any, (Any, Registro) -> String> = mapOf(
        "file" to { pathBuscar , registro  ->
            val pathBuscar = pathBuscar as Path
            ""
        },
        "fecha" to { fechaBuscar, registro ->
            val fechaBuscar = fechaBuscar as LocalDate
            ""
        },
        "hora" to { horaBuscar, registro ->
            val horaBuscar = horaBuscar as LocalTime
            ""
        },
        "id" to { idBuscar, registro ->
            val idBuscar = idBuscar as Long
            ""
        }
    )

    abstract fun nuevoSeguimiento()


    fun buscarPor(listaPareja : ArrayList<Pair<Any, String>>)  { //el primero es el tipo dato de la busqueda
                                                                        //el segundo el string del metodoBusqueda
        //lo que quiero hacer: cada iteracion es la busqueda y su metodo de busqeuda,
        val log = "[Policia$tipo]" //Esto acá es para usar las funciones de repeat y lenght
        val charEspacio = " "      //dentro del $ ya que no se puede usar "" dentro de este sin romper la cadena

        for(pareja in listaPareja) {
            val busqueda = pareja.first //busqueda ej-> id : Long, fecha : LocalDate, hora : LocalTime
            val metodoBusqueda = pareja.second

            val busquedaConTipo =

                when {
                    ((metodoBusqueda == "file") && (busqueda is Path))
                            || ((metodoBusqueda == "fecha") && (busqueda is LocalDate))
                            || ((metodoBusqueda == "hora") && (busqueda is LocalTime))
                            || ((metodoBusqueda == "id") && (busqueda is Long)) -> {
                                val funcionBusqueda = mapaMetodoBusqueda[metodoBusqueda]

                            }

                    else -> throw IllegalArgumentException(
                        "$log La búsqueda es tipo ${busqueda::class.qualifiedName}\n" +
                        "${charEspacio.repeat(log.length)} Y el método de búsqueda es $metodoBusqueda"
                    )
                }

        }
    }

    //mirar un flag --todos, así devuelve todo, colocar en el mapa
    fun ejecutarBusqueda(busqueda : Any, funcionBusqueda : (Any, Registro) -> String){
        if(listaRegistro.isEmpty()){
            println("[Policia$tipo] La lista de registros está vacía")
            exitProcess(1)
        }
        for(registro in listaRegistro){
            
        }
    }
}