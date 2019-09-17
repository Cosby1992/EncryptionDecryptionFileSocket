package client;

import model.CosObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

public class Client implements Runnable {

    public interface ClientListener {
        void updateClientStartet(Date date);
    }

    private final int PORT = 666;
    private final String HOST = "localhost";

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ClientListener listener;

    public Client(ClientListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {

        try {
            socket = new Socket(HOST, PORT);

            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            listener.updateClientStartet(new Date());

            System.out.println("client connected successfully");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    public void sendFile(String filename, byte[] filebytes){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    oos.writeObject(new CosObject(filename, filebytes));
                    oos.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        thread.start();

        System.out.println("object sent");

    }



}
