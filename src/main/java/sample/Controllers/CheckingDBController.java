package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sample.DataSaving.SettingsTransfer;

import java.io.IOException;

public class CheckingDBController {
    @FXML
    private Button ok;

    @FXML
    public void okAction() throws IOException {
        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();
    }
}
