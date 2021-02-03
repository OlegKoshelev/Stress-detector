package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import sample.AdditionalUtils.WorkWindowControllerUtils;
import sample.WorkWindowSettings.WorkWindowSettings;


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
    @FXML
    private LineChart lineChart;

    private WorkWindowSettings workWindowSettings = new WorkWindowSettings();







    @Override
    public void initialize(URL location, ResourceBundle resources) {
/*        WorkWindowControllerUtils.setGraphTypesMenuButton(graphTypes,thicknessLabel,stressLabel,thicknessTextField, lineChart,workWindowSettings);
        WorkWindowControllerUtils.setTableTypesMenuButton(tableTypes,lineChart,workWindowSettings);
        WorkWindowControllerUtils.setFieldsAsDisable(thicknessLabel,stressLabel,thicknessTextField);*/
    }


}
