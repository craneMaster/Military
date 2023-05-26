package utils;

import java.net.*;
import java.io.*;
 
public class Server
{
	public static final int cap = 3;
    //initialize socket and input stream
    Socket[]        socket = new Socket[cap+1];   
    ServerSocket    server = null;
    DataInputStream[] in   = new DataInputStream[cap+1];
    DataOutputStream[]out  = new DataOutputStream[cap+1];
    String line = "START";
    
    
    // constructor with port
    public Server(int port)
    {
        // starts server and waits for a connection
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started");
 
            System.out.println("Waiting for a client ...");
 
            for(int i=0;i<cap;i++){
            	socket[i] = server.accept();
            	System.out.println("A new client joined");	
            	
            in[i] = new DataInputStream(
                new BufferedInputStream(socket[i].getInputStream()));
 
            out[i] = new DataOutputStream(socket[i].getOutputStream());
            
            
            	
            }
            // reads message from client until "Over" is sent
//            while (!line.equals("Over"))
//            {
//                try
//                {
//                    line = in.readUTF();
// 
//                }
//                catch(IOException i)
//                {
//                    System.out.println(i);
//                }
//            }
//            System.out.println("Closing connection");
 
            // close connection
//            socket.close();
//            in.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
 
    public static void main(String args[])
    {
        Server server = new Server(5002);
    }
}
