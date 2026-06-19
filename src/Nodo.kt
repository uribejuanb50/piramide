package src

import java.io.File

class Nodo (val nombre : String, val path : File) {

    val listaSubArchivos: MutableList<Nodo?> = mutableListOf()
    var letrasPalabraMasLarga : Int = 0

    fun crearSubdirectorios(path: File): Nodo? {

        val directorio = path.listFiles()

        if(directorio == null || directorio.isEmpty()){
            return null
        }

        if (path.isFile) {
            val archivo = Nodo(path.name, path)
            return archivo
        }

        val iterar : (path : File) -> Unit = { estructura ->
            val subArchivo = crearSubdirectorios(estructura)
            listaSubArchivos.add(subArchivo)
        }

        directorio.forEach(iterar)

        val subArchivo = Nodo(path.name, path)
        subArchivo.listaSubArchivos.addAll(this.listaSubArchivos.toMutableList())

        return subArchivo
    }

    //se puede mejorar usando el this
    fun calcularMedidaPalabraMasLarga() : Int{
        if(this.listaSubArchivos.isEmpty()){
            return 0
        }
        if(this.listaSubArchivos.size == 1) {
            return this.validarArchivo()
        }

        var palabraMasGrande : Int = 0

        for(nodo in this.listaSubArchivos){
            if(nodo == null){
                continue
            }
            val mayorSubDirectorio = calcularMedidaPalabraMasLarga()
            palabraMasGrande = maxOf(mayorSubDirectorio, this.validarArchivo())
        }
        return palabraMasGrande
    }

    fun validarArchivo() : Int{
        if(this.path.isDirectory){
            return 0
        }
        return this.nombre.length
    }
    /*
    fun imprimirParaREADME(nivel : Int, nodo : Nodo?) : String {
        if(nodo == null) {
            return ""
        }
        if(nodo.listaSubArchivos.size == 1){
            return
        }
    }
    */
}




















