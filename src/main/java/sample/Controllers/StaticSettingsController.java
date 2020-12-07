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


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StaticSettingsController implements Initializable {
    @FXML
    private ImageView imageView;
    @FXML
    private TextField angle;
    @FXML
    private TextField distance;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SettingsTransfer.transferFromFullSettingsToStaticOptions();
        StaticSettingsControllerUtils.uploadImage(imageView);
        StaticSettingsControllerUtils.textFieldsCustomization(angle,distance);
    }

    @FXML
    public void applyAction() {
        SettingsTransfer.transferFromStaticOptionsToFullSettings();
    }

    @FXML
    public void okAction(Event event) throws IOException {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        SettingsTransfer.writeToSettingsFile();
        stage.close();
    }




}
