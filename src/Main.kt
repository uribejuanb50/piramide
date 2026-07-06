package src

import src.arbol.Arbol
import src.cli.Asignacion
import src.cli.Procesamiento
import src.cli.Validacion
import src.utils.toCustomString
import java.io.File
import kotlin.system.exitProcess

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main(args : Array<String>) {

    val validacion = Validacion()
    val procesamiento = Procesamiento()
    val asignacion = Asignacion(procesamiento)

    val (flags, args) = validacion.validarFlags(args)
    val opcion = validacion.verificarEntrada(args)
    val raiz = asignacion.generarPath(args)
    val resultado = asignacion.manejarArbol(raiz, opcion, args, flags)
    println(resultado)
}