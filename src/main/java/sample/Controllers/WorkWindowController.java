package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import sample.AdditionalUtils.WorkWindowControllerUtils;



import java.net.URL;
import java.util.ResourceBundle;

public class WorkWindowController implements Initializable {
    @FXML
    private MenuButton graphTypes;
    @FXML
    private MenuButton tableTypes;
    @FXML
    private Label thicknessLabel;
    @FXML
    private Label stressLabel;
    @FXML
    private TextField thicknessTextField;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WorkWindowControllerUtils.setGraphTypesMenuButton(graphTypes,thicknessLabel,stressLabel,thicknessTextField);
        WorkWindowControllerUtils.setTableTypesMenuButton(tableTypes);
        WorkWindowControllerUtils.setFieldsAsDisable(thicknessLabel,stressLabel,thicknessTextField);
    }


}
