package thread;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Avell B153
 */
public class GeradorNumeros implements Runnable{

    private int pares;
    private String pacote;
    private final Buffer buffer;
    public GeradorNumeros(Buffer b, int n){
        pares = n;
        buffer = b;
    }
    
    @Override
    public void run() {
        
        while(true){
            synchronized(buffer){
                if(buffer.getMemoria()[0] == null && buffer.getVezes() > 0)
                    buffer.getMemoria()[0] = createNumbers();
                if(buffer.getVezes() == 0) 
                    break;
            
            }
        }
    }
    
    private String createNumbers(){
        StringBuilder numbers = new StringBuilder();
        double x,y;
        double fator = 1e3;
        for (int i = 0; i < pares; i++) {
            x = Math.round(Math.random()*fator)/fator;
            y = Math.round(Math.random()*fator)/fator;
            numbers.append(x);
            numbers.append("-");
            numbers.append(y);
            numbers.append(i == pares-1 ? "" : ":");
            
	}
        return numbers.toString();
    }
    
}
