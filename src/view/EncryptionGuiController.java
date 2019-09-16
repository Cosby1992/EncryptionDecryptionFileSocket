package view;

import com.jfoenix.controls.JFXTextArea;
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

public class EncryptionGuiController {

    @FXML JFXTextArea ta_unencrypted;
    @FXML JFXTextArea ta_encrypted;
    @FXML Label lb_chosen_file;
    @FXML Label lb_server_connected;

    private File file = null;


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

        //TODO: connect to server here

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
}
