package src.java.simulations;

import src.java.utilities.ReadFile;
import src.java.entities.Process;
import src.java.entities.User;
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
*   , Multilevel queues (Múltiples colas por prioridad) y Fair-Share Scheduling
*   (por usuario/grupo)
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
                System.out.println("\n\nYa no hay ningun proceso a ejecutar. Tiempo de ejecución: " + cpu_timer);
                System.out.println("Resumen: " + processesExecuted.toString());
                return;
            }

            while(!processes.isEmpty() && processes.get(NEXT_PROCESS_THAT_ARRIVES).getArrival() == cpu_timer){ // Se verifica que sigan llegando procesos
                // Agregamos el proceso a la cola que corresponde por su prioridad
                Process nextProcess = processes.remove(NEXT_PROCESS_THAT_ARRIVES);
                readyProcessesQueues.get(nextProcess.getPriority()).add(nextProcess);
                System.out.println("Llego el proceso " + nextProcess.getID() + " time: " + cpu_timer);
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
                    System.out.println("Primera vez que se ve al proceso " + processInProgress.getID());
                    processInProgress.setStartTime(cpu_timer); // Primer instante en que se ejecuta el proceso
                    processInProgress.setWaitingTime(cpu_timer - processInProgress.getArrival()); // Tiempo de espera del proceso
                    processInProgress.setResponseTime(cpu_timer - processInProgress.getArrival());
                }else{
                    System.out.println("Se reanuda la ejecución del proceso " + processInProgress.getID());
                    processInProgress.setWaitingTime(processInProgress.getWaitingTime() + (cpu_timer - (processInProgress.getStartTime() + (processInProgress.getBurst() - processInProgress.getRemaining())))); // Tiempo de espera del proceso
                }
            }

            // Ejecutamos el proceso
            if(processInProgress != null){
                System.out.println("Se ejecuta el proceso " + processInProgress.getID());
                processInProgress.executeProcess();
                processQuantum--; // Reducimos el quantum del proceso

                if(processInProgress.getRemaining() == 0){
                    System.out.println("El proceso " + processInProgress.getID() + " termino de ejecutarse");
                    processInProgress.setFinishTime(cpu_timer + 1);
                    processInProgress.setTurnaround(processInProgress.getFinishTime() - processInProgress.getArrival());
                    processesExecuted.add(processInProgress);
                    processInProgress = null;
                }else if(processQuantum == 0) { // En caso de que el quantum del proceso se haya agotado, el proceso se regresa a su cola de prioridad
                    System.out.println("Se acabo el quantum del proceso " + processInProgress.getID() + " se ingresa nuevamente a la cola de prioridad " + processInProgress.getPriority());
                    readyProcessesQueues.get(processInProgress.getPriority()).add(processInProgress);
                    processInProgress = null;
                }
            }
            cpu_timer++;
        }
    }

    /**
    *   Simulación del algoritmo Fair-Share Scheduling (por usuario), se ejecuta el proceso
    *   que pertenece al usuario en turno, en caso de el usuario no tener procesos, se cede
    *   el turno al siguiente usuario, cada usuario tiene un quantum, si se acaba su tiempo
    *   para ejecutar, el proceso se regreasa la cola de procesos del usuario y el turno se
    *   asigna al siguiente usuario con procesos en su cola de procesos.
    *
    *   @param processes lista de procesos a ejcutar
    *   @param quantum tiempo que un usuario puede ejecutar
    *   @param numberOfUsers número de usuarios en la simulación
    */
    public void simulateFairShareScheduling(List<Process> processes, int quantum, int numberOfUsers){
        int NEXT_PROCESS_THAT_ARRIVES = 0;
        int cpu_timer = 0; // Contador del CPU
        int userQuantum = 0; // Quantum
        int userTurn = 1; // Usuario a quien le toca usar la CPU

        Process processInProgress = null;
        Map<Integer, User> users = new HashMap<>();
        List<Process> processesExecuted = new ArrayList<>();

        for(int i = 1; i <= numberOfUsers; i++){
            users.put(i, new User(i));
        }

        while(true){

            boolean emptyQueues = users.values().stream().allMatch(User::isUserQueueEmpty); // Verificamos si las colas están vacias
            if(processes.isEmpty() && emptyQueues && processInProgress == null){
                System.out.println("Tiempo de ejecución: " + cpu_timer);
                System.out.println(processesExecuted.toString());
                return;
            }

            while(!processes.isEmpty() && processes.get(NEXT_PROCESS_THAT_ARRIVES).getArrival() == cpu_timer){ // Se verifica que sigan llegando procesos
                // Agregamos el proceso a la cola de procesos del usuario al que corresponde
                Process nextProcess = processes.remove(NEXT_PROCESS_THAT_ARRIVES);
                users.get(nextProcess.getUser()).addProcessToUser(nextProcess); // Agregamos el proceso a la cola del usuario a la que pertenece
                System.out.println("Llego el proceso " + nextProcess.getID() + " que pertenece al usuario " + nextProcess.getUser());
            }

            // Asignamos el turno para ejecutar al usuario que tenga procesos activos
            if(processInProgress == null){
                if(userQuantum == 0){
                    int usersChecked = 0;
                    while(usersChecked < numberOfUsers){
                        if(!users.get(userTurn).isUserQueueEmpty()){
                            processInProgress = users.get(userTurn).pollUserProcess();
                            userQuantum = quantum;
                            System.out.println("\nEl usuario " + userTurn + " tiene el proceso " + processInProgress.getID() + " por ejecutar, se le asigna el turno.");
                            break;
                        }
                        userTurn = (userTurn % numberOfUsers) + 1;
                        usersChecked++;
                    }
                } else{
                    processInProgress = users.get(userTurn).pollUserProcess();
                }
                if(processInProgress != null){
                    if(processInProgress.getBurst() == processInProgress.getRemaining()){
                        System.out.println("Es la primera vez que se ve al proceso " + processInProgress.getID() + " del usuario " + userTurn);
                        processInProgress.setStartTime(cpu_timer);
                        processInProgress.setWaitingTime(cpu_timer - processInProgress.getArrival());
                        processInProgress.setResponseTime(cpu_timer - processInProgress.getArrival());
                    } else {
                        System.out.println("Se reanuda la ejecución del proceso " + processInProgress.getID() + " del usuario " + userTurn);
                        processInProgress.setWaitingTime(processInProgress.getWaitingTime() + (cpu_timer - (processInProgress.getStartTime() + (processInProgress.getBurst() - processInProgress.getRemaining()))));
                    }
                }
            }

            if(processInProgress != null){
                System.out.println("Se ejecuta el proceso " + processInProgress.getID() + " del usuario " + userTurn);
                processInProgress.executeProcess();
                userQuantum--;

                // Si el proceso termina
                if(processInProgress.getRemaining() == 0){
                    System.out.println("El proceso " + processInProgress.getID() + " del usuario " + userTurn + " se termino de ejecutar");
                    processInProgress.setFinishTime(cpu_timer + 1);
                    processInProgress.setTurnaround(processInProgress.getFinishTime() - processInProgress.getArrival());
                    processesExecuted.add(processInProgress);

                    processInProgress = null;
                    userTurn = (userTurn % numberOfUsers) + 1; // Pasamos el turno al siguiente usuario

                }
                // Si se acaba el quantum del usuario
                else if(userQuantum == 0) {
                    // REGRESAMOS EL PROCESO A SU DUEÑO (sin importar el userTurn)
                    users.get(processInProgress.getUser()).addProcessToUser(processInProgress);

                    processInProgress = null;
                    userTurn = (userTurn % numberOfUsers) + 1; // Pasamos el turno al siguiente usuario
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
        //simulateMultilevelQueues(processes, 5, 5);
        simulateFairShareScheduling(processes, 2, 5);

    }

}
