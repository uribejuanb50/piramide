package piramide.utils

import piramide.arbol.Nodo
import java.io.File
import java.nio.file.Path
import java.time.LocalDate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import piramide.policias.dominio.PoliciaReemplazados

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

@JvmName("toCustomStringArrayListPairAnyString")
fun ArrayList<Pair<Any, String>>?.toCustomString() : String {

    if(this == null)
        return "[Vacio]"

    var retorno = "["

    for((indice, pareja) in this.withIndex()){
        if(indice != lastIndex){
            retorno += "[" +
                when(pareja.first){
                    is Path -> {
                        val path = pareja.first as Path
                        path.toAbsolutePath()
                    }
                    is Long -> {
                        val long = pareja.first as Long
                        long.toString()
                    }
                    is LocalDate -> {
                        val fecha = pareja.first as LocalDate
                        fecha.toString()
                    }
                    else -> "F.Desc"
                }
            retorno += ", ${pareja.second}], "
        }
        else{
            retorno += "[" +
                    when(pareja.first){
                        is Path -> {
                            val path = pareja.first as Path
                            path.toAbsolutePath()
                        }
                        is Long -> {
                            val long = pareja.first as Long
                            long.toString()
                        }
                        is LocalDate -> {
                            val fecha = pareja.first as LocalDate
                            fecha.toString()
                        }
                        else -> "F.Desc"
                    }
            retorno += ", ${pareja.second}]"
        }
    }
    return "$retorno]"
}

@JvmName("toCustomStringArrayListPoliciaReemplazados")
fun ArrayList<PoliciaReemplazados>.toCustomString() : String {

    if(this.isEmpty()) return "[Vacío]"

    var retorno = "[\n"

    for((indice, policia) in this.withIndex()){

        if(indice != this.lastIndex){
            retorno += "${policia.toCustomString()}\n----------------------------------\n"
        }
        else{
            retorno += policia.toCustomString() + "\n"
        }
    }

    return "$retorno]"
}




//MAP -----------------------------------------------------------------
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



//Mutable list
@JvmName("toCustomStringMutableListNodo")
fun MutableList<Nodo>.toCustomString() : String{
    var retorno : String = "["
    for(nodo in this){
        retorno += nodo.nombre + ", "
    }
    return "$retorno]"
}


//Extension de gson para arraylists
inline fun <reified T> Gson.fromJsonList(json : String) : ArrayList<T> {
    val type = object : TypeToken<ArrayList<T>>() {}.type
    return this.fromJson(json, type)
}
