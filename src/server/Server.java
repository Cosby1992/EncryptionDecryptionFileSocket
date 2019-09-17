package server;

import model.CosObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server implements Runnable {

    public interface ServerListener{
        void updateTaEncrypted(String filename, byte[] filebytes);
        void updateServerStarted(String update);
        void updateClientConnected(String update, String color);
    }

    private final int PORT = 666;
    private ServerListener listener;

    public Server(ServerListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {

        try {
            ServerSocket server = new ServerSocket(PORT);

            listener.updateServerStarted(new Date().toString());

            while (true){
                listener.updateClientConnected("venter på klient", "#000000");

                Socket socket = server.accept();

                listener.updateClientConnected("tilsluttet", "GREEN");

                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());



                CosObject object = null;

                while (socket.isConnected()){

                    object = (CosObject) input.readObject();

                    if(object != null){
                        listener.updateTaEncrypted(object.getFilename(), object.getFileBytes());
                    } else {
                        listener.updateTaEncrypted("Ingen fil modtaget.", null);
                    }

                }

                listener.updateClientConnected("ikke tilsluttet", "RED");
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }



}
