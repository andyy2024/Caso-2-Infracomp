package pc;
import java.util.HashSet;

public class SWAP {

    // Guarda los indices de paginas copiadas a la memoria SWAP
    HashSet<Integer> memoria;

    public SWAP(){
        memoria = new HashSet<>();
    }

    public void copiarPagina(int pagina) {
        memoria.add(pagina);
    }

    public void liberarPagina(int pagina){
        memoria.remove(pagina);
    }

    public boolean paginaEnSWAP(int pagina){
        return memoria.contains(pagina);
    }
}
