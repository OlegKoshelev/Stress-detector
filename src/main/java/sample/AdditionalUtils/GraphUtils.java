package sample.AdditionalUtils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import sample.Graph.AxisBoundaries;


public  class GraphUtils {


    public static AxisBoundaries setBoundaryValue (double x, double y, NumberAxis axis, TextField textField, StackPane stP ){ // установка значений на границах осей

        textField.setFont(new Font("SansSerif", 12));
        if ((axis.getBoundsInLocal().getHeight() > axis.getBoundsInLocal().getWidth())) { //ось Y
            textField.setMaxSize(axis.getBoundsInLocal().getWidth() + 10, 30); // изменяем текстового поля размер поля в зависимости от ширины оси
            if ((y < axis.getTickMarks().get(axis.getTickMarks().size()-1).getPosition() + 10)){// max Y
                stP.getChildren().add(textField);
                StackPane.setAlignment(stP.getChildren().get(1),Pos.TOP_LEFT);
                StackPane.setMargin(stP.getChildren().get(1), new Insets(40, 0, 0, 5));
                textField.setText(axis.getUpperBound() + "");
                return AxisBoundaries.MaxY;
            }
            if ((y > axis.getTickMarks().get(0).getPosition() - 10)){ // min Y
                stP.getChildren().add(textField);
                StackPane.setAlignment(stP.getChildren().get(1),Pos.BOTTOM_LEFT);
                StackPane.setMargin(stP.getChildren().get(1), new Insets(0, 0, 35, 5));
                textField.setText(axis.getLowerBound() + "");
                return AxisBoundaries.MinY;
            }
        }
        else{ // ось Х
            textField.setMaxSize(60, 30);
            if ((x > axis.getTickMarks().get(axis.getTickMarks().size()-1).getPosition() - 10)){// max X
                stP.getChildren().add(textField);
                StackPane.setAlignment(stP.getChildren().get(1), Pos.BOTTOM_RIGHT);
                StackPane.setMargin(stP.getChildren().get(1), new Insets(0, 10, 7, 0));
                textField.setText(axis.getUpperBound() + "");
                return AxisBoundaries.MaxX;
            }
            if ((x < axis.getTickMarks().get(0).getPosition() + 10)){ // min X
                stP.getChildren().add(textField);
                StackPane.setAlignment(stP.getChildren().get(1),Pos.BOTTOM_LEFT);
                StackPane.setMargin(stP.getChildren().get(1), new Insets(0, 0, 7, 40));
                textField.setText(axis.getLowerBound() + "");
                return AxisBoundaries.MinX;
            }
        }
        return null;
    }




}
