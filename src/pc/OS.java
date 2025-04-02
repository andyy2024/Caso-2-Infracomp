package pc;
import java.util.ArrayList;

import pc.threads.Inspector;
import pc.threads.Lector;

public class OS {

    // VARIABLES
    public int cant_marcos;
    public int NP;
    public int TP;

    // COMPONENTES DEL SISTEMA
    public TablaDePaginas tablaDePaginas;
    public RAM ram;
    public SWAP swap; // esto es solo simbolico, no afecta el funcionamiento
    public DiscoDuro discoDuro; // esto es solo simbolico, no afecta el funcionamiento

    // Los 2 threads que coordinan las peticiones:
    public Inspector Inspector; // se encarga de actualizar el estado del bit R cada 1ms
    public Lector lector; // se encarga de leer 10000 referencias cada 1ms

    public OS(int cant_marcos, int NP, int TP, ArrayList<String[]> referencias){

        // inicia el sistema ^-^

        this.cant_marcos = cant_marcos;
        this.NP = NP;
        this.TP = TP;

        ram = new RAM(cant_marcos);
        swap = new SWAP();
        discoDuro = new DiscoDuro(NP);
        tablaDePaginas = new TablaDePaginas(NP, cant_marcos);
        NRU nru = new NRU(this);

        lector = new Lector(this, referencias, nru);
        Inspector = new Inspector(this);
    }

    public void start(){
        lector.start();
        Inspector.start();

        try {
            lector.join();
            Inspector.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
