import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {

        String imagen_nombre;
        imagen_nombre = "images/caso2-parrotspeq.bmp";
        // imagen_nombre = "images/caso2-parrotspeq_sal.bmp";

        int TP = 512;

        Opcion1 opcion1 = new Opcion1(TP, imagen_nombre);

        opcion1.calcularNumeroDePaginas();
        opcion1.simularReferencias();
        opcion1.escribirReferencias();

        String ruta_archivo = "referencias.txt";
        int cant_marcos = 4;

        Opcion2 opcion2 = new Opcion2(cant_marcos, ruta_archivo);
        opcion2.startAndWait();

        // Generar varios escenarios para graficar
        boolean graficar = true;
        if (graficar) {
            clearData();
            int[] TPs = { 512, 1024, 2048 };
            int[] marcos = { 4, 6 };

            for (int tp : TPs) {
                opcion1 = new Opcion1(tp, imagen_nombre);
                opcion1.calcularNumeroDePaginas();
                opcion1.simularReferencias();
                opcion1.escribirReferencias();
                for (int marco : marcos) {
                    opcion2 = new Opcion2(marco, ruta_archivo);
                    opcion2.startAndWait();
                }
            }
        }

        // Ejecute Graficador.py para ver las grafica despues de correr
        // el codigo con graficar = True

    }

    public static void clearData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("output/data.txt", false))) {
            bw.write("tamaño_pagina " + "marcos_asignados " + "numero_hits " + "numero_fallas");
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }
}
