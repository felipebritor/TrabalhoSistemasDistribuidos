/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thread;

import servidor.Ponto;

public class Executor implements Runnable{

    private int id;
    private int pontosNoCirculo;
    private int pontosNoTotal;
    private Ponto[] pontos;
    
    public Executor(int id, Ponto[] pontos){
        this.id = id;
        this.pontos = pontos;
        
        pontosNoCirculo = 0;
        pontosNoTotal = 0;
    }
    
    @Override
    public void run() {
         //synchronized (mm) {
             for (Ponto ponto : pontos) {
                 double pow = (ponto.x * ponto.x) + (ponto.y * ponto.y);
                 pontosNoTotal++;
                 if(pow <= 1)
                     pontosNoCirculo++;
             }
            System.out.println("Thread id "+id+": "+pontosNoCirculo+"/"+pontosNoTotal);
         //}
    }
    
    
    public int getPontosNoCirculo(){
        return pontosNoCirculo;
    }
}
