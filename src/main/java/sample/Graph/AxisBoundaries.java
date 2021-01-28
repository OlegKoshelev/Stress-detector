package sample.Graph;


import java.awt.*;

public enum AxisBoundaries {
    MaxY, MinY,MaxX,MinX;

    private TextField textField;

    public TextField getTextField() {
        return textField;
    }

    public void setTextField(TextField textField) {
        this.textField = textField;
    }
}
