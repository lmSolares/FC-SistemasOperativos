package src.java.entities;

import java.util.Queue;
import java.util.LinkedList;

/**
*   Clase que representa a un usuario
*/
public class User{

    private int userID;
    private Queue<Process> readyProcessQueue;

    // Constructor del usuario
    public User(int userID){
        this.userID = userID;
        this.readyProcessQueue = new LinkedList<>();
    }

    /**
    *   Método para agregar un proceso a la cola de procesos del usuario.
    *
    *   @param process proceso a agregar
    */
    public void addProcessToUser(Process process){
        this.readyProcessQueue.add(process);
    }

    /**
    *   Método para obtener el proceso que está en la cabeza de la cola
    *   de procesos del usuario.
    *
    *   @return cabeza de la cola de procesos del usuario, o null si la cola está vacia.
    */
    public Process pollUserProcess(){
        return this.readyProcessQueue.poll();
    }

    /**
    *   Método para saber si el usuario tiene algún proceso listo para
    *   ejecutar.
    *
    *   @return false si no hay procesos
    */
    public boolean isUserQueueEmpty(){
        return this.readyProcessQueue.isEmpty();
    }

    // Getters
    public int getUserID(){
        return this.userID;
    }

    // Utilities
    @Override
    public String toString(){
        return "U" + this.userID + ". Lista de procesos del usuario: " + this.readyProcessQueue.toString();
    }

}
