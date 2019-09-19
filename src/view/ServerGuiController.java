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

/**
 * Controller that handles gui updates on server part of the software
 * (OBS.. does a little more work sometimes)
 */
public class ServerGuiController implements Server.ServerListener {

    //From fxml file (view/server_gui.fxml)
    @FXML Label lb_client_connected;
    @FXML Label lb_server_start;
    @FXML Label lb_filename;
    @FXML JFXTextArea ta_encrypted_file;
    @FXML JFXTextArea ta_decrypted_file;

    //intanse variables
    private byte[] filebytes = null;
    private String filename = "";

    private boolean decrypted = false;

    //Button onClick method
    public void startServer(ActionEvent actionEvent) {

        //Starts a new server on a thread (Server is runnable)
        new Thread(new Server(this)).start();

    }

    //Button onClick  method
    public void decryptFile(ActionEvent actionEvent) {

        if(!decrypted){
            //starts a new thread that handles decryption of the received file and updates ui
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(filebytes != null){
                        Encrypt encrypt = new Encrypt();
                        filebytes = encrypt.cosDecrypt(filebytes);
                        updateTaDecrypted();
                        decrypted = true;
                    } else {
                        ta_decrypted_file.setText("Ingen Fil at Dekryptere");
                    }
                }
            }).start();
        } else {
            ta_decrypted_file.setText("Filen er allerede dekrypteret");
        }



    }

    //Button onClick method
    public void saveFile(ActionEvent actionEvent) {

        if(decrypted){
            //if the received file was decrypted a save location chooser is shown
            Window window = ((Node) actionEvent.getTarget()).getScene().getWindow();

            FileChooser chooser = new FileChooser();

            chooser.setTitle("Vælg placering");
            chooser.setInitialFileName(filename);

            File file = chooser.showSaveDialog(window);

            if(file != null){
                //if a location was chosen a new thread writes the received file to the location
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
                //if no location was chosen
                lb_filename.setText("No location added");
            }
        } else {
            //If recieved file was not decrypted
            lb_filename.setText("Dekrypter filen før du kan gemme.");
        }

    }



    //Updates the textarea of the encrypted message
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
                    decrypted = false;
                }
            }
        });
    }

    //updates the label with a timestamp of when the server was started
    @Override
    public void updateServerStarted(String update) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lb_server_start.setText(update);
            }
        });
    }

    //Updates client status
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


    //Updates the textarea of the decrypted message (also decrypts the file )
    private void updateTaDecrypted(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(filebytes != null){

                    ta_decrypted_file.setText(FileUtils.getStringFromByteArray(filebytes));

                }
            }
        });
    }


}
