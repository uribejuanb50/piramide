package src.policias

import java.nio.file.Path

//usar uno para todos los policias, solo escribir los atributos y ya
data class Registro(
    //general
    val pathActual: Path,
    val fecha : String, //str por ahora
    val hora : String, //str por ahora
    val id : Long,
    val tipo : String,
    val desc : String = "", //activado por flag desde terminal para la impresión,

    //Para el arbol
    val profundidad : Int = 0,
    val nodos : Int = -1, //No estou seguro si usarlo
    val nodosOcultos : Int //lo mismo
    val raiz : Path,

    //Para borrados
    val pathOriginal : Path,
    val palabraBorrada : String,

    //Para reemplazar - reemplazar necesita también
    //pathoriginal y palabraborrada, pero ya están declaradas
    val palabraNueva : String
){
    init{

    }
}
