package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Stage stage = new Stage();

        Parent rootServer = FXMLLoader.load(getClass().getResource("../view/server_gui.fxml"));
        stage.setTitle("Server");
        stage.setScene(new Scene(rootServer, 600, 400));
        stage.show();

        Parent root = FXMLLoader.load(getClass().getResource("../view/encryption_gui.fxml"));
        primaryStage.setTitle("Encryption program");
        primaryStage.setScene(new Scene(root, 691, 400));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);

    }
}
