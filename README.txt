# Piramide
> Navega a través del directorio que le indiques y permite visualizarlo y alterarlo, como se ven los archivos parecen piramides

![[aqui va texto entre corchetes que es la desc de la imagen (obligatoria por SEO)]]([link imagen]

---
## DEMO[link a demo o bloque de UI o respuesta de terminal (```shell //codigo// ```]
---
## Configuracion[Solo si aplica. Variables de entorno, archivos de config, flags, etc.]

flags:
 --simple                    	//imprime el arbol sin caracteres especiales
 --ocultos                   	//mostrar carpetas y archivos ocultos
 --reversar                  	//reversar las listas internas y mostrar primero archivos que directorios
 --recortar n                	//recorta palabras hasta ese tamaño (falta añadir compatibilidad con --simple usar regex distintos)
 --prueba                    	//lanzar prueba
 --nivelMax n                	//hacerlo hasta el nivel max (0 es nada, 1 es la raiz, 2 el segundo nivel ...)
 --README desc               	//crear README (desc es opcional para activar la descripcion)
 --toArchivo "path_archivo"  	//guardar en archivo (borra el contenido)
 --ayuda                     	//Imprime este txt

 //Hacer un generador de reglas con la posibilidad de hacer tus propios ejemplos y probar tu regla pero antes arreglar lo que falta
 //no paila está re dificil, dejar de últimas

//antes de desarrollar policias, limpiar el main con validadores de los datos provenientes de terminal
comandos:
 ...main.jar "path_original" //genera un arbol
 ...main.jar "path_original" "borrar" "palabra" 				//elimina esa palabra de todo el arbol (falta implementar) //meterle un regex y un respaldo
 ...main.jar "path_original" "buscar" "palabra" --condicion "condicion" 	//busca archivo que contenga esa palabra
                    						        	//lo busca exacto o conteniendo, tiene que tener un flag, o por default
                    								// sale exacto
 ...main.jar "path_original" "reemplazar" "palabraVieja" "palabraNueva"  	//reemplaza palabra dentro de cada archivo

compilar:
 kotlinc *.kt -include-runtime -d main.jar
---
## Estructura del proyecto
creador-arquitectura/
├── .gitignore
├── creador-arquitectura.iml
├── README.txt
└── src/
    ├── Arbol.kt
    ├── Extensiotes.kt
    ├── main.jar
    ├── Main.kt
    └── Nodo.kt

## creador-arquitectura/
### .gitignore
### creador-arquitectura.iml
### README.txt
### src/

## src/
### Arbol.kt
### Extensiotes.kt
### main.jar
### Main.kt
### Nodo.kt

---
## Contribuir[Habla de como la gente puede contribuir]