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

    //no recuerdo para qué estaba esto
    fun buscarPorFile(busqueda: String, listaMetodoBusqueda : ArrayList<String?>) : Path {

        val log = "[Policia$tipo]" //Esto acá es para usar las funciones de repeat y lenght
        val charEspacio = " "      //dentro del $ ya que no se puede usar "" dentro de este sin romper la cadena
        val metodoBusqueda = listaMetodoBusqueda.first()    //por ahora, se supone que la lista que llega trae todas los tipos de busqeuda que se hacen
                                                            //si llega más de un metodo de búsqueda el algoritmo filtra y hace intersección entre las listas
        val busqueda = //esto solo asigna busqueda al tipo de busqueda para luego meterlo en los lambdas
            try {
                when(metodoBusqueda){ //mirar para los tipos lo de sealed class aunque no creo, mejor así nno?
                    "file" -> {
                        val pathBusqueda = Path(busqueda)

                        if(!pathBusqueda.exists())
                            throw IllegalArgumentException(
                                "$log La búsqueda $busqueda no existe" +
                                        "${charEspacio.repeat(log.length)} Deberías intentar con el comando 'path_original' 'buscar' 'palara' --condicion 'condicion' "
                            )

                        else
                            pathBusqueda
                    }
                    "fecha" -> {
                        val busqueda =

                    }
                    "hora" -> {

                    }
                    "id" -> {

                    }
                    else -> throw IllegalArgumentException("$log El método de búsqueda es inválido")
                }
            } catch (e : DateTimeParseException){
                throw IllegalArgumentException("$log La búsqueda por fecha $busqueda no es una fecha")
            }

        for(registro in listaRegistro){
            TODO()
        }
        return pathBusqueda
    }

    fun validarMetodoBusqueda(metodoBusqueda: String) : () -> String{

    }

}