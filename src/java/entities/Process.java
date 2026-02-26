package src.entities;

/**
*   Clase que representa un proceso
*/
public class Process{

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

    // Constructor del proceso
    public Process(){

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


    public void setID(int id){
        this.id = id;
    }

    public void setArrial(int arrival){
        this.arrival = arrival;
    }

    public void setBurst(int burst){
        this.burst = burst;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    public void setStarTime(int start_time){
        this.start_time = start_time;
    }

    public void setFinishTime(int finish_time){
        this.finish_time = finish_time;
    }

    public void setWaitingTime(int waiting_time){
        this.waiting_time = waiting_time;
    }

    public void Turnaround(int turnaround){
        this.turnaround = turnaround;
    }
    
    public void setResponseTime(int response_time){
        this.response_time = response_time;
    }



}
