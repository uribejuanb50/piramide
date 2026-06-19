package src

import java.io.File

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main(args : Array<String>) {

    val raiz = verificarEntrada(args)

    val arbol : Arbol = Arbol(raiz)
    arbol.crearSubdirectorios()
    print(arbol.nPalabraMasLarga())

}

fun verificarEntrada(args : Array<String>) : Array<String> {
    if(args.isEmpty())
        throw IllegalArgumentException("[Main] Los argumentos de entrada están incompletos")
    return args
}
