package sample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStream;
import java.net.URL;


public class Program extends Application {

    public static void main(String[] args) throws Exception {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("mainWindow.fxml");
        Parent root = new FXMLLoader().load(inputStream);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}






