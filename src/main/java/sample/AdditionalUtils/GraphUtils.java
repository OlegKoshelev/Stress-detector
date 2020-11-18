package sample.AdditionalUtils;

import javafx.scene.chart.NumberAxis;
import sample.Graph.AxisBoundaries;

public  class GraphUtils {
    public static AxisBoundaries getBoundary (double x, double y, NumberAxis axis){

        if ((axis.getBoundsInLocal().getHeight() > axis.getBoundsInLocal().getWidth())) { //ось Y
            if ((y < axis.getTickMarks().get(axis.getTickMarks().size()-1).getPosition() + 10)){
                return AxisBoundaries.MaxY;
            }
            if ((y > axis.getTickMarks().get(0).getPosition() - 10)){
                return AxisBoundaries.MinY;
            }
        }
        else{ // ось Х
            if ((x > axis.getTickMarks().get(axis.getTickMarks().size()-1).getPosition() - 10)){
                return AxisBoundaries.MaxY;
            }
            if ((x < axis.getTickMarks().get(0).getPosition() + 10)){
                return AxisBoundaries.MinY;
            }
        }

        return null;
    }

}
