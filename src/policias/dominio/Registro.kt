package src.policias.dominio

import java.nio.file.Path

import java.time.LocalDate
import java.time.LocalTime

//usar uno para todos los policias, solo escribir los atributos y ya
data class Registro(
    //general
    val id : Long,
    val fecha : LocalDate = LocalDate.now(), //str por ahora
    val hora : LocalTime = LocalTime.now(),
    val pathOriginal : Path, //path original es el base, actual es para borrar reemplazar y aplanar
    val pathActual: Path? = null,
    val tipo : String? = null,
    val desc : String? = null, //activado por flag desde terminal para la impresión,

    //Para el arbol
    val profundidad : Int? = null,
    val raiz : Path? = null,

    //Para borrados
    val palabraBorrada : String? = null,

    //Para reemplazar - reemplazar necesita también
    //pathoriginal y palabraborrada, pero ya están declaradas
    val palabraNueva : String? = null
){
    init{

    }
}

fun Registro.toCustomString(){

}