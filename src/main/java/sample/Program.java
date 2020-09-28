package sample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;


public class Program extends Application {

    public static void main(String[] args) throws Exception {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL mainWindow = getClass().getResource("/mainWindow.fxml");
        Parent root = FXMLLoader.load(mainWindow);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}






