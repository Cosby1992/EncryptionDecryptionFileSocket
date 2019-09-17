package server;

import model.CosObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * This class handles a server socket (only one client at a time is possible)
 */
public class Server implements Runnable {

    //Listener interface is used to update Server ui (implemented in ServerGuiController.java)
    public interface ServerListener{
        //updates 3 different things in gui
        void updateTaEncrypted(String filename, byte[] filebytes);
        void updateServerStarted(String update);
        void updateClientConnected(String update, String color);
    }

    //constant port OBS: if changed remember to change in client as well
    private final int PORT = 666;

    private ServerListener listener;

    //Contructor that takes a listener as argument (class that updates server ui)
    //Used to update UI
    public Server(ServerListener listener) {
        this.listener = listener;
    }

    //To run on a thread
    @Override
    public void run() {

        try {
            //Allocation of server socket
            ServerSocket server = new ServerSocket(PORT);

            //updates ui with server start time (now)
            listener.updateServerStarted(new Date().toString());

            //loop handles client connection (only one at a time)
            while (true){
                //updates ui with current client status
                listener.updateClientConnected("venter på klient", "#000000");

                //accepts a client
                Socket socket = server.accept();

                //updates ui with current client status
                listener.updateClientConnected("tilsluttet", "GREEN");

                //generating input and output streams (using Objectstreams to send objects)
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

                //Declaring object used for filetransfer
                CosObject object;

                //loop handles incomming files from client
                while (socket.isConnected()){

                    //reads object from client
                    object = (CosObject) input.readObject();

                    if(object != null){
                        //if file is not null update the ui (filebytes is also used to save file)
                        //TODO: make sure ui is updated seperately and file is transferred on thread
                        listener.updateTaEncrypted(object.getFilename(), object.getFileBytes());
                    } else {
                        //if file is null
                        listener.updateTaEncrypted("Ingen fil modtaget.", null);
                    }

                }

                //updates ui with current client status
                listener.updateClientConnected("ikke tilsluttet", "RED");
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
    }

}
