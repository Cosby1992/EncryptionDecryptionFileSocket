package view;

import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.Encrypt;
import model.FileUtils;
import server.Server;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class ServerGuiController implements Server.ServerListener {

    @FXML Label lb_client_connected;
    @FXML Label lb_server_start;
    @FXML Label lb_filename;
    @FXML JFXTextArea ta_encrypted_file;
    @FXML JFXTextArea ta_decrypted_file;

    private byte[] filebytes = null;
    private String filename = "";

    private boolean decrypted = false;

    public void startServer(ActionEvent actionEvent) {

        new Thread(new Server(this)).start();

    }

    public void decryptFile(ActionEvent actionEvent) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(filebytes != null){
                    updateTaDecrypted();
                    decrypted = true;
                } else {
                    ta_decrypted_file.setText("Ingen Fil at Dekryptere");
                }
            }
        }).start();


    }

    public void saveFile(ActionEvent actionEvent) {

        if(decrypted){
            Window window = ((Node) actionEvent.getTarget()).getScene().getWindow();

            FileChooser chooser = new FileChooser();

            chooser.setTitle("Vælg placering");
            chooser.setInitialFileName(filename);

            File file = chooser.showSaveDialog(window);

            if(file != null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FileUtils.writeToFile(file.getPath(), filebytes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                lb_filename.setText("No location added");
            }
        } else {
            lb_filename.setText("Dekrypter filen før du kan gemme.");
        }


    }



    @Override
    public void updateTaEncrypted(String filename, byte[] filebytes) {
        this.filename = filename;
        this.filebytes = filebytes;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                lb_filename.setText(filename);
                if(filebytes != null){
                    ta_encrypted_file.setText(FileUtils.getStringFromByteArray(filebytes));
                }
            }
        });
    }

    @Override
    public void updateServerStarted(String update) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lb_server_start.setText(update);
            }
        });
    }

    @Override
    public void updateClientConnected(String update, String color) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lb_client_connected.setTextFill(Paint.valueOf(color));
                lb_client_connected.setText(update);
            }
        });
    }


    private void updateTaDecrypted(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(filebytes != null){
                    Encrypt encrypt = new Encrypt();
                    try {
                        byte[] bytes = encrypt.cosDecrypt(filebytes);
                        ta_decrypted_file.setText(FileUtils.getStringFromByteArray(bytes));

                    } catch (IOException e) {
                        ta_decrypted_file.setText("Kunne ikke dekryptere filen");
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}
