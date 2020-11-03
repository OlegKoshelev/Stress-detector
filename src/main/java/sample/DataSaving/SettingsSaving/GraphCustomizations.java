package sample.DataSaving.SettingsSaving;

import sample.InitialDataSetting.Graph.GraphType;
import javafx.scene.paint.Color;

public class GraphCustomizations {
    private static GraphCustomizations instance;
    private GraphType type;
    private Color line;
    private Color grid;

    private GraphCustomizations() {
    }

    public static GraphCustomizations getInstance() {
        if (instance == null) {
            instance = new GraphCustomizations();
        }
        return instance;
    }

    public GraphType getType() {
        return type;
    }

    public void setType(GraphType type) {
        this.type = type;
    }

    public Color getLine() {
        return line;
    }

    public void setLine(Color line) {
        this.line = line;
    }

    public Color getGrid() {
        return grid;
    }

    public void setGrid(Color grid) {
        this.grid = grid;
    }
}
