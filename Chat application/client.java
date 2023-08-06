import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
//import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class client extends JFrame {
    //constructer
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    //declare components
    private JLabel heading=new JLabel("........CLIENT AREA......");
    private JTextArea messageArea=new JTextArea();
    private JTextField massageInput= new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);   
    

   
    
    public client(){
        try {
        
                System.out.println("sending request to server.");
                socket=new Socket("127.0.0.1",7777);
                System.out.println("connection done.");
                br=new BufferedReader(new InputStreamReader (socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream());

             createGUI(new JScrollPane());
             handleEvents();


             startReading();
             //startWriting();

            
        } catch (Exception e) {
            

        }
    }
    private void handleEvents(){
        massageInput.addKeyListener(new KeyListener() {

           // private JLabel messageInput;


            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            
            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("Key released"+e.getKeyCode());
                if (e.getKeyCode()==10){
                    // JTextField messageInput = new JTextField();
                    //System.out.println("Press Enter button");
                    String contentToSend= massageInput.getText(); 
                    messageArea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    massageInput.setText("");
                    massageInput.requestFocus();
                }

            }
            
        });
    }




    /**
     * @param scrollPane 
     * 
     */
    private void createGUI(JScrollPane scrollPane){
        this.setTitle("Client Messager[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //for components
        heading.setFont(font);
        messageArea.setFont(font);
        massageInput.setFont(font);
        //heading.setIcon(new ImageIcon("clogo.jpg"));
        //heading.setHorizontalTextPosition(SwingConstants.CENTER);
        //heading.setVerticalTextPosition(SwingConstants.BOTTOM);
    
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        massageInput.setHorizontalAlignment(SwingConstants.CENTER);
        //for frame layout
        this.setLayout(new BorderLayout());
        //adding components

        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(massageInput,BorderLayout.SOUTH);

        this.setVisible(true);




    }
    
    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("reader started..");
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("Exit")) {
                        System.out.println("server closed the chat...");
                        JOptionPane.showMessageDialog(this, "Server closed the chat");
                        massageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    //System.out.println("server: " + msg); // Print the received message
                    messageArea.append("server: " + msg + "\n");
                }
            } catch (Exception e) {
                //e.printStackTrace();
               System.out.println("connection closed...");
            }
        };
        new Thread(r1).start();
    }
    // Writing....
    public void startWriting()
    {
        Runnable r2 = ()-> {
            System.out.println("writer started..");

            try{
                while(true && !socket.isClosed())
                {
              
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if (content.equals("Exit")){
                        socket.close();
                        break;
                } 
            }
            
            System.out.println("connection closed...");
        } 
            catch(Exception e){
                 e.printStackTrace();
            }
         };
        
         new Thread(r2).start();

    }

    public static void main(String[] args) {
        
        System.out.println("this is client...");
        new client();
    }
}