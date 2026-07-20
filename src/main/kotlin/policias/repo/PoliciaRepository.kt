package piramide.policias.repo

import com.google.gson.*
import piramide.policias.dominio.PoliciaArbol
import piramide.policias.dominio.PoliciaReemplazados
import piramide.policias.factories.PoliciaFactory
import piramide.policias.factories.RegistroFactory
import piramide.utils.Rutas
import piramide.utils.fromJsonList
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import java.time.LocalTime

class PoliciaRepository(

    val rutaGuardadoObjetoArbol : Path =
        Rutas.carpetaPolicias
            .resolve("PoliciaArbol.json")//para el objeto unico policiaarbol
            .also { if (Files.notExists(it)) Files.createFile(it) }, //crea el archivo si no existe

    val rutaGuardadoObjetoReemplazados : Path =
        Rutas.carpetaPolicias
            .resolve("PoliciasReemplazados.json") //para el arraylist policiasreemplazados
            .also { if (Files.notExists(it)) Files.createFile(it) }, //esto crea es la carpeta de guardado del objeto arbol
                                                                                    //no de los registros
    val rutaGuardadoRespaldosReemplazados : Path =
        Rutas.carpetaReemplazados
            .also { if (Files.notExists(it)) Files.createFile(it) },

    val registroFactory: RegistroFactory = RegistroFactory
) {

    private val gson = GsonBuilder()
        .registerTypeHierarchyAdapter(LocalDate::class.java, JsonSerializer<LocalDate> { src, _, _ ->
            JsonPrimitive(src.toString())
        })
        .registerTypeHierarchyAdapter(LocalDate::class.java, JsonDeserializer<LocalDate> { json, _, _ ->
            LocalDate.parse(json.asString)
        })
        .registerTypeHierarchyAdapter(LocalTime::class.java, JsonSerializer<LocalTime> { src, _, _ ->
            JsonPrimitive(src.toString())
        })
        .registerTypeHierarchyAdapter(LocalTime::class.java, JsonDeserializer<LocalTime> { json, _, _ ->
            LocalTime.parse(json.asString)
        })
        .registerTypeHierarchyAdapter(Path::class.java, JsonSerializer<Path> { src, _, _ ->
            JsonPrimitive(src.toAbsolutePath().toString())
        })
        .registerTypeHierarchyAdapter(Path::class.java, JsonDeserializer<Path> { json, _, _ ->
            Path.of(json.asString)
        })
        .setPrettyPrinting() // Opcional: formatea el JSON con sangrías para que sea legible
        .create()

    fun cargarPoliciaArbol() : PoliciaArbol?{

        if(!Files.exists(rutaGuardadoObjetoArbol)) return null
        val json = Files.readString(rutaGuardadoObjetoArbol)
        val policiaArbolDTO = gson.fromJson(json, PoliciaArbolDTO::class.java)

        try{
            val policiaArbol = PoliciaArbol(
                policiaArbolDTO.pathCarpetaGuardando,
                policiaArbolDTO.id,
                policiaArbolDTO.tipo, registroFactory,
                policiaArbolDTO.pathRaizArbolUsando
            )
            policiaArbol.listaRegistro.addAll(policiaArbolDTO.listaRegistro)
            return policiaArbol
        }catch(e : Exception){
            println("[PoliciaRepository] Lectura del archivo tiró error, probablemente corrupto o manipulado.\n" +
                    "                    Lanzando plantilla PoliciaArbol por defecto")
            return null
        }
    }

    fun guardarPoliciaArbol(policiaArbol: PoliciaArbol) { //Aquí deberiamos enviar es el dto, igual al cargar solo se lee lo que los campos en el que el dto se llama igual
        Files.createDirectories(rutaGuardadoObjetoArbol.parent)
        val json = gson.toJson(policiaArbol)
        Files.writeString(rutaGuardadoObjetoArbol, json)
    }

    fun cargarListaPoliciaReemplazados() : ArrayList<PoliciaReemplazados> {
        if(!Files.exists(rutaGuardadoObjetoReemplazados)) return arrayListOf()

        val json = Files.readString(rutaGuardadoObjetoReemplazados)

        if(json.isBlank()) return arrayListOf()

        val arrayListDTO = gson.fromJsonList<PoliciaReemplazadosDTO>(json)

        try{
            return arrayListDTO
                .map{ dto ->
                    PoliciaReemplazados(
                        dto.pathCarpetaGuardando,
                        dto.id,
                        dto.tipo,
                        rutaGuardadoRespaldosReemplazados,
                        registroFactory
                    ).also {
                        it.listaRegistro.addAll(dto.listaRegistro)
                    }
                }
                .toCollection(ArrayList())
        }
        catch (e : Exception){
            println("[PoliciaRepository] Lectura del archivo tiró error, probablemente corrupto o manipulado.\n" +
                    "                    RetornandoListaVacia")

            return arrayListOf()
        }

    }

    fun guardarListaPoliciaReemplazados( listaPoliciaReemplazados: ArrayList<PoliciaReemplazados>){
        Files.createDirectories(rutaGuardadoObjetoReemplazados.parent)
        val json = gson.toJson(listaPoliciaReemplazados)
        Files.writeString(rutaGuardadoObjetoReemplazados, json)
    }

    fun cargarListaPoliciaAplanados(){

    }
}