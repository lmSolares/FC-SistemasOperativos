package src.java.simulations;

import src.java.utilities.ReadFile;
import src.java.entities.Process;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Queue;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

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

            while(!processes.isEmpty() && processes.get(NEXT_PROCESS_THAT_ARRIVES).getArrival() == cpu_timer){ // Se verifica que sigan llegando procesos
                // Se verifica la llegada del siguiente proceso
                Process nextProcess = processes.get(NEXT_PROCESS_THAT_ARRIVES);
                readyProcessQueue.add(processes.remove(0));
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

    /**
    *   Simulación del algoritmo Múltiples colas por prioridad (Multilevel queues),
    *   se ejecuta siempre la cola de mayor prioridad (1 es la de menor prioridad),
    *   además hay RR dentro de cada cola.
    *
    *   @param processes lista de procesos a ejecutar
    *   @param quantum valor quantum
    *   @param numberOfQueues número de colas de prioridad
    */
    public static void simulateMultilevelQueues(List<Process> processes, int quantum, int numberOfQueues){
        int NEXT_PROCESS_THAT_ARRIVES = 0;
        int processQuantum = quantum;
        int cpu_timer = 0;

        List<Process> processesExecuted = new ArrayList<>();
        Process processInProgress = null;
        Map<Integer, Queue<Process>> readyProcessesQueues = new HashMap<>(); //Inicializamos las colas de prioridad

        for(int i = 1; i <= numberOfQueues; i++){
            readyProcessesQueues.put(i, new LinkedList<>());
        }

        while(true){

            boolean emptyQueues = readyProcessesQueues.values().stream().allMatch(Queue::isEmpty); // Verificamos si las colas están vacias
            if(processes.isEmpty() && emptyQueues && processInProgress == null){
                System.out.println("Tiempo de ejecución: " + cpu_timer);
                System.out.println(processesExecuted.toString());
                return;
            }

            while(!processes.isEmpty() && processes.get(NEXT_PROCESS_THAT_ARRIVES).getArrival() == cpu_timer){ // Se verifica que sigan llegando procesos
                // Agregamos el proceso a la cola que corresponde por su prioridad
                Process nextProcess = processes.remove(NEXT_PROCESS_THAT_ARRIVES);
                readyProcessesQueues.get(nextProcess.getPriority()).add(nextProcess);
            }

            // En caso que no haya ningun proceso en ejecución, se toma el siguiente en la cola (tomando en cuenta la prioridad)
            if(processInProgress == null){
                for(int i = readyProcessesQueues.size(); i >= 1; i--){
                    if(!readyProcessesQueues.get(i).isEmpty()){
                        processInProgress = readyProcessesQueues.get(i).poll();
                        processQuantum = quantum;
                        break;
                    }
                }
                if(processInProgress.getBurst() == processInProgress.getRemaining()){
                    processInProgress.setStartTime(cpu_timer); // Primer instante en que se ejecuta el proceso
                    processInProgress.setWaitingTime(cpu_timer - processInProgress.getArrival()); // Tiempo de espera del proceso
                    processInProgress.setResponseTime(cpu_timer - processInProgress.getArrival());
                }else{
                    processInProgress.setWaitingTime(processInProgress.getWaitingTime() + (cpu_timer - (processInProgress.getStartTime() + (processInProgress.getBurst() - processInProgress.getRemaining())))); // Tiempo de espera del proceso
                }
            }

            // Ejecutamos el proceso
            if(processInProgress != null){
                processInProgress.executeProcess();
                processQuantum--; // Reducimos el quantum del proceso

                if(processInProgress.getRemaining() == 0){
                    processInProgress.setFinishTime(cpu_timer + 1);
                    processInProgress.setTurnaround(processInProgress.getFinishTime() - processInProgress.getArrival());
                    processesExecuted.add(processInProgress);
                    processInProgress = null;
                }else if(processQuantum == 0) { // En caso de que el quantum del proceso se haya agotado, el proceso se regresa a su cola de prioridad
                    readyProcessesQueues.get(processInProgress.getPriority()).add(processInProgress);
                    processInProgress = null;
                }
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

        //simulateFCFS(processes);
        simulateMultilevelQueues(processes, 5, 5);

    }

}
