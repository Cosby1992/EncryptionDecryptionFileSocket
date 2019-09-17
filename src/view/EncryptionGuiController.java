package view;

import client.Client;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.Encrypt;
import model.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Client Gui controller (handles a little more than gui at times)
 */
public class EncryptionGuiController implements Client.ClientListener {

    //Initializing from fxml file (encryption_gui.fxml)
    @FXML JFXTextArea ta_unencrypted;
    @FXML JFXTextArea ta_encrypted;
    @FXML Label lb_chosen_file;
    @FXML Label lb_server_connected;

    //Instance variables
    private File file = null;
    private Client client = null;
    private byte[] encryptedFilebytes = null;


    //Button OnClick Method
    public void pickFile(ActionEvent actionEvent) {

        //Opening a file chooser window using the source of the actionevent
        Window window = ((Node) actionEvent.getTarget()).getScene().getWindow();

        FileChooser chooser = new FileChooser();

        chooser.setTitle("Vælg fil");
        file = chooser.showOpenDialog(window);

        //Handles selected file
        if(file != null){

            //updates ui
            lb_chosen_file.setText(file.getName());

            ta_unencrypted.setText("Indlæser fil...");

            //starting thread that reads the file content and shows it in textarea
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String s = FileUtils.getBytesAsString(file);
                        ta_unencrypted.setText(s);
                    } catch (IOException e) {
                        ta_unencrypted.setText("Failed to load file bytes");
                        e.printStackTrace();
                    }
                }
            }).start();

        } else {
            //if no file was selected
            lb_chosen_file.setText("Der blev ikke valgt nogen fil");
            ta_unencrypted.setText("Ingen fil valgt");
            ta_encrypted.setText("Ingen fil valgt");
        }

    }


    //Button OnClick Method
    public void connect_to_server(ActionEvent actionEvent) {

        //Connecting client to server (if client is not started already)
        if(client == null){
            client = new Client(this);
            new Thread(client).start();
        }


    }


    //Button OnClick method
    public void sendFile(ActionEvent actionEvent) {

        //Send the selcted file to the server (only if connected to server and file is selected)
        if(client != null && file != null){
            new Thread(new Runnable() {
                @Override
                public void run() {

                    client.sendFile(file.getName(), encryptedFilebytes);

                }
            }).start();
        }

    }

    //Button OnClick method
    public void encryptFile(ActionEvent actionEvent) {

        //Encrypts a file if a file was selected
        if(file != null){

            Encrypt encrypt = new Encrypt();

            ta_encrypted.setText("Indlæser Krypteret Fil...");

            //Starting thread that does the work and updates the UI
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] bytes = encrypt.cosEncrypt(file);
                        encryptedFilebytes = bytes;

                        String s = FileUtils.getStringFromByteArray(bytes);

                        ta_encrypted.setText(s);

                    } catch (IOException e) {
                        ta_encrypted.setText("Kunne ikke indlæse den krypterede fil.");
                        e.printStackTrace();
                    }
                }
            }).start();

        } else {
            //If no file was selected
            ta_encrypted.setText("Der er ikke valgt nogen fil.");
        }


    }

    //Updates the UI (implemented from listener interface)
    @Override
    public void updateClientStartet(Date date) {

        //Makes sure that threads can update the ui by using Platform.runLater
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //updates UI with date from argument
                lb_server_connected.setText(date.toString());
            }
        });

    }
}
