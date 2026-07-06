package src.policias

import java.io.File

//usar uno para todos los policias, solo escribir los atributos y ya
data class Registro(
    val file: File,
    val fecha : String, //str por ahora
    val hora : String, //str por ahora
    val id : Long,
    val desc : String //activado por flag
)
