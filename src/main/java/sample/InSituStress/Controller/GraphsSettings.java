package sample.InSituStress.Controller;

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
        series.setName("Distance(time)");
        yAxis.setLabel("Distance");
        xAxis.setLabel("Time");
        Date date = new Date();
        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);
        xAxis.setLowerBound(date.getTime());
        xAxis.setUpperBound(date.getTime() + 400000);
        xAxis.setTickUnit(100000);
        xAxis.setTickLabelsVisible(true);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(150);
        yAxis.setTickUnit(25);
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
    }
}
