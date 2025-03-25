package pc;

public class TablaDePaginas {
    
    private Fila[] filas; // cada indice del array es una pagina

    public TablaDePaginas(int paginas, int marcos, boolean preCargar) {
        
        filas = new Fila[paginas];
        for (int i = 0; i < paginas; i++) {
            filas[i] = new Fila();
        }

        if (preCargar){

            for (int i = 0; i < marcos; i++) {
                filas[i].marco = i;
            }
        }

    }

    //--------------------------------------------------
    // metodos sincronizados
    public synchronized boolean getBitR(int n){
        return filas[n].R;
    }

    public synchronized void setBitR(int n, boolean R){
        filas[n].R = R;
    }

    public synchronized void resetearBitR(){
        
        for (Fila fila : filas) {
            fila.R = false;
        }
    }
    //----------------------------------------------------

    public synchronized boolean getBitM(int n){
        return filas[n].M;
    }

    public synchronized void setBitM(int n, boolean M){
        filas[n].M = M;
    }

    public synchronized Integer getMarco(int n){
        return filas[n].marco;
    }

    public int size(){
        return filas.length;
    }

    public void desocupar(int pagina){
        filas[pagina].marco = null;
    }

    public void setMarco(int pagina, int marco){
        filas[pagina].marco = marco;
    }

    public class Fila {

        Integer marco = null; // Direccion en RAM (a un marco)
        boolean R = false; // Bit de referencia
        boolean M = false; // Bit de modificacion

    }
}