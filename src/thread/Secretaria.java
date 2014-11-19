package thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author felipe
 */
public class Secretaria implements Runnable{

    private PrintWriter out;
    private BufferedReader in;
    private String pacote;
    private StringBuilder recebido;
    private final Buffer buffer;
    
    public Secretaria(PrintWriter out, BufferedReader in, Buffer buffer){
        this.out = out;
        this.in = in;
        this.buffer = buffer;
        
        recebido = new StringBuilder();
    }
    
    
    @Override
    public void run() {
        int vez;
        while(true){
            synchronized(buffer){
                
                vez = buffer.getVezes();
                if(buffer.getMemoria()[0] != null && vez > 0){
                    try {
                        pacote = (String) buffer.getMemoria()[0];
                        enviar(pacote);
                        buffer.getMemoria()[0] = null;
                        buffer.setVezes(--vez);
                        
                        while(!in.ready()){}
                        recebido.append(in.readLine());
                        recebido.append(vez == 0 ? "" : "-");
                    } catch (IOException ex) {
                        Logger.getLogger(Secretaria.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                if(vez == 0) break;
                
            }
        }
        
    }
    
    private void enviar(String pacote){
        out.println("/send " + pacote);
    }

    /**
     * @return the pacote
     */
    public String getPacote() {
        return pacote;
    }

    /**
     * @param pacote the pacote to set
     */
    public void setPacote(String pacote) {
        this.pacote = pacote;
    }

    /**
     * @return the recebido
     */
    public String getRecebido() {
        return recebido.toString();
    }
}
