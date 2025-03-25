package pc;

public class RAM {

    boolean[] marcos; // 1 signfica ocupado, 0 un marco libre

    public RAM(int size){
        marcos = new boolean[size];
    }

    public Integer buscarMarcoDisponible(){

        for (int i = 0; i < marcos.length; i++) {
            if (!marcos[i]){
                marcos[i] = true; // ocupamos el marco
                return i;
            }
        }
        return null;
    }
}
