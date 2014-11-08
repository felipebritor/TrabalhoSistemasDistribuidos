package servidor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author felipe, francis & vitor
 */
public class Servidor {

    private final String EXIT = "/exit";
    private final String SEND = "/send";
    private final String ERRO = "Um erro ocorreu...";
    
    PrintWriter out;
    BufferedReader in;
    
    ServerSocket ss;
    Socket cliente;
    
    FileInputStream inFile;
    FileOutputStream outFile;
    
    int totalThreads;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Servidor();
    }
    
    public Servidor(){
        String entrada;
        boolean keep = true;
        try {
            totalThreads = Runtime.getRuntime().availableProcessors();
            System.out.println("Processadores: "+totalThreads);
            ss = new ServerSocket(49500);
            cliente = ss.accept();
            out = new PrintWriter(cliente.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            out.println("Conectado.");
            System.out.println("Conectado...");
            
            while(keep){
                entrada = in.readLine();
                keep = messagesManager(entrada);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private boolean messagesManager(String cmd){
        boolean keep = true;
        
        //testa conexão
        System.out.println(cmd);
        if(cmd.startsWith(SEND)){
            delegarTrabalho(cmd);
        }
        else if(cmd.equals(EXIT)){
            closeConnection();
            keep = false;
        }
        else{
            out.println("comando inválido!");
        }
        
        return keep;
    }
    
    private void delegarTrabalho(String numbersString){
        quebrarString(numbersString);
        
        
    }
    
    private float[] quebrarString (String numbersString){
        float[] numbers;
        String[] numTexto = numbersString.split(" ")[1].split(":");
        int length = numTexto.length;
        System.out.println("LENGTH: "+length);
        numbers = new float[length];
        for(int i = 0; i < length; i++) {
           numbers[i] =  Float.parseFloat(numTexto[i]);
           System.out.println(i+": "+numbers[i]);
        }
        return numbers;
    } 
    
    
    
    private void closeConnection(){
        try {
            out.close();
            in.close();
            cliente.close();
            ss.close();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    
}
