import java.net.ServerSocket;
import java.net.Socket;

import java.io.*;


public class Server {
    private static ServerSocket serverSocket;
    private static Socket clientSocket;

    private static PrintWriter in;
    private static BufferedOutputStream out;

    private static int port = 3500;


    public static void main(String args[]){
        try{
            try{
                serverSocket = new ServerSocket(port);
                System.out.println("Server started on port " + port);

                clientSocket = serverSocket.accept();
                System.out.println("Client connected to server");


            } finally {
                clientSocket.close();
                System.out.println("Client socket closed");
                serverSocket.close();
            }
        }catch (IOException e){
            System.out.println(e);
        }
    }
}
