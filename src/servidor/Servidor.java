package servidor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import thread.Executor;


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
    
    int totalPontos;
    int totalProcessors;
    
    
    Executor[] exs;
    Thread[] threads;
    
    public static void main(String[] args) {
        new Servidor();
    }
    
    public Servidor(){
        String entrada;
        boolean keep = true;
        try {
            totalProcessors = Runtime.getRuntime().availableProcessors();
            System.out.println("Processadores: "+totalProcessors);
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
        //System.out.println(cmd);
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
        //System.out.println(numbersString);
        Ponto[] pontos = quebrarString(numbersString);
        totalPontos = pontos.length;
        
        int pontosPorThreadsLength = (int)totalPontos/totalProcessors;
        Ponto[] pontosPorThreads;
        // dividir os pontos pelas threads
        
        exs = new Executor[totalProcessors];
        threads = new Thread[totalProcessors];
        
        for (int i = 0; i < totalProcessors; i++) {
            if(i < totalProcessors-1){
                pontosPorThreads = new Ponto[pontosPorThreadsLength];
                for (int k = 0; k < pontosPorThreadsLength; k++){
                    pontosPorThreads[k] = pontos[k+i*pontosPorThreadsLength];
                }
            }else{
                int pontosUltimaThread = totalPontos-(pontosPorThreadsLength*(totalProcessors-1));
                pontosPorThreads = new Ponto[pontosUltimaThread];
                for (int l = 0; l < pontosUltimaThread; l++){
                    pontosPorThreads[l] = pontos[l+i*pontosPorThreadsLength];
                }
            }
            
            Executor ex = new Executor(i,pontosPorThreads);
            exs[i] = ex;
            Thread th = new Thread(exs[i]);
            threads[i] = th;
            threads[i].start();
        }
        tratarProcessamento();
    }
    
    private void tratarProcessamento(){
        try {
            for (Thread th : threads) 
                th.join();
        } catch (InterruptedException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        int tp = 0;
        for (Executor ex : exs) {
            tp += ex.getPontosNoCirculo();
        }
        double pi = (double) 4*tp/totalPontos;
        out.println(tp+":"+pi);
        System.out.println("TotalPontos: "+tp+" - Pi: "+pi);
    }
    
    private Ponto[] quebrarString (String numbersString){
        Ponto[] pontos;
        String[] numTexto = numbersString.split(" ")[1].split(":");
        int length = numTexto.length;
        
        pontos = new Ponto[length];
        String[] pontoString;
        for(int i = 0; i < length; i++) {
           pontoString = numTexto[i].split("-");
           Ponto p = new Ponto();
           p.x = Double.parseDouble(pontoString[0]);
           p.y = Double.parseDouble(pontoString[1]);
           pontos[i] = p;
        }
        return pontos;
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
