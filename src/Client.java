import java.io.*;

import java.net.Socket;

public class Client {
    private static Socket socket;

    public static void main(String[] args) {
        try{
            socket = new Socket("127.0.0.1", 3500);
        }catch (IOException e){
            System.out.println(e);
        }
    }
}
