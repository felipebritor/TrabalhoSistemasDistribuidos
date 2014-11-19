package thread;

/**
 *
 * @author felipe
 */
public class Buffer {
    
     private Object memoria[] = new Object[1];
     private int vezes;

    /**
     * @return the memoria
     */
    public Object[] getMemoria() {
        return memoria;
    }

    /**
     * @param memoria the memoria to set
     */
    public void setMemoria(Object[] memoria) {
        this.memoria = memoria;
    }

    /**
     * @return the vezes
     */
    public int getVezes() {
        return vezes;
    }

    /**
     * @param vezes the vezes to set
     */
    public void setVezes(int vezes) {
        this.vezes = vezes;
    }
    
    
}
