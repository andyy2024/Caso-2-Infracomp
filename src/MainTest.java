import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import pc.threads.Lector;

public class MainTest {
    public static void main(String[] args) throws Exception {

        TestDynamicParameters(); 
        TestResultadosEnunciado();
        TestDifferentImages();

    }

    /**
     * Este método prueba diferentes imagenes para comparar resutados
     * 
     * - Si en la ejecucion no se lanza ninguna excepcion -> se pasa la prueba.
     * - El objetivo de esta prueba es comprobar que el codigo funciona con mas
     * de una imagen
     */
    private static void TestDifferentImages() {

        clearData();
        String ruta = "images/";
        String[] imagenes = { "caso2-parrotspeq","cute_cat", "kirby", "pikachu" };
        String ruta_archivo = "referencias.txt";

        int[] tamanosPagina = { 256, 512, 1024, 2048, 4096 };
        int[] marcosAsignados = { 1, 2, 3, 4, 5, 6, 7, 8 };

        for (String imagen : imagenes) {
            String imagen_nombre = ruta + imagen + ".bmp";
            System.out.printf("Probando con la imagen: %s%n", imagen_nombre);

            for (int tp : tamanosPagina) {
                Opcion1 opcion1 = new Opcion1(tp, imagen_nombre);
                opcion1.calcularNumeroDePaginas();
                opcion1.simularReferencias();
                opcion1.escribirReferencias();

                for (int marcos : marcosAsignados) {
                    Opcion2 opcion2 = new Opcion2(marcos, ruta_archivo);
                    opcion2.startAndWait();

                    int hitsObtenidos = opcion2.os.lector.hits;
                    int fallasObtenidas = opcion2.os.lector.fallas;

                    System.out.printf("TP: %d, Marcos: %d, Hits: %d, Fallas: %d%n", tp, marcos, hitsObtenidos,
                            fallasObtenidas);
                }
            }
        }

        System.out.println("Pruebas con diferentes imágenes completadas.");
    }

    /**
     * Este método prueba que los resultados arrojados sean similares a los
     * del enunciado.
     * 
     * Segun el enunciado, nuestros resultados deberian estar en un rango +/- 5%
     * de los presentados en las tablas
     */
    private static void TestResultadosEnunciado() {

        clearData();

        int[][] hitsObjetivo = new int[][] { { 743318, 756556 },
                { 756518, 756701 },
                { 756716, 756728 } };
        int[][] fallasObjetivo = new int[][] { { 13438, 200 },
                { 238, 55 },
                { 40, 28 } };
        int[] tamanosPagina = { 512, 1024, 2048 }; // Agrega más tamaños de página si es necesario
        int[] marcosAsignados = { 4, 6 }; // Agrega más valores de marcos si es necesario

        String imagen_nombre = "images/caso2-parrotspeq.bmp";
        String ruta_archivo = "referencias.txt";

        double rangoPermitido = 0.05; // 5%
        Lector.print = false;
        for (int i = 0; i < tamanosPagina.length; i++) {
            int tp = tamanosPagina[i];
            for (int j = 0; j < marcosAsignados.length; j++) {
                int marcos = marcosAsignados[j];

                int hits = hitsObjetivo[i][j];
                int fallas = fallasObjetivo[i][j];

                Opcion1 opcion1 = new Opcion1(tp, imagen_nombre);
                opcion1.calcularNumeroDePaginas();
                opcion1.simularReferencias();
                opcion1.escribirReferencias();

                Opcion2 opcion2 = new Opcion2(marcos, ruta_archivo);
                opcion2.startAndWait();

                int hitsObtenidos = opcion2.os.lector.hits;
                int fallasObtenidas = opcion2.os.lector.fallas;

                double porcentajeHits = Math.abs(hits - hitsObtenidos) / (double) hits;
                double porcentajeFallas = Math.abs(fallas - fallasObtenidas) / (double) fallas;

                boolean hitsValidos = porcentajeHits <= rangoPermitido;
                boolean fallasValidas = porcentajeFallas <= rangoPermitido;

                System.out.printf("\nTP: %d, Marcos: %d%n", tp, marcos);
                System.out.printf("Porcentaje de diferencia en hits: %.2f%%%n", porcentajeHits * 100);
                System.out.printf("Porcentaje de diferencia en fallas: %.2f%%%n", porcentajeFallas * 100);

                if (hitsValidos) {
                    System.out.println("Prueba pasada: Los hits están dentro del rango permitido.");
                } else {
                    System.err.println("Prueba fallida: Los hits están fuera del rango permitido.");
                    System.err.println("Hits esperados: " + hits + ", Hits obtenidos: " + hitsObtenidos);
                }

                if (fallasValidas) {
                    System.out.println("Prueba pasada: Las fallas están dentro del rango permitido.");
                } else {
                    System.err.println("Prueba fallida: Las fallas están fuera del rango permitido.");
                    System.err.println("Fallas esperadas: " + fallas + ", Fallas obtenidas: " + fallasObtenidas);
                }
            }
        }
    }

    /**
     * Este método prueba parámetros dinámicos para probar la opcion 1 y 2
     * 
     * - Si en la ejecucion no se lanza ninguna excepcion -> se pasa la prueba.
     * - El objetivo de esta prueba es buscar parametros que causan bugs
     */
    public static void TestDynamicParameters() {

        clearData();

        String imagen_nombre;
        // imagen_nombre = "images/caso2-parrotspeq.bmp";
        // imagen_nombre = "images/cute_cat.bmp";
        // imagen_nombre = "images/kirby.bmp";
        imagen_nombre = "images/pikachu.bmp";

        String ruta_archivo = "referencias.txt";

        // PARAMETROS
        int[] TPs = { 256, 512, 1024, 2048, 4096 };
        int[] marcos = { 1, 2, 3, 4, 5, 6, 7, 8 };

        for (int tp : TPs) {
            Opcion1 opcion1 = new Opcion1(tp, imagen_nombre);
            opcion1.calcularNumeroDePaginas();
            opcion1.simularReferencias();
            opcion1.escribirReferencias();
            for (int marco : marcos) {
                Opcion2 opcion2 = new Opcion2(marco, ruta_archivo);
                opcion2.startAndWait();
            }
        }

        System.out.println("TEST PASSED: ningun parametro causo problemas");

        // Ejecute Graficador(n).py para ver las grafica despues de correr
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
