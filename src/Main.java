import java.util.Scanner;

import pc.threads.Lector;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;
        Lector.print = true;

        while (continuar) {
            System.out.println("\nSeleccione una opción:");
            System.out.println("1. Calcular número de páginas y simular referencias");
            System.out.println("2. Procesar archivo de referencias");
            System.out.println("3. S1alir");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
            case 1:
                System.out.println("Ingrese el nombre de la imagen (en la carpeta images/):");
                String imagen_nombre = "images/" + scanner.nextLine();

                java.io.File imagen = new java.io.File(imagen_nombre);
                if (!imagen.exists() || !imagen.isFile()) {
                    System.out.println("La imagen no existe. Por favor, verifique el nombre e intente de nuevo.");
                    break;
                }

                System.out.println("Ingrese el tamaño de página (TP):");
                int TP = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                Opcion1 opcion1 = new Opcion1(TP, imagen_nombre);

                opcion1.calcularNumeroDePaginas();
                opcion1.simularReferencias();
                opcion1.escribirReferencias();
                break;

            case 2:
                System.out.println("Ingrese la ruta del archivo de referencias:");
                String ruta_archivo = scanner.nextLine();

                java.io.File archivo = new java.io.File(ruta_archivo);
                if (!archivo.exists() || !archivo.isFile()) {
                    System.out.println("El archivo no existe. Por favor, verifique la ruta e intente de nuevo.");
                    break;
                }

                System.out.println("Ingrese la cantidad de marcos:");
                int cant_marcos = scanner.nextInt();

                Opcion2 opcion2 = new Opcion2(cant_marcos, ruta_archivo);
                opcion2.startAndWait();
                break;

            case 3:
                continuar = false;
                System.out.println("Saliendo del programa...");
                break;

            default:
                System.out.println("Opción no válida. Intente de nuevo.");
            }
        }

        scanner.close();
    }
}