package src.policias

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeParseException

abstract class Policia (val path : Path){

    abstract val tipo : String
    abstract val id : String

    val listaRegistro : ArrayList<Registro> = arrayListOf()
    val mapaMetodoBusqueda  = mapOf(
        "file" to { pathBuscar : Path, registro : Registro ->
            ""
        },
        "fecha" to { fechaBuscar : LocalDate, registro : Registro ->
            ""
        }
        "hora" to { horaBuscar :

        }
    )

    abstract fun nuevoSeguimiento()


    fun buscarPor(listaPareja : ArrayList<Pair<Any, String>>) : Path { //el primero es el tipo dato de la busqueda
                                                                        //el segundo el string del metodoBusqueda
        //lo que quiero hacer: cada iteracion es la busqueda y su metodo de busqeuda,
        val log = "[Policia$tipo]" //Esto acá es para usar las funciones de repeat y lenght
        val charEspacio = " "      //dentro del $ ya que no se puede usar "" dentro de este sin romper la cadena

        for(pareja in listaPareja) {
            val busqueda = pareja.first //busqueda ej-> id : Long, fecha : LocalDate, hora : LocalTime
            val metodoBusqueda = pareja.second

            val busquedaConTipo =

                when { //mirar para los tipos lo de sealed class aunque no creo, mejor así nno?
                    ((metodoBusqueda == "file") && (busqueda is Path))
                            || ((metodoBusqueda == "fecha") && (busqueda is LocalDate))
                            || ((metodoBusqueda == "hora") && (busqueda is LocalTime))
                            || ((metodoBusqueda == "id") && (busqueda is Long)) -> {

                            }

                    else -> throw IllegalArgumentException(
                        "$log La búsqueda es tipo ${busqueda::class.qualifiedName}\n" +
                        "${charEspacio.repeat(log.length)} Y el método de búsqueda es $metodoBusqueda"
                    )
                }

        }

        return
    }

    fun validarMetodoBusqueda(metodoBusqueda: String) : () -> String{

    }

}