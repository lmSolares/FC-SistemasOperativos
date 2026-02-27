package src.java.simulations;

import src.java.utilities.ReadFile;
import src.java.entities.Process;
import java.util.List;

/**
*   Clase con la implementación de una simulación de técnicas de planificación
*   de procesos (CPU scheduling) basada en el capítulo de Procesos e Hilos
*   de Modern Operating Systems, 5th Ed (sección 2.4 Scheduling), se
*   implementan especificamente los algoritmos: First-Come, First-Served (FCFS)
*   y Multilevel queues (Múltiples colas por prioridad).
*/
public class Simulation{

    // Número máximo de procesos
    private int MAX_PROCESSES = 200;

    public static void simulateFCFS(){

    }


    // Método main
    public void main(){

        // Cargamos los procesos del archivo Procesos.txt
        List<Process> processes = ReadFile.readProcessFile("files/Procesos.txt", MAX_PROCESSES);

        // Mostramos los procesos cargados
        for(Process process : processes){
            System.out.println(process.toString());
        }


    }

}
