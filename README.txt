# [Nombre del proyecto]
> [Una línea que explica qué hace el proyecto. Clara, directa, sin tecnicismos innecesarios.]

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
                    
comandos:
 ...main.jar "path_original" //genera un arbol
 ...main.jar "path_original" "borrar" "palabra" 				//elimina esa palabra de todo el arbol (falta implementar)
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
├── out/
│   └── production/
│       └── creador-arquitectura/
│           ├── META-INF/
│           │   └── creador-arquitectura.kotlin_module
│           └── src/
│               ├── Arbol.class
│               ├── MainKt.class
│               └── Nodo.class
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
### out/
### README.txt
### src/

## out/
### production/

## production/

## creador-arquitectura/

## creador-arquitectura/
### META-INF/

## src/

## META-INF/
### creador-arquitectura.kotlin_module

## src/
### Arbol.class
### MainKt.class
### Nodo.class

## src/
### Arbol.kt
### Extensiotes.kt
### main.jar
### Main.kt
### Nodo.kt

---
## Contribuir[Habla de como la gente puede contribuir]