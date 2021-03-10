package sample.Controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.AdditionalUtils.StaticSettingsControllerUtils;
import sample.DataSaving.SettingsSaving.SettingsTransfer;
import sample.DataSaving.SettingsSaving.StaticSettings.SetupCustomizations;


import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class StaticSettingsController implements Initializable {
    @FXML
    private ImageView imageView;
    @FXML
    private TextField angle;
    @FXML
    private TextField distance;
    @FXML
    private TextField d0;

    private SetupCustomizations setupCustomizations = new SetupCustomizations();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SettingsTransfer.transferFromFullSettingsToStaticOptions(setupCustomizations);
        StaticSettingsControllerUtils.uploadImage(imageView);
        StaticSettingsControllerUtils.textFieldsCustomization(angle,distance,d0,setupCustomizations);
    }

    @FXML
    public void applyAction() throws IOException, URISyntaxException {
        SettingsTransfer.transferFromStaticOptionsToFullSettings(setupCustomizations);
        SettingsTransfer.writeToSettingsFile();
    }

    @FXML
    public void okAction(Event event) throws IOException, URISyntaxException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        SettingsTransfer.writeToSettingsFile();
        stage.close();
    }
}
