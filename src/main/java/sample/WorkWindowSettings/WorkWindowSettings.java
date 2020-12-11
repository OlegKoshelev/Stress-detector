package sample.WorkWindowSettings;

import sample.DataBase.Entities.TableType;
import sample.InitialDataSetting.Graph.GraphType;

public class WorkWindowSettings {
    private static WorkWindowSettings instance;
    private GraphType graphType = null;
    private TableType tableType = null;

    private WorkWindowSettings() {
    }

    public static WorkWindowSettings getInstance() {
        if (instance == null) {
            instance = new WorkWindowSettings();
        }
        return instance;
    }

    public GraphType getGraphType() {
        return graphType;
    }

    public void setGraphType(GraphType graphType) {
        this.graphType = graphType;
    }

    public TableType getTableType() {
        return tableType;
    }

    public void setTableType(TableType tableType) {
        this.tableType = tableType;
    }
}
