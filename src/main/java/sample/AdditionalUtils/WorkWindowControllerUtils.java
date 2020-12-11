package sample.AdditionalUtils;

import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import sample.DataBase.Entities.TableType;
import sample.InitialDataSetting.Graph.GraphType;
import sample.WorkWindowSettings.WorkWindowSettings;

public class WorkWindowControllerUtils {

    public static void setGraphTypesMenuButton(MenuButton graphTypes, Label thicknessLabel, Label stressLabel, TextField thicknessTextField) {
        for (GraphType type : GraphType.values()) {
            MenuItem item = new MenuItem();
            item.setText(type.getName());
            graphTypes.getItems().add(item);
            item.setOnAction(event -> {
                graphTypes.setText(type.getName());
                setFieldsActivity(type, thicknessLabel, stressLabel, thicknessTextField);
                plotGraph();
            });
        }
    }

    public static void setTableTypesMenuButton(MenuButton tableTypes) {
        for (TableType type : TableType.values()) {
            MenuItem item = new MenuItem();
            item.setText(type.getName());
            tableTypes.getItems().add(item);
            item.setOnAction(event -> {
                tableTypes.setText(type.getName());
                plotGraph();
            });
        }
    }

    public static void plotGraph() {
        if (WorkWindowSettings.getInstance().getTableType() == null || WorkWindowSettings.getInstance().getGraphType() == null) {
        } else {

        }
    }

    public static void setFieldsActivity(GraphType type, Label thicknessLabel, Label stressLabel, TextField thicknessTextField) {
        if (type == GraphType.StressThickness)
            setFieldsASEnable(thicknessLabel, stressLabel, thicknessTextField);
        else
            setFieldsAsDisable(thicknessLabel, stressLabel, thicknessTextField);
    }

    public static void setFieldsAsDisable(Label thicknessLabel, Label stressLabel, TextField thicknessTextField) {
        thicknessLabel.setDisable(true);
        stressLabel.setDisable(true);
        thicknessTextField.setDisable(true);
    }

    public static void setFieldsASEnable(Label thicknessLabel, Label stressLabel, TextField thicknessTextField) {
        thicknessLabel.setDisable(false);
        stressLabel.setDisable(false);
        thicknessTextField.setDisable(false);
    }


}
