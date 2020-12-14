package sample.WorkWindowSettings;

import sample.DataBase.Entities.TableType;
import sample.InitialDataSetting.Graph.GraphType;

public class WorkWindowSettings {
    private GraphType graphType;
    private TableType tableType;



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
