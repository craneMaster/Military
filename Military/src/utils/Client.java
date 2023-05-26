package utils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
 
public class Client {
    // initialize socket and input output streams
    Socket socket = null;
    DataInputStream in = null;
    DataOutputStream out = null;
	String line = "START";
 
    
    
    // constructor to put ip address and port
    public Client(String address, int port)
    {
        // establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");
 
            // takes input from terminal
            in = new DataInputStream(socket.getInputStream());
 
            // sends output to the socket
            out = new DataOutputStream(
                socket.getOutputStream());
        }
        catch (UnknownHostException u) {
            System.out.println(u);
            return;
        }
        catch (IOException i) {
            System.out.println(i);
            return;
        }
        
        // string to read message from input
 
//         keep reading until "Over" is input
//        while (!line.equals("Over")) 
//        {
//            try {
//              line = in.readLine();
//            	
//                out.writeUTF(line);
//            }
//            catch (IOException i) {
//                System.out.println(i);
//            }
//        }
 
//        // close the connection
//        try {
//            in.close();
//            out.close();
//            socket.close();
//        }
//        catch (IOException i) {
//            System.out.println(i);
//        }
    }
 
    public static void main(String args[])
    {
        Client client = new Client("172.16.200.173", 5002);
    }
}