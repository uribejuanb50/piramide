package piramide

import piramide.policias.factories.PoliciaFactory
import piramide.policias.negocio.GestorPolicias
import piramide.policias.repo.PoliciaRepository
import piramide.utils.toCustomString
import java.nio.file.Path
import kotlin.system.exitProcess

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main(args : Array<String>) {
    try {

        println("Corrió")
        println(args.toCustomString())
        val policiaFactory = PoliciaFactory()
        val policiaRepository = PoliciaRepository()
        val gestorPolicias = GestorPolicias(policiaFactory, policiaRepository)

        val path = Path.of(args.first())
        gestorPolicias.policiaArbol.registrarArbol(path, "Arbol de prueba")
        gestorPolicias.policiaArbol.listarRegistros()


        /*
        val validacion = Validacion()
        val procesamiento = Procesamiento()
        val asignacion = Asignacion(procesamiento)

        val (flags, args) = validacion.validarFlags(args)
        val (categoria, opcion) = validacion.verificarEntrada(args) //categoria o es arbol o policía
        val raiz = asignacion.generarPath(args)
        val resultado = asignacion.manejarArbol(raiz, opcion, args, flags)
        println(resultado)
        */
    }
    catch(e : IllegalArgumentException){
        exitProcess(1)
    }
    catch (e : IllegalStateException){
        println(e)
        exitProcess(1)
    }
    finally {
        //lógica para hacer el guardado en el gestor
    }
}