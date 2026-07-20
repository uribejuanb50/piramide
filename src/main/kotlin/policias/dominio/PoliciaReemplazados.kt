package piramide.policias.dominio

import piramide.policias.factories.PoliciaFactory
import piramide.policias.factories.RegistroFactory
import piramide.utils.escribirArchivo
import piramide.utils.toCustomString
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension

class PoliciaReemplazados(
    pathCarpetaGuardando : Path, //ruta de donde se guarda el objeto arbol -> data/policias
    id : Long, //tiene que ser el del arbol que se está usando, que viene del registro del arbol,
                //ademas el id dicta el nombre de la carpeta donde se van a guardar los respaldos respectivos a este policia
    tipo : String,
    pathCarpetaReemplazados : Path, //carpeta de reemplazados >data/reemplazados
    val registroFactory: RegistroFactory,
) : Policia(pathCarpetaGuardando, id, tipo){

    //crea la carpeta donde vamos a estar guardando los registros respectivos al arbol
    val carpetaGuardandoRespaldos : Path = pathCarpetaReemplazados.resolve(id.toString()).also{
        Files.createDirectories(it) //ej : data/reemplazados/1 ; 1 porque es el id del policia
    }

    @Transient
    override val devolverFormatoRegistro: (registro: Registro) -> String = { registro ->
        var retornar = "\n"
        retornar += "ID: ${registro.id}\n"
        retornar += "Tipo: ${registro.tipo}\n"
        retornar += "Descrpcion: ${registro.desc}\n"
        retornar += "Path original: ${registro.pathOriginal}\n"
        retornar += "Path actual: ${registro.pathActual}"
        retornar += "Fecha: ${registro.fecha}"
        retornar += "Palabra borrada: ${registro.palabraBorrada}"
        retornar += "Palabra nueva: ${registro.palabraNueva}"
        "$retornar---------------------------------------------\n"
    }

    //pathnuevo: id-arbol-$id-id-registro-$id-palabra-antigua-$palabraAntigua-palabra-nueva
    fun nuevoSeguimiento(stringOriginal : String, palabraAntigua : Regex, palabraNueva : String, pathOriginal : Path) {

        val idRegistro =
            if(!this.listaRegistro.isEmpty()) this.listaRegistro.last().id
            else 0

        val descripcion = "palabra antigua: $palabraAntigua | palabra nueva : $palabraNueva | archivo originario : ${pathOriginal.toAbsolutePath()}"
        val extension = pathOriginal.extension
        val respaldoGenerado =
            carpetaGuardandoRespaldos
                .resolve(
                    "ida-arbol-$id-idr-registro-${idRegistro + 1}.$extension"
                )
                .also{
                    if(Files.notExists(it)) Files.createFile(it)
                }

        val nuevoRegistro = registroFactory.generarRegistroReemplazar(
            idRegistro,
            "reemplazo",
            descripcion,
            pathOriginal,
            respaldoGenerado,
            palabraAntigua.toString(),
            palabraNueva
        )

        escribirArchivo(stringOriginal, respaldoGenerado.toFile())

        this.listaRegistro.add(nuevoRegistro)
    }


    @JvmName("toStringClassPoliciaReemplazados")
    fun toString(mostrarLista : Boolean = false) : String{
        var retorno = ""

        retorno += "carpetaGuardandoRespaldos: ${carpetaGuardandoRespaldos.toAbsolutePath()?:"null"}\n"
        retorno += "pathCarpetaGuardando: ${pathCarpetaGuardando.toAbsolutePath()}\n"
        retorno += "id : $id\n"
        retorno += "tipo : $tipo\n"
        retorno +=
            if(mostrarLista)
                this.listaRegistro.map(devolverFormatoRegistro).toCollection(ArrayList()).toCustomString()
            else
                ""

        return retorno
    }
    override fun devolverFormatoListaRegistro(listaRegistro: ArrayList<Registro>): ArrayList<String> {
        TODO("Not yet implemented")
    }

    override fun transdormarListaToSetPorAtributo(lista: ArrayList<Registro>): Set<Path> {
        TODO("Not yet implemented")
    }
}