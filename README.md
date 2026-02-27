# Sistemas Operativos
Repositorio con las actividades del curso de Sistemas Operativos 2026-2 de la Facultad de Ciencias.

## Equipo
* Solares Ramos Luis Mario: lm.solares@ciencias.unam.mx
* Fuentes Anica Cesar Candelario: mdcesar@ciencias.unam.mx

## Estructura del repositorio
El repositorio está dividido en:

* __src:__ Directorio principal donde se encuentra el código de todas las actividades, este se divide por lenguaje, por ejemplo, en *src/java* está el código en Java de las actividades que se hicieron en ese lenguaje.
* __files:__  Directorio con archivos necesarios para las actividades (ejem. lista de procesos)

## Actividades

### T2_Modulo Kernel
En está actividad se realizo un módulo del kernel de Linux que crea */proc/counter*, y devuelve cuántas veces se ha leido ese archivo desde que se cargo el módulo.

* Código del módulo en: *src/c/linux/counter.c* 

### T3_Planificación_Procesos
En está actividad se realizo la implementación de 3 simulaciones, simulando los siguientes 
algortimos:
* First-Come, First-Served (FCFS)
* Multilevel queues (Múltiples colas por prioridad)
* Fair-Share Scheduling (por usuario/grupo)

Las simulaciones se realizaron en el lenguaje de programación Java, para su compilación y ejecución:

* Compilación: *javac -d build -cp build src/java/simulations/Simulation.java*
* Ejecución: *java -cp build src.java.simulations.Simulation*

*Nota:* Para simular uno o varios algoritmos, de deben de descomentar del método main en *src/java/simulations.Simulation.java*, al igual si se desea reducir el número máximo de procesos a leer, se debe modificar *MAX_PROCESSES*. Si se desea agregar más procesos se debe hacer en *files/Procesos.txt*, con el formato *id arrival burst priority/user*, cada proceso en una linea, para mejor lectura. (priority y user por comodidad son el mismo)
