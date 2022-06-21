import java.net.*;
import java.io.*;

import java.util.LinkedList;

class ClientsHandler extends Thread{
    private final Socket socket; // Socket talking with client

    private final BufferedReader in; // Reading thread
    private final BufferedWriter out; // Writing thread


    public ClientsHandler(Socket socket) throws IOException {
        this.socket = socket;

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        Server.story.printStory(out); // Tell to new client last 10 messages

        start();
    }


    @Override
    public void run(){
        String clientMsg;

        try{
            clientMsg = in.readLine();

            try{
                out.write(clientMsg + "\n");
                out.flush();
            }catch (IOException ignored){}

            try{
                while(true){
                    clientMsg = in.readLine();

                    if(clientMsg.equals("server Stop")){
                        this.downService();
                        break;
                    }
                    System.out.println("Echo: " + clientMsg);

                    Server.story.addStoryEl(clientMsg);
                    for(ClientsHandler vr : Server.serverList){
                        vr.send(clientMsg);
                    }
                }

            }catch (NullPointerException ignored){}

        }catch (IOException e){
            this.downService();
        }


    }

    private void send(String msg){
        try{
            out.write(msg);
            out.flush();
        }catch (IOException ignored){}
    }

    private void downService(){
        try{
            if(!socket.isClosed()){
                socket.close();
                in.close();
                out.close();

                for (ClientsHandler vr : Server.serverList){
                    if(vr.equals(this)){ vr.interrupt();}
                    Server.serverList.remove(this);
                }
            }
        }catch (IOException ignored){}
    }
}


class Story{
    private final LinkedList<String> story = new LinkedList<>();

    public void addStoryEl(String el){
        if(story.size() >= 10){
            story.removeFirst();
            story.add(el);
        }else{
            story.add(el);
        }
    }

    public void printStory(BufferedWriter writer){
        if(story.size() > 0){
            try{
                writer.write("History messages" + "\n");
                for (String vr : story){
                    writer.write(vr + "\n");
                }
                writer.write("/...." +  "\n");
                writer.flush();
            }catch (IOException ignored){}
        }
    }
}


public class Server {

    public static int PORT = 3500;
    public static LinkedList<ClientsHandler> serverList = new LinkedList<>();
    public static Story story;


    public static void main(String[] args){
        try{
            ServerSocket server = new ServerSocket(PORT);
            story = new Story();
            System.out.println("Server started");
            try{
                while(true){
                    Socket socket = server.accept();
                    try{
                        serverList.add(new ClientsHandler(socket));
                    }catch (IOException e){
                        socket.close();
                    }
                }
            }finally {
                server.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
