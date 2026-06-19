package src

import java.io.File

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main(args : Array<String>) {

    val args = verificarEntrada(args)


}

fun verificarEntrada(args : Array<String>) : Array<String> {
    if(args.isEmpty())
        throw IllegalArgumentException("[Main] Los argumentos de entrada están vacíos mani")

    if(args.size > 1)
        throw IllegalArgumentException("[Main] Se espera un solo argumento en la terminal")

    return args
}

fun generarPath(args : Array<String>) : File {
    val path = File(args.first())

    if(!path.exists())
        throw IllegalArgumentException("[Main] El path no existe cabrón")

    if(path.isFile)
        throw IllegalArgumentException("[Main] El path es un archivo, no un directorio")

    return path
}
