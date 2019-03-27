/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketcom;

/**
 *
 * @author I14746
 */
import java.net.*; 
import java.io.*; 
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
  
public class SocketClient 
{ 
    // initialize socket and input output streams 
    private Socket socket            = null; 
    private DataInputStream  input   = null; 
    private DataOutputStream out     = null; 
    private Output output; 
    
    // constructor to put ip address and port 
    public SocketClient(String address, int port, Output output) 
    { 
        this.output = output;
        output.PrintStatusMessage("Attempting to connect...\n");
       
        // establish a connection 
        try
        { 
            socket = new Socket(address, port); 
            output.PrintStatusMessage("Connection established\n");
            // takes input from terminal 
            input  = new DataInputStream(System.in); 
            InputStreamReader isr = new InputStreamReader(input);
            ChangeListener<byte[]> l; 
            l = new ChangeListener<byte[]>() {
                @Override
                public void changed(ObservableValue<? extends byte[]> observable, byte[] oldValue, byte[] newValue) {
                    output.PrintDataRecieved(SharedData.bytesToHex(newValue));
                    output.PrintStatusMessage("Recieve successful\n");
                }
            };
            isr.addChangeListener(l);
  
            // sends output to the socket 
            out   = new DataOutputStream(socket.getOutputStream()); 
        } 
        catch(UnknownHostException u) 
        { 
            System.out.println(u); 
            output.PrintStatusMessage("Failed to establish connection\n");
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
            output.PrintStatusMessage("Failed to establish connection\n");
        } 
  
//        // string to read message from input 
//        String line = ""; 
//  
//        // keep reading until "Over" is input 
//        while (!line.equals("Over")) 
//        { 
//            try
//            { 
//                line = input.readLine(); 
//                out.writeUTF(line); 
//            } 
//            catch(IOException i) 
//            { 
//                System.out.println(i); 
//            } 
//        } 
//  
//        // close the connection 
//        try
//        { 
//            input.close(); 
//            out.close(); 
//            socket.close(); 
//        } 
//        catch(IOException i) 
//        { 
//            System.out.println(i); 
//        } 
    }
    
    public void disconnect(){
        try
        { 
            input.close(); 
            out.close(); 
            socket.close(); 
            output.PrintStatusMessage("Socket link disconnected\n");
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
            output.PrintStatusMessage("Failed to disconnected\n");
        } 
    }
    public boolean write(byte[] bytes){
        boolean retval = true;
        output.PrintStatusMessage(String.format("Writing %d bytes\n",bytes.length));
        if(out!=null){
            try {
                out.write(bytes);                
            } catch (IOException ex) {
                retval = false;
                Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else retval = false;
        if(retval){
            this.output.PrintDataSent(SharedData.bytesToHex(bytes));
            output.PrintStatusMessage("Write successful\n");
        }else{
            output.PrintStatusMessage("Write failed\n");
        }
        return retval;
    }
    
    
} 
