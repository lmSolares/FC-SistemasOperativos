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
    private int remaining; // Tiempo que falta de ejecución
    private int start_time; // Primer instante que se ejecuta
    private int finish_time; // Momento en que termina
    private int waiting_time; // Tiempo total de espera
    private int turnaround; // finish_time - arrival
    private int response_time; // primer instante en que el proceso recibe CPU − arrival
    private int user; // usuario al que pertenece el proceso (utilizaremos a priority tambien como usuario)

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
        this.user = priority;
    }

    /**
    *   Método que ejecuta el proceso (decrementa el tiempo que falta de ejecución)
    */
    public void executeProcess(){
        this.remaining--;
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

    public int getUser(){
        return this.user;
    }

    // Setters
    public void setStartTime(int start_time){
        this.start_time = start_time;
    }

    public void setFinishTime(int finish_time){
        this.finish_time = finish_time;
    }

    public void setWaitingTime(int waiting_time){
        this.waiting_time = waiting_time;
    }

    public void setTurnaround(int turnaround){
        this.turnaround = turnaround;
    }

    public void setResponseTime(int response_time){
        this.response_time = response_time;
    }

    // Utilities
    @Override
    public String toString(){
        return "P" + this.id + ": arrival=" + this.arrival + " burst=" + this.burst + " priority=" + this.priority + " remaining=" + this.remaining + " start_time=" + this.start_time + " finish_time=" + this.finish_time + " waiting_time=" + this.waiting_time + " turnaround=" + this.turnaround + " response_time=" + this.response_time + "\n";
    }

}
