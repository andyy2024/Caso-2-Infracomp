package pc;
import java.util.HashSet;

public class DiscoDuro {

    // Guarda los índices de páginas copiadas al disco duro (fuera de la SWAP)
    HashSet<Integer> memoria;

    public DiscoDuro(int paginas_totales){
        memoria = new HashSet<>();

        // Todas las paginas comienzan en el disco duro
        for (int i = 0; i < paginas_totales; i++) {
            memoria.add(i);
        }
    }

    public void copiarPagina(int pagina) {
        memoria.add(pagina);
    }

    public void liberarPagina(int pagina){
        memoria.remove(pagina);
    }

    public boolean paginaEnDiscoDuro(int pagina){
        return memoria.contains(pagina);
    }
}