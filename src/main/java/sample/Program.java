package sample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Controllers.MainController;
import sample.Controllers.SettingsController;

import java.io.File;
import java.io.InputStream;
import java.net.URL;


public class Program extends Application {

    public static void main(String[] args) throws Exception {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainWindow.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            controller.shutdown();
        });
    }

}






