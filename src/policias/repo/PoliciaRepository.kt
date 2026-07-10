package src.policias.repo

import com.google.gson.Gson
import src.policias.dominio.Policia
import src.policias.dominio.PoliciaArbol
import src.utils.Rutas
import java.nio.file.Files
import java.nio.file.Path

class PoliciaRepository(
    val rutaGuardadoArbol : Path = Rutas.carpetaPolicias.resolve("PoliciaArbol.json")
) {

    private val gson = Gson()

    fun cargarPoliciaArbol() : PoliciaArbol?{
        if(!Files.exists(rutaGuardadoArbol)) return null
        val json = Files.readString(rutaGuardadoArbol)
        return gson.fromJson(json, PoliciaArbol::class.java)
    }

    fun guardarPoliciaArbol(policiaArbol: PoliciaArbol) {
        Files.createDirectory(rutaGuardadoArbol.parent)
        val json = gson.toJson(policiaArbol)
        Files.writeString(rutaGuardadoArbol, json)
    }
    fun cargarListaPoliciaBorrar(){

    }
    fun cargarListaPoliciaReemplazar(){

    }
    fun cargarListaPoliciaAplazar(){

    }
}