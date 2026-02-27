package src.java.utilities;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import src.java.entities.Process;

/**
*   Clase que permite leer archivos.
*/
public class ReadFile{

    /**
    *   Método que lee un archivo de procesos y establece un límite de
    *   procesos a agregar, cada proceso debe estar en una linea, y debe
    *   tener 4 ints (id, arrival, burst y priority) separados por espacios.
    *
    *   @param maxprocessses número máximo de procesos a agregar
    *   @return Lista de procesos del archivo dado
    */
    public static List<Process> readProcessFile(String path, int maxprocessses){
        List<Process> processes = new ArrayList<Process>();
        Scanner scan = null;
        int i = 0;

        try{
            scan = new Scanner(new File(path));
        } catch(Exception e){
            System.out.println(e.getMessage());
        }

        if(scan != null){
            while(scan.hasNextInt() && i < maxprocessses){
                int id = scan.nextInt();
                int arrival = scan.nextInt();
                int burst = scan.nextInt();
                int priority = scan.nextInt();
                i++;

                Process process = new Process(id, arrival, burst, priority);
                processes.add(process);
            }
            scan.close();
        }
        return processes;
    }

}
