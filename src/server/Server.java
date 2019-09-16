package server;

import model.CosObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    public interface ServerListener{
        void updateTaEncrypted(String filename, byte[] filebytes);
    }

    private final int PORT = 666;

    private byte[] fileBytes = null;
    private ServerListener listener;

    public Server(ServerListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {

        try {
            ServerSocket server = new ServerSocket(PORT);

            Socket socket = server.accept();

            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            CosObject object = null;

            while (object == null){

                object = (CosObject) input.readObject();

                if(object != null){
                    listener.updateTaEncrypted(object.getFilename(), object.getFileBytes());
                } else {
                    listener.updateTaEncrypted("Ingen fil modtaget.", null);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }



}
