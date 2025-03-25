import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import sobel.Imagen;

public class Opcion1 {

    private int TP; // Tamaño de página

    private int NF; // Numero de filas de la matriz de imagen

    private int NC; // Numero de columnas de la matriz de imagen

    private int NR; // Numero de referencias que hace el archivo 

    private int NP; // Numero de páginas virtuales

    private Imagen imagen = null; // La imagen a ser procesada 

    ArrayList<String> referencias = new ArrayList<>();;

    // Arreglo de tamaño [2] para guardar el numero de pagina y 
    // desplazamiento inicial donde empieza cada matriz
    private int[] start_imagenIn, start_imagenOut, start_sobel_X, start_sobel_Y;


    public Opcion1(int TP, String nombre_archivo){
        this.TP = TP;
        imagen = new Imagen(nombre_archivo);
        NF = imagen.alto;
        NC = imagen.ancho;
    }

    public void calcularNumeroDePaginas(){

        int bytes_imagenIn; // Numero de bytes que la matriz imagenIn.imagen necesita ('imagen' en el enunciado)
        int bytes_sobel_X; // Numero de bytes que la matriz SOBEL_X necesita
        int bytes_sobel_Y; // Numero de bytes que la matriz SOBEL_Y necesita
        int bytes_imagenOut; // Numero de bytes que la matriz imagenOut.imagen necesita ('Rta' en el enunciado)
        int bytes_totales; // trivial

        bytes_imagenIn = bytes_imagenOut = NF * NC * 3;
        bytes_sobel_X = bytes_sobel_Y = 36; // 9 elementos de 4 bytes (4*9 = 36)

        bytes_totales = bytes_imagenIn + bytes_sobel_X + bytes_sobel_Y + bytes_imagenOut;
        NP = (int) Math.ceil((double) bytes_totales / TP);

        // Ahora vamos a calcular donde empieza cada matriz
        // El orden debe ser: [imagen, filtroX, filtroY, rta]

        int cuenta = 0; //lleva la cuenta de bytes

        // Numero de pagina y desplazamiento donde empieza cada matriz
        start_imagenIn = new int[]{0,0};
        cuenta += bytes_imagenIn;
        start_sobel_X = new int[]{cuenta / TP, cuenta%TP};
        cuenta += bytes_sobel_X;
        start_sobel_Y = new int[]{cuenta / TP, cuenta%TP}; 
        cuenta += bytes_sobel_Y;
        start_imagenOut = new int[]{cuenta / TP, cuenta%TP};  

    }

    public void simularReferencias(){

        Imagen imagenIn = imagen;
        String sobel_x, sobel_y;
        int fila, columna, bytes_recorridas;
        
        // Vamos a copiar el algoritmo tal cual,
        // omitiendo todo lo que no sea un llamado
        // a una matriz, para ejecutar los llamados
        // en orden ^-^

        // public void applySobel() {
            for (int i = 1; i < imagenIn.alto - 1; i++) {
                for (int j = 1; j < imagenIn.ancho - 1; j++) {
    
                    // Aplicar las máscaras Sobel X y Y
                    for (int ki = -1; ki <= 1; ki++) {
                        for (int kj = -1; kj <= 1; kj++) {

                            // --------------------------------------------------------------
                            // ImagenIn pide referencia
                            // --------------------------------------------------------------
                            // int red = imagenIn.imagen[i + ki][j + kj][0];
                            // int green = imagenIn.imagen[i + ki][j + kj][1];
                            // int blue = imagenIn.imagen[i + ki][j + kj][2];

                            fila = i + ki;
                            columna = j + kj;
                            bytes_recorridas = 3*((fila)*imagenIn.ancho + columna); // Hasta antes de la casilla actual (!)

                            // RED
                            referencias.add(
                                String.format("Imagen[%d][%d].r,%d,%d,R",
                                                fila, 
                                                columna, 
                                                start_imagenIn[0] + (0 + start_imagenIn[1] + bytes_recorridas)/TP,
                                                (start_imagenIn[1] + 0 + bytes_recorridas)%TP ));
                            // GREEN
                            referencias.add(
                                String.format("Imagen[%d][%d].g,%d,%d,R",
                                                fila, 
                                                columna, 
                                                start_imagenIn[0] + (1 + start_imagenIn[1] + bytes_recorridas)/TP,
                                                (start_imagenIn[1] + 1 + bytes_recorridas)%TP ));
                            // BLUE
                            referencias.add(
                                String.format("Imagen[%d][%d].b,%d,%d,R",
                                                fila, 
                                                columna, 
                                                start_imagenIn[0] + (2 + start_imagenIn[1] + bytes_recorridas)/TP,
                                                (start_imagenIn[1] + 2 + bytes_recorridas)%TP ));
                            
                            // --------------------------------------------------------------
                            // SOBEL_X pide referencia:
                            // --------------------------------------------------------------
                            // gradXRed += red * SOBEL_X[ki + 1][kj + 1];
                            // gradXGreen += green * SOBEL_X[ki + 1][kj + 1];
                            // gradXBlue += blue * SOBEL_X[ki + 1][kj + 1];

                            fila = ki + 1;
                            columna = kj + 1;
                            bytes_recorridas = 4*(fila*3 + columna); // Hasta antes de la casilla actual (!)
                                        // Cada fila de la matriz SOBEL_X tiene 3 enteros (4 bytes)
                            
                            sobel_x = String.format("SOBEL_X[%d][%d],%d,%d,R",
                                                fila, 
                                                columna, 
                                                start_sobel_X[0] + (start_sobel_X[1] + bytes_recorridas)/TP,
                                                (start_sobel_X[1] + bytes_recorridas)%TP );

                            //  se consulta la misma posicion 3 veces
                            //-> añadimos la misma referencia 3 veces
                            referencias.add(sobel_x);
                            referencias.add(sobel_x);
                            referencias.add(sobel_x);
                            
                            // --------------------------------------------------------------
                            // SOBEL_Y pide referencia:
                            // --------------------------------------------------------------
                            // gradYRed += red * SOBEL_Y[ki + 1][kj + 1];
                            // gradYGreen += green * SOBEL_Y[ki + 1][kj + 1];
                            // gradYBlue += blue * SOBEL_Y[ki + 1][kj + 1];

                            fila = ki + 1;
                            columna = kj + 1;
                            bytes_recorridas = 4*(fila*3 + columna); // Hasta antes de la casilla actual (!)
                                            // Cada fila de la matriz SOBEL_Y tiene 3 enteros (4 bytes)
                            
                            sobel_y = String.format("SOBEL_Y[%d][%d],%d,%d,R",
                                                fila, 
                                                columna, 
                                                start_sobel_Y[0] + (start_sobel_Y[1] + bytes_recorridas)/TP,
                                                (start_sobel_Y[1] + bytes_recorridas)%TP );

                            //  se consulta la misma posicion 3 veces
                            //-> añadimos la misma referencia 3 veces
                            referencias.add(sobel_y);
                            referencias.add(sobel_y);
                            referencias.add(sobel_y);
                            
                        }
                    }
                    // --------------------------------------------------------------
                    // ImagenOut pide referencia:
                    // --------------------------------------------------------------
                    // imagenOut.imagen[i][j][0] = (byte) red;
                    // imagenOut.imagen[i][j][1] = (byte) green;
                    // imagenOut.imagen[i][j][2] = (byte) blue;

                    fila = i;
                    columna = j;
                    bytes_recorridas = 3*((fila)*imagenIn.ancho + columna); // Hasta antes de la casilla actual (!)

                    // RED
                    referencias.add(
                        String.format("Rta[%d][%d].r,%d,%d,W",
                                        fila, 
                                        columna, 
                                        start_imagenOut[0] + (0 + start_imagenOut[1] + bytes_recorridas)/TP,
                                        (start_imagenOut[1] + 0 + bytes_recorridas)%TP ));
                    // GREEN
                    referencias.add(
                        String.format("Rta[%d][%d].g,%d,%d,W",
                                        fila, 
                                        columna, 
                                        start_imagenOut[0] + (1 + start_imagenOut[1] + bytes_recorridas)/TP,
                                        (start_imagenOut[1] + 1 + bytes_recorridas)%TP ));
                    // BLUE
                    referencias.add(
                        String.format("Rta[%d][%d].b,%d,%d,W",
                                        fila, 
                                        columna, 
                                        start_imagenOut[0] + (2 + start_imagenOut[1] + bytes_recorridas)/TP,
                                        (start_imagenOut[1] + 2 + bytes_recorridas)%TP ));
                }
            }
        //}

        NR = referencias.size();
    }

    public void escribirReferencias(){
        try (PrintWriter writer = new PrintWriter("referencias.txt")) {

            writer.println("TP=" + TP);
            writer.println("NF=" + NF);
            writer.println("NC=" + NC);
            writer.println("NR=" + NR);
            writer.println("NP=" + NP);

            for (String referencia : referencias) {
                writer.println(referencia);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

}
