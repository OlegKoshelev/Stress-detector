package sample.Plugins;

import de.gsi.chart.axes.Axis;
import de.gsi.chart.plugins.AbstractSingleValueIndicator;
import de.gsi.chart.plugins.XValueIndicator;
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
            label.setText(String.format("T: %s",new SimpleDateFormat("HH:mm:ss").format(new Date((long)getValue()))));
        }
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
