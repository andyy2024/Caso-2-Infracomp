package pc.threads;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import pc.NRU;
import pc.OS;

public class Lector extends Thread {

    private OS os;
    private ArrayList<String[]> referencias;
    private NRU nru;
    public static boolean chambeando; // informa al inspector que el
                                     // lector termino de leer referencias
    public int hits;
    public int fallas;
    public static boolean print;

    public Lector(OS os, ArrayList<String[]> referencias, NRU nru){

        this.os = os;
        this.referencias = referencias;
        this.nru = nru;
        hits = 0;
        fallas = 0;
        chambeando = true;
    }

    @Override
    public void run() {
        
        Integer pagina, desplazamiento, disponible, espacio;
        String accion;
        boolean cargada, modificada, enSwap, enDisco;

        int i = 0;
        for (String[] referencia : referencias) {
            
            pagina = Integer.parseInt(referencia[1]);
            desplazamiento = Integer.parseInt(referencia[2]);
            accion = referencia[3];
            
            // 1. Primero checkamos que la pagina esta cargada en RAM
            cargada = os.tablaDePaginas.getMarco(pagina) != null;

            if (!cargada){

                fallas++;

                // Si la pagina no esta en la RAM, tiene que estar en
                // el disco duro si no fue modificada o en la memoria
                // SWAP si sí lo fue.
                modificada = os.tablaDePaginas.getBitM(pagina);
                enSwap = os.swap.paginaEnSWAP(pagina);
                enDisco = os.discoDuro.paginaEnDiscoDuro(pagina);

                // Esto es puramente simbolico para simular como funciona
                // de verdad, si todo sale bien jamas van a saltar las excepciones
                if (modificada) {
                    if (!enSwap) {
                        throw new IllegalStateException("La página modificada no se encuentra en la memoria SWAP.");
                    }
                } else {
                    if (!enDisco) {
                        throw new IllegalStateException("La página no modificada no se encuentra en el disco duro.");
                    }
                }

                // Como la pagina no esta cargada debemos preguntarle
                // a la memoria RAM si tiene espacio para un marco mas.
                // Si sí lo tiene, retorna el marco, si no, retorna null
                disponible = os.ram.buscarMarcoDisponible();

                if (disponible == null){
                    // Si la RAM esta llena, entonces llamamos a NRU
                    // para que nos libere un espacio
                    espacio = nru.ejecutar(pagina); 
                    os.tablaDePaginas.setMarco(pagina, espacio); // Asignamos el marco a la pagina
                } else {
                    // Si la RAM no esta llena, entonces simplemente
                    // asignamos el marco libre a la pagina
                    os.tablaDePaginas.setMarco(pagina, disponible);
                }
 
            } else {
                hits++;
                // Si la pagina ya esta cargada, tecnicamente no 
                // hacemos nada, el proceso puede ejecutarse con normalidad
            }

            // 2. Actualizamos la accion sobre esa pagina
            if (accion.equals("R")){
                // Una lectura solo activa el bit R
                os.tablaDePaginas.setBitR(pagina, true);
            } else if (accion.equals("W")){
                // Una escritura activa ambos bits
                os.tablaDePaginas.setBitR(pagina, true);
                os.tablaDePaginas.setBitM(pagina, true);
            }
            
            
            i++;
            // espera 1 ms cada que lee 10000 referencias
            if (i%10000 == 0){
                dormir(1,0);
            }
        }

        chambeando = false;

        // Reporte:
        if (print){
            System.out.println("\n|-----------------------------------------------------------|");
            System.out.println("                Tamaño de pagina: " + os.TP);
            System.out.println("                Marcos: " + os.cant_marcos);
            System.out.println("                Referencias: " + referencias.size());
            System.out.printf("                Hits: %d (%%%.2f)%n", hits, (double) hits*100/referencias.size());
            System.out.printf("                Fallas: %d (%%%.2f)", fallas, (double) fallas*100/referencias.size());
            System.out.println("\n|-----------------------------------------------------------|");
        }

        guardarInfo();
    }

    public void dormir(long milis, int nanos){
        try {
            Thread.sleep(milis, nanos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void guardarInfo(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("output/data.txt", true))) {
            bw.write(os.TP + " " + os.cant_marcos + " " + hits + " " + fallas);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

}
