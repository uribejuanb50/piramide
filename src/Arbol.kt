package src

import java.io.File


class Arbol (val path : File){

    val raiz = Nodo(path.name, path)
    var profundidad : Int = 0

    val mapaCondiciones = mapOf(
        "exacto" to { str1: String, str2: String -> str1 == str2},
        "conteniendo" to {str1, str2 -> str1.contains(str2)}
    )

    lateinit var mdreadme : ArrayList<String> //para programación dinámica

    fun iniciarMDReadME(espacio : Int){
        this.mdreadme = ArrayList(List (espacio) {""})
    }

    //alimenta el arbol
    fun crearSubDirectorios() : Unit {
        this.raiz.crearSubDirectorios()
    }
    //solo para practicar
    fun nPalabraMasLarga() : Int {
        return this.raiz.calcularMedidaPalabraMasLarga()
    }

    fun reversarListas(){
        this.raiz.reversarListas()
    }

    //la profundidad total del arbol
    fun calcularProfundidad() : Int{
        val profundidad = this.raiz.calcularProfundidad()
        this.profundidad = profundidad
        return profundidad
    }
    //el baseline
    fun generarArquitecturaSencilla(carcter: String, repeticion : Int) : String {
        return this.raiz.impresionUltraSencilla(espacio = carcter.repeat(repeticion))
    }
    //el que genera con rayitas y bonito con eso
    fun generarArquitectura(profundidad : Int, mostrarOcultos : Boolean = false, profundidadMax : Int? = null) : String {
        this.iniciarMDReadME(profundidad)
        return this.raiz.generarArquitectura(this.mdreadme, mostrarEscondidos = mostrarOcultos, profundidadMax = profundidadMax)
    }

    //nombre intuitivo no?
    fun buscarArchivosPorNombre(busqueda : String, condicion : String?) : String{
        val condicion = condicion ?: "exacto"

        return try {
            val metodoBusqueda = this.mapaCondiciones.getValue(condicion)
            this.raiz.buscarArchivosPorNombreYCondicion(busqueda, metodoBusqueda).toCustomString()
        } catch (e: Exception) {
            throw IllegalArgumentException("[Arbol] Las condiciones de busquedas actuales son ${this.mapaCondiciones.keys}")
        }
    }

    //agarra de a niveles y va explicandolos empezando por el más hacia la raiz
    fun organizarDescripciones(ocultos : Boolean = false) : String {

        val mdinamica = mutableSetOf<String>()
        val directorios = this.raiz.devolverPadresConHijos(mostrarEscondidos = ocultos)

        var retorno = "## " + directorios.first() +"\n"
        mdinamica.add(directorios.first())

        for(directorio in directorios.drop(1)){
            if(directorio in mdinamica)
                retorno += "\n## $directorio\n"

            else
                retorno += "### $directorio\n"
                mdinamica.add(directorio)
        }

        return retorno
    }

    //hace la estructura de un readme
    fun generarREADME(arquitectura : String, descripcion : String) : String{
        var readme = ""
        readme += "# [Nombre del proyecto]\n"
        readme += "> [Una línea que explica qué hace el proyecto. Clara, directa, sin tecnicismos innecesarios.]\n"
        readme += "\n"
        readme += "![[aqui va texto entre corchetes que es la desc de la imagen (obligatoria por SEO)]]([link imagen]\n"
        readme += "\n"
        readme += "---"
        readme += "\n"
        readme += "## DEMO"
        readme += "[link a demo o bloque de UI o respuesta de terminal (```shell //codigo// ```]"
        readme += "\n"
        readme += "---"
        readme += "\n"
        readme += "## Configuracion"
        readme += "[Solo si aplica. Variables de entorno, archivos de config, flags, etc.]"
        readme += "\n"
        readme += "---"
        readme += "\n"
        readme += "## Estructura del proyecto"
        readme += "\n"
        readme += arquitectura
        readme += "\n"
        readme += descripcion
        readme += "\n"
        readme += "---"
        readme += "\n"
        readme += "## Contribuir"
        readme += "[Habla de como la gente puede contribuir]"

        return readme
    }

    //elimina esa palabra hasta cierta cantidad de niveles
    fun eliminarPalabra(palabra : String, nivelMax : Int? = null){
        this.raiz.eliminarPalabra(palabra, nivelMax)
    }
}

