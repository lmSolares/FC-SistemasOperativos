package src.java.entities;

/**
*   Clase que representa un proceso
*/
public class Process{

    // Atributos de un proceso
    private int id; // id del proceso
    private int arrival; // Tiempo de llegada del proceso
    private int burst; // Ráfaga de CPU original (tiempo cpu)
    private int priority; // Para algoritmo con prioridades
    private int remaining;
    private int start_time; // Primer instante que se ejecuta
    private int finish_time; // Momento en que termina
    private int waiting_time; // Tiempo total de espera
    private int turnaround; // finish_time - arrival
    private int response_time; // primer instante en que el proceso recibe CPU − arrival

    // Constructor del proceso
    public Process(int id, int arrival, int burst, int priority){
        this.id = id;
        this.arrival = arrival;
        this.burst = burst;
        this.priority = priority;
        this.remaining = burst;
        this.start_time = -1;
        this.finish_time = 0;
        this.waiting_time = 0;
        this.turnaround = 0;
        this.response_time = -1;
    }

    // Getters
    public int getID(){
        return this.id;
    }

    public int getArrival(){
        return this.arrival;
    }

    public int getBurst(){
        return this.burst;
    }

    public int getPriority(){
        return this.priority;
    }

    public int getStartTime(){
        return this.start_time;
    }

    public int getFinishTime(){
        return this.finish_time;
    }

    public int getWaitingTime(){
        return this.waiting_time;
    }

    public int getTurnaround(){
        return this.turnaround;
    }

    public int getResponseTime(){
        return this.response_time;
    }

    public int getRemaining(){
        return this.remaining;
    }

    // Utilities
    @Override
    public String toString(){
        return "P" + this.id + ": arrival=" + this.arrival + " burst=" + this.burst + " priority=" + this.priority;
    }

}
