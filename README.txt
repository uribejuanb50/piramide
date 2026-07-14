# Piramide
> Navega a través del directorio que le indiques y permite visualizarlo y alterarlo, como se ven los archivos parecen piramides

![[aqui va texto entre corchetes que es la desc de la imagen (obligatoria por SEO)]]([link imagen]

---
## DEMO[link a demo o bloque de UI o respuesta de terminal (```shell //codigo// ```]
---
## Configuracion[Solo si aplica. Variables de entorno, archivos de config, flags, etc.]
chcp 65001 // para imprimir las rayas en vez de chars basura
Cambiar los dos tipos,
arbol ->
Ima (Imaa): Una hermosa y antigua variante jeroglífica para referirse a un "árbol verde, frondoso o lleno de gracia". Se usaba en la literatura egipcia para describir árboles que daban sombra reconfortante y frescura en medio del desierto.
policia -> Mena (Menat): Significa "El vigilante" o "El que se mantiene despierto". Se usaba para los centinelas nocturnos que patrullaban las murallas de los palacios y las calles de las ciudades egipcias durante la noche
borrados -> Ajem (Akhem): Significa literalmente "Los que se extinguieron" o "Lo que fue borrado". Proviene de la palabra utilizada para describir el acto de apagar una llama o hacer desaparecer una inscripción de forma permanente. Suena corto, tajante y misterioso.
reemplazados -> Sejper (Sekhper): Significa "Los Transformados" o "Los que fueron mutados en otra cosa". En el contexto del reemplazo, se refiere a un objeto, título o monumento que perdió su identidad original para convertirse en la propiedad de un nuevo dueño.
aplanados -> Medyet (Med-yet): Significa "Los Comprimidos" o "Los Compactados". Evoca el proceso de apisonar la tierra y la arena para crear las gigantescas rampas de construcción que permitían subir las piedras milenarias.

//Configurar idioma, para usar en egipcio o para suaves con idioma normal

flags: //2 tipos funcionalidad, interna de cada clase que entra por param y externa que maneja el formato de respuesta en el cli
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
 //no paila está re dificil, dejar de últimas !!! En vez de esto hacerlo según un regex

//Falta hacer los warnings de categoria
//Incluir el flag --incluir para reemplazar, así se incluiria los que empiezan con .
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