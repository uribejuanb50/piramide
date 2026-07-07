package src.policias

import java.nio.file.Path

import java.time.LocalDate
import java.time.LocalTime

//usar uno para todos los policias, solo escribir los atributos y ya
data class Registro(
    //general
    val id : Long,
    val fecha : LocalDate = LocalDate.now(), //str por ahora
    val hora : LocalTime = LocalTime.now(),
    val pathOriginal : Path?, //path original es el base, actual es para borrar reemplazar y aplanar
    val pathActual: Path = Path.of(""),
    val tipo : String = "",
    val desc : String = "", //activado por flag desde terminal para la impresión,

    //Para el arbol
    val profundidad : Int = 0,
    val nodos : Int = -1, //No estou seguro si usarlo
    val nodosOcultos : Int, //lo mismo
    val raiz : Path,

    //Para borrados
    val palabraBorrada : String,

    //Para reemplazar - reemplazar necesita también
    //pathoriginal y palabraborrada, pero ya están declaradas
    val palabraNueva : String
){
    init{

    }
}

fun Registro.toCustomString(){

}