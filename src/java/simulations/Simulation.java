package src.java.simulations;

import src.java.utilities.ReadFile;
import src.java.entities.Process;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Queue;
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

    /**
    *   Simulación del algoritmo First-Come, First-Served donde los procesos
    *   son asignados al CPU conforme al orden que se le solicita, teniendo
    *   una sola cola de readyProcess.
    *
    *   @param processes lista de procesos a ejecutar
    */
    public static void simulateFCFS(List<Process> processes){
        int NEXT_PROCESS_THAT_ARRIVES = 0;
        int cpu_timer = 0;
        Queue<Process> readyProcessQueue = new LinkedList<>();
        List<Process> processesExecuted = new ArrayList<>();
        Process processInProgress = null;

        while(true){

            if(processes.isEmpty() && readyProcessQueue.isEmpty() && processInProgress == null){
                System.out.println("Tiempo de ejecución: " + cpu_timer);
                System.out.println(processesExecuted.toString());
                return;
            }

            if(!processes.isEmpty()){ // Se verifica que sigan llegando procesos
                // Se verifica la llegada del siguiente proceso
                Process nextProcess = processes.get(NEXT_PROCESS_THAT_ARRIVES);
                if(nextProcess.getArrival() == cpu_timer){
                    readyProcessQueue.add(nextProcess);
                    processes.remove(0);
                }
            }

            // En caso que no haya ningun proceso en ejecución, se toma el siguiente en la cola
            if(processInProgress == null){
                processInProgress = readyProcessQueue.poll();
                processInProgress.setStartTime(cpu_timer); // Primer instante en que se ejecuta el proceso
                processInProgress.setWaitingTime(cpu_timer - processInProgress.getArrival()); // Tiempo de espera del proceso
                processInProgress.setResponseTime(cpu_timer - processInProgress.getArrival());
            }

            // Ejecutamos el proceso
            processInProgress.executeProcess();

            if(processInProgress.getRemaining() == 0){
                processInProgress.setFinishTime(cpu_timer + 1);
                processInProgress.setTurnaround(processInProgress.getFinishTime() - processInProgress.getArrival());
                processesExecuted.add(processInProgress);
                processInProgress = null;
            }

            cpu_timer++;
        }
    }


    // Método main
    public void main(){

        // Cargamos los procesos del archivo Procesos.txt
        List<Process> processes = ReadFile.readProcessFile("files/Procesos.txt", MAX_PROCESSES);

        // Mostramos los procesos cargados
        for(Process process : processes){
            System.out.print(process.toString());
        }

        simulateFCFS(processes);

    }

}
