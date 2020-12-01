package sample.Utils;

import com.sun.javafx.charts.Legend;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.StringConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GraphsSettings {


    public static void DistanceGraph(LineChart<Number, Number> chart, NumberAxis xAxis, NumberAxis yAxis, XYChart.Series<Number, Number> series) {
        series.setName("Stress*Thickness(time)");
        yAxis.setLabel("Stress*Thickness(GPa*um)");
        xAxis.setLabel("Time");
        chart.setLegendVisible(false);
        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);
        xAxis.setTickLabelsVisible(true);
        yAxis.setAutoRanging(false);
        xAxis.setAutoRanging(false);
        xAxis.setAnimated(true);
        yAxis.setAnimated(true);

        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                Date date = new Date(object.longValue());
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
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
        yAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                double number = object.floatValue();

                return String.format("%.1e %n", number);
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
}
