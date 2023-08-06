import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

class Server extends JFrame {

    ServerSocket Server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    //constructor

    private JLabel heading=new JLabel("........SERVER AREA......");
    private JTextArea messageArea=new JTextArea();
    private JTextField massageInput= new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);

    public Server()
    {
        try{
        
           Server = new ServerSocket(7777);

           System.out.println("server is ready to accept connection");
           System.out.println("waiting......");
           socket=Server.accept();
  
           br=new BufferedReader(new InputStreamReader (socket.getInputStream()));
           out=new PrintWriter(socket.getOutputStream());

           createGUI(new JScrollPane());
            handleEvents();

           startReading();
           startWriting();


        } catch(Exception e){

            e.printStackTrace();
        }
    }

    private void handleEvents() {
         massageInput.addKeyListener(new KeyListener() {

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
     * @param ScrollPane 
     */
    private void createGUI(JScrollPane jScrollPane) {
        this.setTitle("Server Messager[END]");
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
        JScrollPane jScrollPane1Pane = new JScrollPane(messageArea);
        this.add(jScrollPane1Pane,BorderLayout.CENTER);
        this.add(massageInput,BorderLayout.SOUTH);

        this.setVisible(true);




    }

    public void startReading()
    {
        Runnable r1 = ()-> {
            System.out.println("reader started..");

            try{
                while(true)
                {
                    String msg = br.readLine();
                    if(msg.equals("Exit")){

                        System.out.println("client close chat...");
                        JOptionPane.showMessageDialog(this, "Server closed the chat");
                        massageInput.setEnabled(false);

                        socket.close();
                        break;
                    }
                   // System.out.println("client : "+msg);
                    messageArea.append("server: " + msg + "\n");
                }
            }
            catch(Exception e){
               //e.printStackTrace();
               System.out.println("connection closed...");
            }
         };
        
        new Thread(r1).start();

    }
    public void startWriting()
    {
        Runnable r2 = ()-> {
            System.out.println("reader started..");

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
               
                
            }   
                catch(Exception e){
                //e.printStackTrace();
                System.out.println("connection closed...");
             }
         };
        
         new Thread(r2).start();

    }
    public static void main(String[] args) {
        
        System.out.println("this is server......going to start server");
        new Server();
    }
}