package sample.AdditionalUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import sample.DataBase.*;
import sample.DataBase.Entities.BaseTable;
import sample.DataBase.Entities.TableType;
import sample.DataSaving.SettingsSaving.SettingsData;
import sample.Graph.BoundaryValues;
import sample.InitialDataSetting.Graph.GraphType;
import sample.Utils.GraphValuesFromTable;
import sample.WorkWindowSettings.WorkWindowSettings;

import java.io.File;
import java.util.List;

public class WorkWindowControllerUtils {

    public static void setGraphTypesMenuButton(MenuButton graphTypes, Label thicknessLabel, Label stressLabel, TextField thicknessTextField,
                                               LineChart lineChart, WorkWindowSettings workWindowSettings) {
        for (GraphType type : GraphType.values()) {
            MenuItem item = new MenuItem();
            item.setText(type.getName());
            graphTypes.getItems().add(item);
            item.setOnAction(event -> {
                graphTypes.setText(type.getName());
                setFieldsActivity(type, thicknessLabel, stressLabel, thicknessTextField);
                workWindowSettings.setGraphType(type);
                plotGraph(lineChart,workWindowSettings);
            });
        }
    }

    public static void setTableTypesMenuButton(MenuButton tableTypes, LineChart lineChart, WorkWindowSettings workWindowSettings) {
        for (TableType type : TableType.values()) {
            MenuItem item = new MenuItem();
            item.setText(type.getName());
            tableTypes.getItems().add(item);
            item.setOnAction(event -> {
                tableTypes.setText(type.getName());
                workWindowSettings.setTableType(type);
                plotGraph(lineChart,workWindowSettings);
            });
        }
    }

    public static void plotGraph( LineChart lineChart, WorkWindowSettings workWindowSettings) {
        if (workWindowSettings.getTableType() != null && workWindowSettings.getGraphType() != null) {
            GraphUtils.InitialGraph(workWindowSettings.getGraphType(),lineChart);
            getDataForGraph(lineChart,workWindowSettings);
        }
    }

    public static void setFieldsActivity(GraphType type, Label thicknessLabel, Label stressLabel, TextField thicknessTextField) {
        if (type == GraphType.StressThickness)
            setFieldsASEnable(thicknessLabel, stressLabel, thicknessTextField);
        else
            setFieldsAsDisable(thicknessLabel, stressLabel, thicknessTextField);
    }

    public static void getDataForGraph(LineChart lineChart,WorkWindowSettings workWindowSettings) {
        lineChart.getData().clear();
        HibernateUtil hibernateUtil = new HibernateUtilForOpening(SettingsData.getInstance().getPathToDB());
        TableHelper tableHelper = null;
        switch (workWindowSettings.getTableType()){
            case DetailedTable:
                tableHelper = new DetailedTableHelper(hibernateUtil);
                break;
            case RegularTable:
                tableHelper = new RegularTableHelper(hibernateUtil);
                break;
            case AbbreviatedTable:
                tableHelper = new AbbreviatedTableHelper(hibernateUtil);
                break;
        }
        List<BaseTable> data = tableHelper.getTable();;
        GraphUtils.InitialGraph(workWindowSettings.getGraphType(), lineChart);
        GraphValuesFromTable graphValuesFromTable = new GraphValuesFromTable();
        for (BaseTable values :
                data) {
            graphValuesFromTable.addData(values.getTimestamp(), values.getMeasuredValue(workWindowSettings.getGraphType()));
        }
        System.out.println( graphValuesFromTable.getData().size() + "SIZE");

        BoundaryValues boundaryValues = new BoundaryValues(graphValuesFromTable.getMinX(), graphValuesFromTable.getMinY(), graphValuesFromTable.getMaxX(), graphValuesFromTable.getMaxY());
      /*
        maxX = graphValuesFromRegularTable.getMaxX();
        minX = graphValuesFromRegularTable.getMinX();
        maxY = graphValuesFromRegularTable.getMaxY();
        minY = graphValuesFromRegularTable.getMinY();
       */
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().addAll(graphValuesFromTable.clone());
        lineChart.getData().add(series);
        GraphUtils.setBoundaries( (NumberAxis) lineChart.getXAxis(),(NumberAxis)lineChart.getYAxis(), boundaryValues.getMinX(), boundaryValues.getMaxX(), boundaryValues.getMinY(), boundaryValues.getMaxY());
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
