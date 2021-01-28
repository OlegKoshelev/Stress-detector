package sample.Plugins;

import de.gsi.chart.axes.Axis;
import de.gsi.chart.plugins.AbstractSingleValueIndicator;
import de.gsi.chart.plugins.XValueIndicator;
import de.gsi.dataset.DataSet;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MyXValueIndicator extends XValueIndicator {
    protected Line xLabel;


    public MyXValueIndicator(Axis axis, double value) {
        super(axis, value);
    }

    public MyXValueIndicator(Axis axis, double value, String text) {
        super(axis, value, text);

    }

    @Override
    public void layoutChildren() {
        if (getChart() == null) {
            return;
        }

        final Bounds plotAreaBounds = getChart().getCanvas().getBoundsInLocal();
        final double minX = plotAreaBounds.getMinX();
        final double maxX = plotAreaBounds.getMaxX();
        final double minY = plotAreaBounds.getMinY();
        final double maxY = plotAreaBounds.getMaxY();
        final double xPos = minX + getChart().getFirstAxis(Orientation.HORIZONTAL).getDisplayPosition(getValue());

        if (xPos < minX || xPos > maxX) {
            getChartChildren().clear();
        } else {
            layoutLine(xPos, minY, xPos, maxY);
            layoutMarker(xPos, minY + 1.5 * AbstractSingleValueIndicator.triangleHalfWidth, xPos, maxY);
            layoutLabel(new BoundingBox(xPos, minY, 0, maxY - minY), AbstractSingleValueIndicator.MIDDLE_POSITION,
                    getLabelPosition());
            label.setText(String.format("Val. %.4f\nTime %s",getYValue(),new SimpleDateFormat("HH:mm:ss").format(new Date((long)getValue()))));

           // label.setText(String.format("%.3f",getYValue()));
        }
    }
    
    public double getYValue(){
        if (getChart().getDatasets().size()!= 1)
            return 0;
        int size = getChart().getDatasets().get(0).getDataCount(DataSet.DIM_X);
        long searchedX = (long) getValue();
        long prevX = 0;
        long nextX = 0;
        int prevIn = 0;
        int nextIn = 0;
        for (int i = 0; i < size; i++){
            long currentX = (long) getChart().getDatasets().get(0).get(DataSet.DIM_X,i);
            if (currentX < searchedX){
                prevX = currentX;
                prevIn = i;
            }
            else{
                nextX = currentX;
                nextIn = i;
                break;
            }
        }
        if (Math.abs(prevX - searchedX) < Math.abs(nextX - searchedX))
            return getChart().getDatasets().get(0).get(DataSet.DIM_Y,prevIn);
        else
            return getChart().getDatasets().get(0).get(DataSet.DIM_Y,nextIn);
    }


    @Override
    public void updateStyleClass() {
        setStyleClasses(label, "x-", AbstractSingleValueIndicator.STYLE_CLASS_LABEL);
        setStyleClasses(triangle, "x-", AbstractSingleValueIndicator.STYLE_CLASS_MARKER);
        line.setStyle("-fx-stroke: green; -fx-background-color: green;");

    }

    public double getLabelY(){
        return label.getLayoutX();
    }
    public double getLabelX(){
        return label.getLayoutY();
    }
}
