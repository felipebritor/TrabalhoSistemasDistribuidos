/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thread;

import servidor.Ponto;

public class Executor implements Runnable{

    private Monitor mm;
    private int id;
    private int pontosNoCirculo;
    private Ponto[] pontos;
    
    public Executor(int id, Ponto[] pontos, Monitor monitor){
        this.id = id;
        this.pontos = pontos;
        this.mm = monitor;
        pontosNoCirculo = 0;
    }
    
    @Override
    public void run() {
         synchronized (mm) {
             for (Ponto ponto : pontos) {
                 float sqrt = (ponto.x * ponto.x) + (ponto.y * ponto.y);
                 if(sqrt <= 1)
                     pontosNoCirculo++;
                 if(mm.getStatus() == 1) //CHECAR
                     break;
             }
            System.out.println("Thread id"+id+": "+pontosNoCirculo);
         }
    }
    
}
