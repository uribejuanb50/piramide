package piramide

import piramide.cli.Asignacion
import piramide.cli.Procesamiento
import piramide.policias.factories.PoliciaFactory
import piramide.policias.negocio.GestorPolicias
import piramide.policias.repo.PoliciaRepository
import piramide.utils.toCustomString
import piramide.cli.Validacion
import java.nio.file.Path
import kotlin.system.exitProcess

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

fun main(args : Array<String>) {
    System.setOut(java.io.PrintStream(System.out, true, "UTF-8"))
    System.setErr(java.io.PrintStream(System.err, true, "UTF-8"))

    println("Corrió")
    println(args.toCustomString())
    val policiaFactory = PoliciaFactory()
    val policiaRepository = PoliciaRepository()
    val gestorPolicias = GestorPolicias(policiaFactory, policiaRepository)

    try {

        val validacion = Validacion()
        val procesamiento = Procesamiento()
        val asignacion = Asignacion(procesamiento, gestorPolicias)

        val (flagsExploracion, flagsPolicias, args) = validacion.validarFlags(args)
        val tarea = validacion.verificarEntrada(args) //categoria o es arbol o policía
        val raiz = asignacion.generarPathArbol()
        val resultado = asignacion.asignarCategoria(tarea, raiz, args, flagsExploracion, flagsPolicias)
        println("falla: $resultado")

    }
    catch(e : IllegalArgumentException){
        println(e)
        exitProcess(1)
    }
    catch (e : IllegalStateException){
        println(e)
        exitProcess(1)
    }
    finally {
        //lógica para hacer el guardado en el gestor
        println("[Main] cerrando")
        gestorPolicias.cerrarGestor()
    }
}