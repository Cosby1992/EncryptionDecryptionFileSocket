package client;

import model.CosObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * This class connects to the server and handles file transfer through input and output streams
 * from the socket connection
 */
public class Client implements Runnable {

    //Listener to update client UI
    public interface ClientListener {
        void updateClientStartet(Date date);
    }

    //Constans host and port (if used over a network, remember to change localhost to server IP address)
    private final int PORT = 666;
    private final String HOST = "localhost";

    //Instance variables
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ClientListener listener;

    public Client(ClientListener listener) {
        this.listener = listener;
        //Listener interface implemented where gui is updated (EncryptionGUIController.java)
    }

    //To run on thread
    @Override
    public void run() {

        try {
            //Connect to server
            socket = new Socket(HOST, PORT);

            //Get input output streams for data transfer (using Objects to transfer here)
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            //Update Client user interface
            listener.updateClientStartet(new Date());

            System.out.println("client connected successfully");

        } catch (IOException e) {
            //If connection error occurs
            e.printStackTrace();
        }


    }



    //Method to transfer file to server
    public void sendFile(String filename, byte[] filebytes){

        //Creates new thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //Trying to transfer object
                    oos.writeObject(new CosObject(filename, filebytes));
                    oos.flush();

                } catch (IOException e) {
                    //If error occurs
                    e.printStackTrace();
                }

            }
        });

        //Staring thread
        thread.start();

        System.out.println("object sent");

    }

}
