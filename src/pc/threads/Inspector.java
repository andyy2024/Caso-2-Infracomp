package pc.threads;

import pc.OS;

public class Inspector extends Thread {

    private OS os;

    public Inspector(OS os){
        this.os = os;
    }

    @Override
    public void run() {
        
        boolean lectorChamba = true;

        while (lectorChamba){

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            os.tablaDePaginas.resetearBitR(); // Todos los bit R de todas 
                                              // las paginas los cambia a 0

            lectorChamba = Lector.chambeando;
        }
    }
}
