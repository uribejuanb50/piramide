package piramide.utils

import piramide.arbol.Nodo
import java.io.File

//ARRAY
fun Array<String>.toCustomString() : String {
    var retorno = "["

    for((indice, str) in this.withIndex()){
        if(indice != this.lastIndex)
            retorno += "$str, "
        else
            retorno += str
    }

    return "$retorno]"
}

//ARRAYLIST
fun ArrayList<String?>?.toCustomString() : String {
    if(this == null)
        return "[Vacío]"

    var retorno = "["

    for((indice, str) in this.withIndex()){

        if(str == null)
            retorno += "null"
        else
            retorno += str

        if(indice != this.lastIndex)
            retorno += ", "
    }

    return "$retorno]"
}

@JvmName("toCustomStringArrayListString")
fun ArrayList<String>?.toCustomString() : String {
    if(this == null){
        return "NoEncontrado"
    }

    var retorno = "["

    for((indice, str) in this.withIndex()){

        if(indice != this.lastIndex)
            retorno += "$str, "
        else
            retorno += str
    }
    return "$retorno]"
}

@JvmName("toCustomStringArrayListAny")
fun Map<String, Any?>.toCustomString() : Pair<String, Int> {
    var retorno = "["
    var elementos = 0

    val lastindex = this.size - 1

    for((indice, item) in this.entries.withIndex()){

        if(item.value == null || item.value == false)
            continue

        elementos++

        if(indice != lastindex)
            retorno += "${item.key}, "
        else
            retorno += "${item.key}]"
    }

    return Pair(retorno, elementos)
}

@JvmName("toCustomStringArrayListFile")
fun ArrayList<File>.toCustomString() : String {

    val transformacion = { path : File ->
        if(path.isDirectory)
            "${path.name}/"
        else
            "${path.name}"
    }

    return this.map(transformacion).toCollection(ArrayList()).toCustomString()
}

//Mutable list
@JvmName("toCustomStringMutableListNodo")
fun MutableList<Nodo>.toCustomString() : String{
    var retorno : String = "["
    for(nodo in this){
        retorno += nodo.nombre + ", "
    }
    return "$retorno]"
}

fun ArrayList<String>?.unirString() : String {

    if(this == null){
        return "Vacio"
    }

    var retorno = ""

    for(str in this){
        retorno += str
    }

    return retorno
}