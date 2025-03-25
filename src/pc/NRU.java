package pc;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NRU {

    private OS os;

    public NRU(OS os) {
        this.os = os;
    }

    public Integer ejecutar(int p){

        //--------------------------------------------------------------
        // El objetivo es abrirle espacio a una pagina en memoria RAM
        
        // 1. buscamos la mejor pagina para quitar de la RAM
        int sacrificio = seleccionarCandidato(); // id de la pagina candidata
        int marco = os.tablaDePaginas.getMarco(sacrificio); // el marco que vamos a desocupar

        os.tablaDePaginas.desocupar(sacrificio); // ahora la pagina no tiene marco asignado
        
        // 2. Si la pagina fue modificada, no podemos solo quitarla
        // o perderiamos los cambios, por eso debemos copiarla a la 
        // memoria swap en caso de que el proceso que la estaba usando vuelva
        // (esto es puramente simbolico para ilustrar como funciona realmente)
        if (os.tablaDePaginas.getBitM(sacrificio)){
            os.swap.copiarPagina(sacrificio);
        }
        
        // 3. Retorna el marco libre
        return marco;
    }

    public int seleccionarCandidato() {
        // Crear listas para cada clase
        List<Integer> clase0 = new ArrayList<>();
        List<Integer> clase1 = new ArrayList<>();
        List<Integer> clase2 = new ArrayList<>();
        List<Integer> clase3 = new ArrayList<>();

        boolean R, M;
        Integer marco;

        // Clasificamos las filas en las cuatro clases
        for (int pagina = 0; pagina < os.tablaDePaginas.size(); pagina++) {

            R = os.tablaDePaginas.getBitR(pagina);
            M = os.tablaDePaginas.getBitM(pagina);
            marco = os.tablaDePaginas.getMarco(pagina);

            if (marco == null){continue;} // buscamos paginas en la RAM

            if (!R && !M) {
                clase0.add(pagina);
            } else if (!R && M) {
                clase1.add(pagina);
            } else if (R && !M) {
                clase2.add(pagina);
            } else {
                clase3.add(pagina);
            }
        }

        // Seleccionamos la primera fila de la clase de menor prioridad
        if (!clase0.isEmpty()) {
            return clase0.get(0);
        } else if (!clase1.isEmpty()) {
            return clase1.get(0);
        } else if (!clase2.isEmpty()) {
            return clase2.get(0);
        } else if (!clase3.isEmpty()) {
            return clase3.get(0);
        }
        return -1;
    }

}
