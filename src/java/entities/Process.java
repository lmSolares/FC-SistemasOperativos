package src.entities;

/**
*   Clase que representa un proceso
*/
public class Process{

    // Constantes
    privita int MAX_PROCECESSES = 200;

    // Atributos de un proceso
    private int id; // id del proceso
    private int arrival; // Tiempo de llegada del proceso
    private int burst; // Ráfaga de CPU original (tiempo cpu)
    private int priority; // Para algoritmo con prioridades
    private int start_time; // Primer instante que se ejecuta
    private int finish_time; // Momento en que termina
    private int waiting_time; // Tiempo total de espera
    private int turnaround; // finish_time - arrival
    private int response_time; // primer instante en que el proceso recibe CPU − arrival



}
