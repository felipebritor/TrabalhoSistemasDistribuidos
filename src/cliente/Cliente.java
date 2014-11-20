package cliente;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import thread.Buffer;
import thread.GeradorNumeros;
import thread.Secretaria;

/**
 *
 * @author felipe, francis & vitor
 * 
 * Forma de uso: /send vezes quantidadeDePontos
 * Ex.: /send 4 100000
 * 
 */
public class Cliente {

    private final int SERVIDOR = 0;
    private final int CLIENTE = 1;
    private final String titulo = "AV 2";

    private final String SEND = "/send";
    private final String CLEAR = "/clear";
    private final String EXIT = "/exit";

    private PrintWriter out;
    private BufferedReader in;
    Socket s;

    // LAYOUT
    private JTextField messageBox;
    private JButton sendBtn;
    private JTextArea displayBox;
    private JFrame frame = new JFrame(titulo);

    public static void main(String[] args) {
        new Cliente();
    }

    public Cliente() {
        String connRes = startConnection();
        setDisplay();
        displayMsg(connRes, SERVIDOR);

    }

    private void messagesManager() {
        String msg = messageBox.getText();

        if (msg.length() < 1) {/* Não faz nada! */

        } else if (msg.equals(CLEAR)) {
            displayBox.setText("");
            messageBox.setText("");
        } else if (msg.equals(EXIT)) {
            displayMsg(msg, CLIENTE);
            out.println(messageBox.getText());
            closeConnection();
            frame.setVisible(false);
            frame.dispose();
            System.exit(0);
        } else if (msg.startsWith(SEND)) {
            displayMsg(msg, CLIENTE);
            trataParametros(msg);
        } else {
            displayMsg(msg, CLIENTE);
        }

    }

    private void trataParametros(String cmd) {
        String[] paramString = cmd.split(" ");
        int vezes, pares;
        if (paramString.length <= 1) {
            displayMsg("ERRO - insira os parametros necessários", CLIENTE);
        } else {
            vezes = Integer.parseInt(paramString[1]);
            pares = Integer.parseInt(paramString[2]);
            gerenciadorDeEnvio(vezes, pares);
        }
    }

    private void gerenciadorDeEnvio(int vezes, int pares) {
        
        Buffer buffer = new Buffer();
        buffer.setVezes(vezes);
        GeradorNumeros gn = new GeradorNumeros(buffer, pares);
        Secretaria sec = new Secretaria(out,in,buffer);
        
        Thread thGerador = new Thread(gn);
        Thread thSec = new Thread(sec);
        
        thGerador.start();
        thSec.start();
        
        try {
            thGerador.join();
            thSec.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        tratarRecebimento(sec.getRecebido());
        
    }

    /*private void sendNumbers(String pacote) {
        out.println("/send " + pacote);
    }*/
    
    private void tratarRecebimento(String pacote){
        String[] dados = pacote.split("-");
        for (String dado : dados) {
            String[] params = dado.split(":");
            String resultado;
            if(params.length == 2)
                resultado = "Pontos no circulo: "+params[0]+" - Pi: "+params[1];
            else
                resultado = params[0];
            displayMsg(resultado, SERVIDOR);
        }
        
    }

    private void displayMsg(String msg, int flag) {
        String voz;
        SimpleDateFormat sdt = new SimpleDateFormat("hh:mm:ss");
        String hora = sdt.format(Calendar.getInstance().getTime());
        if (flag == SERVIDOR) {
            voz = "<servidor - " + hora + ">: ";
        } else {
            voz = "<cliente  - " + hora + ">: ";
        }
        displayBox.append(voz + msg + "\n");
    }

    private class SendBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            messagesManager();
            messageBox.setText("");
            messageBox.requestFocusInWindow();
        }
    }

    private String startConnection() {
        String connRes = "não conectado... reinicie o programa.";

        try {
            s = new Socket("127.0.0.1", 49500);
            out = new PrintWriter(s.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            connRes = in.readLine();

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connRes;
    }

    private void closeConnection() {

        try {
            out.close();
            in.close();
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setDisplay() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel caixaInput = new JPanel();
        caixaInput.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();

        sendBtn = new JButton("Enviar");
        sendBtn.addActionListener(new SendBtnListener());

        displayBox = new JTextArea();
        displayBox.setEditable(false);
        displayBox.setFont(new Font("Droid Sans Mono", Font.PLAIN, 14));
        displayBox.setLineWrap(true);

        panel.add(new JScrollPane(displayBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 612.0D;
        left.weighty = 2.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        caixaInput.add(messageBox, left);
        caixaInput.add(sendBtn, right);

        panel.add(BorderLayout.SOUTH, caixaInput);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(670, 400);
        frame.setVisible(true);
        frame.getRootPane().setDefaultButton(sendBtn);
    }

}
