/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private Monitor mm;
    public GeradorNumeros(Monitor m, int n){
        pares = n;
        mm = m;
    }
    
    public void setPares(int n){
        pares = n;
    }
    
    public String getPacote(){
        return this.pacote;
    }
    
    @Override
    public void run() {
        //while(true){
            pacote = createNumbers(pares);
            if(mm.getStatus() == 1){ 
                try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(GeradorNumeros.class.getName()).log(Level.SEVERE, null, ex);
            }
        //    }
        }
    }
    
    private String createNumbers(int n){
        StringBuilder numbers = new StringBuilder();
        double x,y;
        double fator = 1e3;
        for (int i = 0; i < n; i++) {
            x = Math.round(Math.random()*fator)/fator;
            y = Math.round(Math.random()*fator)/fator;
            numbers.append(x);
            numbers.append("-");
            numbers.append(y);
            numbers.append(i == n-1 ? "" : ":");
            
	}
        return numbers.toString();
    }
    
}
