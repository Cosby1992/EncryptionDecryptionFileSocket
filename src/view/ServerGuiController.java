package view;

import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Encrypt;
import model.FileUtils;
import server.Server;

import java.io.File;
import java.io.IOException;

public class ServerGuiController implements Server.ServerListener {

    @FXML Label lb_server_start;
    @FXML Label lb_filename;
    @FXML JFXTextArea ta_encrypted_file;
    @FXML JFXTextArea ta_decrypted_file;

    private byte[] filebytes = null;
    private String filename = "";

    public void startServer(ActionEvent actionEvent) {

        new Thread(new Server(this)).start();

    }

    public void decryptFile(ActionEvent actionEvent) {

        if(filebytes != null){
            updateTaDecrypted();
        } else {
            ta_decrypted_file.setText("Ingen Fil at Dekryptere");
        }

    }

    @Override
    public void updateTaEncrypted(String filename, byte[] filebytes) {
        this.filename = filename;
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

    private void updateTaDecrypted(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(filebytes != null){
                    Encrypt encrypt = new Encrypt();
                    try {
                        byte[] bytes = encrypt.cosDecrypt(new File(filename));
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
