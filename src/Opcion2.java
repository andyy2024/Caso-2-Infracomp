import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import pc.OS;

public class Opcion2 {

    private ArrayList<String[]> referencias = new ArrayList<>();
    private OS os;
    private Integer NP;
    private Integer TP;

    public Opcion2(int cant_marcos, String nombre_archivo){

        cargarReferencias(nombre_archivo);

        os = new OS(cant_marcos, NP, TP, referencias);
    }

    public void startAndWait(){
        os.start();
    }

    public void cargarReferencias(String nombre_archivo){

        try (BufferedReader br = new BufferedReader(new FileReader(nombre_archivo))) {
            String linea;
            int i = 0;

            // Leer y mostrar las líneas a partir de la quinta
            while ((linea = br.readLine()) != null) {
                i++;

                if (linea.trim().isEmpty()) {continue;} // Ignorar líneas vacías
                
                if (i == 1){TP = Integer.parseInt(linea.split("=")[1]); continue;}
                if (i == 5){NP = Integer.parseInt(linea.split("=")[1]); continue;}

                if (i < 6){continue;} //ignora las primeras 5 lineas

                referencias.add(linea.split(","));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
