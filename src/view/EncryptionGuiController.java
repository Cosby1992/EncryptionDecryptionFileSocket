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

public class EncryptionGuiController implements Client.ClientListener {

    @FXML JFXTextArea ta_unencrypted;
    @FXML JFXTextArea ta_encrypted;
    @FXML Label lb_chosen_file;
    @FXML Label lb_server_connected;

    private File file = null;
    private Client client = null;
    private byte[] encryptedFilebytes = null;


    public void pickFile(ActionEvent actionEvent) {

        Window window = ((Node) actionEvent.getTarget()).getScene().getWindow();

        FileChooser chooser = new FileChooser();

        chooser.setTitle("Vælg fil");
        file = chooser.showOpenDialog(window);

        if(file != null){
            lb_chosen_file.setText(file.getName());

            ta_unencrypted.setText("Indlæser fil...");

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
            lb_chosen_file.setText("Der blev ikke valgt nogen fil");
            ta_unencrypted.setText("Ingen fil valgt");
            ta_encrypted.setText("Ingen fil valgt");
        }

    }


    public void connect_to_server(ActionEvent actionEvent) {

        if(client == null){
            client = new Client(this);
            new Thread(client).start();
        }


    }


    public void sendFile(ActionEvent actionEvent) {

        if(client != null && file != null){
            new Thread(new Runnable() {
                @Override
                public void run() {

                    client.sendFile(file.getName(), encryptedFilebytes);

                }
            }).start();
        }

    }


    public void encryptFile(ActionEvent actionEvent) {

        if(file != null){

            Encrypt encrypt = new Encrypt();

            ta_encrypted.setText("Indlæser Krypteret Fil...");

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
            ta_encrypted.setText("Der er ikke valgt nogen fil.");
        }


    }

    @Override
    public void updateClientStartet(Date date) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lb_server_connected.setText(date.toString());
            }
        });

    }
}
