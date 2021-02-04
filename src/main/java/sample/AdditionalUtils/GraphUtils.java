package sample.AdditionalUtils;


import de.gsi.chart.axes.spi.DefaultNumericAxis;
import de.gsi.chart.plugins.ChartPlugin;
import de.gsi.chart.plugins.ParameterMeasurements;
import de.gsi.chart.plugins.YValueIndicator;
import de.gsi.chart.plugins.Zoomer;
import de.gsi.dataset.spi.DefaultErrorDataSet;
import de.gsi.dataset.utils.ProcessingProfiler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import sample.DataSaving.SettingsSaving.SettingsData;
import sample.Graph.AxisBoundaries;
import sample.Graph.BoundaryValues;
import sample.InitialDataSetting.Graph.GraphType;
import sample.Plugins.DataPoint;
import sample.Plugins.Markers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class GraphUtils {

    public static void initialGraph(de.gsi.chart.XYChart chart, DefaultErrorDataSet dataSet){
        DefaultNumericAxis xAxis = (DefaultNumericAxis) chart.getXAxis();
        DefaultNumericAxis yAxis = (DefaultNumericAxis) chart.getYAxis();
        yAxis.setName(SettingsData.getInstance().getType().getName());
        xAxis.setName("Time");
        yAxis.setTickLabelFont(Font.font("Times", 15));
        xAxis.setTickLabelFont(Font.font("Times", 15));
        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);
        chart.getDatasets().add(dataSet);
        chart.getPlugins().add(new DataPoint());
        chart.getPlugins().add(new Zoomer());
        chart.getPlugins().add(new Markers());
        yAxis.setAnimated(false);
        xAxis.setAnimated(false);
       // Platform.runLater(() -> dataSet.fireInvalidated(null));

        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                Date date = new Date(object.longValue());
                DateFormat dateFormat = null;
                if (xAxis.getMax() -  xAxis.getMin() < 10000)
                    dateFormat = new SimpleDateFormat("HH:mm:ss.SS");
                if (xAxis.getMax() -  xAxis.getMin() >= 10000 && xAxis.getMax() -  xAxis.getMin() <3600000)
                    dateFormat = new SimpleDateFormat("HH:mm:ss");
                if (xAxis.getMax() -  xAxis.getMin() >= 3600000)
                    dateFormat = new SimpleDateFormat("HH:mm");
                return dateFormat.format(date);
            }

            @Override
            public Number fromString(String string) {
                Date todayDate = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                Number result = null;
                try {
                    Date date = formatter.parse(string);
                    result = date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return result;
            }
        });

    }

    public static void repaint(de.gsi.chart.XYChart chart){
        DefaultNumericAxis yAxis = (DefaultNumericAxis) chart.getYAxis();
        yAxis.setName(SettingsData.getInstance().getType().getName());
    }
}
