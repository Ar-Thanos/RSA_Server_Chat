import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


public class SecureChatClient extends JFrame implements Runnable, ActionListener {

    public static final int PORT = 8765;

    ObjectInputStream myReader;
    ObjectOutputStream myWriter;
    JTextArea outputArea;
    JLabel prompt;
    JTextField inputField;
    String myName, serverName;
    Socket connection;
    private static SymCipher cipher;
    private static BigInteger key;
    public SecureChatClient ()
    {
        try {

        serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");
        InetAddress addr =
                InetAddress.getByName(serverName);
        connection = new Socket(addr, PORT);   // Connect to server with new
                                               // Socket
         myWriter = new ObjectOutputStream(connection.getOutputStream());
         myWriter.flush();
         myReader = new ObjectInputStream(connection.getInputStream());
        
                     BigInteger E = (BigInteger) myReader.readObject();
                     BigInteger N = (BigInteger) myReader.readObject();
                     System.out.println("E is: "+E);
            
                     System.out.println("\nN is: "+N);
                     String cipherType = (String) myReader.readObject();
                     System.out.println("\nThe cipher type is "+cipherType);
                     if(cipherType.equals("Sub")){
                        cipher = new Substitute();
                     }
                     else if(cipherType.equals("Add")){
                        cipher = new Add128();
                     }
                     else{
                         throw new IOException();
                     }
                     key = new BigInteger(1, cipher.getKey()); //Sign convention
                     key = key.modPow(E, N); //Good old RSA Encryption Math :)
                     System.out.println("\nThe symmetric key is: "+key);
                     System.out.println("\n\n");
                     myWriter.writeObject(key); 
                     myWriter.flush();
                     myName = JOptionPane.showInputDialog(this, "Enter your user name: ");
                     //myWriter.writeObject(obj);
                     myWriter.writeObject(cipher.encode(myName));   // Send name to Server.  Server will need
                     myWriter.flush();               
                     // this to announce sign-on and sign-off
                                    // of clients

        this.setTitle(myName);      // Set title to identify chatter

        Box b = Box.createHorizontalBox();  // Set up graphical environment for
        outputArea = new JTextArea(8, 30);  // user
        outputArea.setEditable(false);
        b.add(new JScrollPane(outputArea));

        outputArea.append("Welcome to the Chat Group, " + myName + "\n");

        inputField = new JTextField("");  // This is where user will type input
        inputField.addActionListener(this);

        prompt = new JLabel("Type your messages below:");
        Container c = getContentPane();

        c.add(b, BorderLayout.NORTH);
        c.add(prompt, BorderLayout.CENTER);
        c.add(inputField, BorderLayout.SOUTH);

        Thread outputThread = new Thread(this);  // Thread is to receive strings
        outputThread.start();                    // from Server

	      addWindowListener(
              new WindowAdapter()
              {
                  public void windowClosing(WindowEvent e)
                  { 
                      String exit = "CLIENT CLOSING";
                    try{
                    System.out.println("Original Message: "+exit);
                    myWriter.writeObject(cipher.encode(exit));
                    }
                    catch(IOException f){
                        System.out.println();
                    }
                    System.exit(0);
                   }
              }
          );

        setSize(500, 200);
        setVisible(true);

        }
        catch (Exception e)
        {
            System.out.println("Problem starting client!"+e);
        }
    }

    public void run()
    {
        while (true)
        {
             try {
                byte[] msg = (byte[])myReader.readObject();
                System.out.print("\nMessage bytes are: ");
                for(int i=0; i<msg.length;i++){
                    System.out.print(msg[i]+" ");
                }
                String s = cipher.decode(msg);
                System.out.println("\n\nDecoded Message: "+s);
                outputArea.append(s+"\n");
             }
             catch (Exception e)
             {

                System.out.println(e +  ", closing client!");
                break;
             }
        }
        System.exit(0);
    }

    public void actionPerformed(ActionEvent e)
    {
        String currMsg = e.getActionCommand();      // Get input value
        inputField.setText("");
        String message = myName + ": " + currMsg;
        System.out.println("\n\nThe original message is: "+message);
        System.out.print("Bytes of message: ");
        byte[] messageBytes = message.getBytes();
        for(int i=0; i<messageBytes.length;i++){
            System.out.print(messageBytes[i]+" ");
        }
        System.out.println("\n");
        try{
        myWriter.writeObject(cipher.encode(message));  myWriter.flush();
        }
        catch(IOException f){
            System.out.println();
        } // Add name and send it
    }                                               // to Server

    public static void main(String [] args)
    {
         SecureChatClient JR = new SecureChatClient();
         JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}