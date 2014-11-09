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


/**
 *
 * @author felipe, francis & vitor
 */
public class Cliente {

    private final int SERVIDOR = 0;
    private final int CLIENTE = 1;
    private final String titulo = "AV 2";
    
    private final String SEND = "/send";
    private final String CLEAR = "/clear";
    private final String EXIT = "/exit";
    
    PrintWriter out;
    BufferedReader in;
    Socket s;
    
    // LAYOUT
    private JTextField messageBox;
    private JButton sendBtn;
    private JTextArea displayBox;
    private JFrame frame = new JFrame(titulo);
    
    public static void main(String[] args) {
        new Cliente();
    }
    
    public Cliente(){
        
        String connRes = startConnection();
        setDisplay();
        displayMsg(connRes,SERVIDOR);
        
    }
    
    private void messagesManager(){
        String msg = messageBox.getText();
        
        if (msg.length() < 1) {/* Não faz nada! */} 
        else if (msg.equals(CLEAR)) {
            displayBox.setText("");
            messageBox.setText("");
        }
        else if(msg.equals(EXIT)){
            displayMsg(msg,CLIENTE);
            out.println(messageBox.getText());
            closeConnection();        
            frame.setVisible(false);
            frame.dispose();
            System.exit(0);
        }
        else if(msg.startsWith(SEND)){
            displayMsg(msg,CLIENTE);
            String[] params = msg.split(" ");
            int n = Integer.parseInt(params[1]);
            sendNumbers(createNumbers(n*2));
        }
        else
            displayMsg(msg,CLIENTE);
        
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
    
    private void sendNumbers (String numbers){
        out.println("/send "+numbers);
    }
    
    
    private void displayMsg(String msg, int flag){
        String voz;
        SimpleDateFormat sdt = new SimpleDateFormat("hh:mm:ss");
        String hora = sdt.format(Calendar.getInstance().getTime());
        if(flag == SERVIDOR)
            voz = "<servidor - "+ hora +">: ";
        else
            voz = "<cliente  - "+ hora +">: ";
        displayBox.append(voz+msg+"\n");
    }
    
    private class SendBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            messagesManager();
            messageBox.setText("");
            messageBox.requestFocusInWindow();
        }
    }
    
    private String startConnection(){
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
    
    private void closeConnection(){
        
        try {
            out.close();
            in.close();
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    private void setDisplay(){
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



