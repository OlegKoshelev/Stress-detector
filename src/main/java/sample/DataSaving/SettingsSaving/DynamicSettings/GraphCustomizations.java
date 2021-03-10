package sample.DataSaving.SettingsSaving.DynamicSettings;

import sample.InitialDataSetting.Graph.GraphType;
import javafx.scene.paint.Color;

public class GraphCustomizations {
    private GraphType type = GraphType.StressThickness;
    private Color line;
    private Color grid;

    public GraphCustomizations() {
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
