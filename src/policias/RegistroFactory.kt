package src.policias

import java.nio.file.Path

object RegistroFactory {

    fun generarRegistroArbol(
        ultimoID : Long,
        tipo : String,
        descripcion : String?,
        pathRaiz : Path
    ) : Registro  {
        return Registro(
            id = ultimoID + 1,
            tipo = tipo,
            desc = descripcion ?:"N/A descripción",
            pathOriginal = pathRaiz
        )
    }

    fun generarRegistroBorrar(
        ultimoID: Long,
        tipo : String,
        descripcion : String,
        pathOriginal : Path,
        pathActual : Path,
        palabraBorrada : String
    ) : Registro {
        return Registro(
            id = ultimoID + 1,
            tipo = tipo,
            desc = descripcion ?:"N/A descripción",
            pathOriginal = pathOriginal,
            pathActual = pathActual,
            palabraBorrada = palabraBorrada
        )
    }

    fun generarRegistroReemplazar(
        ultimoID: Long,
        tipo : String,
        descripcion : String,
        pathOriginal : Path,
        pathActual : Path,
        palabraBorrada : String,
        palabraNueva : String
    ) : Registro {
        return Registro(
            id = ultimoID + 1,
            tipo = tipo,
            desc = descripcion ?:"N/A descripción",
            pathOriginal = pathOriginal,
            pathActual = pathActual,
            palabraBorrada = palabraBorrada,
            palabraNueva = palabraNueva
        )
    }

    fun generarRegistroAplazar(
        ultimoID: Long,
        tipo : String,
        descripcion : String,
        pathOriginal : Path,
        pathActual : Path,
    ) : Registro {
        return Registro(
            id = ultimoID + 1,
            tipo = tipo,
            desc = descripcion ?:"N/A descripción",
            pathOriginal = pathOriginal,
            pathActual = pathActual
        )
    }
}

//hacer un factory de dataclasses y se pasa por constructor del policiafactory, así cada policia tiene un factory de dataclasses
//el policia se asigna en el arbol