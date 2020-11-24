package sample.AdditionalUtils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import sample.Graph.AxisBoundaries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class GraphUtils {


    public static AxisBoundaries setBoundaryValue(double x, double y, NumberAxis axis, TextField textField, StackPane stP) { // установка значений на границах осей

        textField.setFont(new Font("SansSerif", 12));
        if ((axis.getBoundsInLocal().getHeight() > axis.getBoundsInLocal().getWidth())) { //ось Y
            textField.setMaxSize(axis.getBoundsInLocal().getWidth() + 10, 30); // изменяем текстового поля размер поля в зависимости от ширины оси
            if ((y < axis.getTickMarks().get(axis.getTickMarks().size() - 1).getPosition() + 10)) {// max Y
                stP.getChildren().add(textField);
                StackPane.setAlignment(stP.getChildren().get(1), Pos.TOP_LEFT);
                StackPane.setMargin(stP.getChildren().get(1), new Insets(5, 0, 0, 5));
                textField.setText(axis.getUpperBound() + "");
                return AxisBoundaries.MaxY;
            }
            if ((y > axis.getTickMarks().get(0).getPosition() - 15)) { // min Y
                stP.getChildren().add(textField);
                StackPane.setAlignment(stP.getChildren().get(1), Pos.BOTTOM_LEFT);
                StackPane.setMargin(stP.getChildren().get(1), new Insets(0, 0, 60, 5));
                textField.setText(axis.getLowerBound() + "");
                return AxisBoundaries.MinY;
            }
        } else { // ось Х
            textField.setMaxSize(80, 30);
            if ((x > axis.getTickMarks().get(axis.getTickMarks().size() - 1).getPosition() - 25)) {// max X
                stP.getChildren().add(textField);
                StackPane.setAlignment(stP.getChildren().get(1), Pos.BOTTOM_RIGHT);
                StackPane.setMargin(stP.getChildren().get(1), new Insets(0, 1, 30, 0));
                textField.setText(dateToString(new Date((long) axis.getUpperBound())));
                return AxisBoundaries.MaxX;
            }
            if ((x < axis.getTickMarks().get(0).getPosition() + 25)) { // min X
                stP.getChildren().add(textField);
                StackPane.setAlignment(stP.getChildren().get(1), Pos.BOTTOM_LEFT);
                StackPane.setMargin(stP.getChildren().get(1), new Insets(0, 0, 30, 85));
                textField.setText(dateToString(new Date((long) axis.getLowerBound())));
                return AxisBoundaries.MinX;
            }
        }
        return null;
    }

    public static String dateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(date);
    }

    public static Date stringToDate(String string, Date plottingDate ) throws ParseException { // выясняем в кааом формате ввел время пользователь
        if (!string.matches("(\\d\\d:?){1,3}"))
            return null;

        String cutStr = string;
        if (string.contains(":")) {
            cutStr = string.replaceAll(":", "");
        }
        SimpleDateFormat formatter = null;
        switch (string.length() - cutStr.length()) {
            case 0:
                formatter = new SimpleDateFormat("HH");
                break;
            case 1:
                formatter = new SimpleDateFormat("HH:mm");
                break;
            case 2:
                formatter = new SimpleDateFormat("HH:mm:ss");
                break;
        }

        Calendar calendar = new GregorianCalendar(); // установка даты соответствующей таде создания данных графика
        calendar.setTime(formatter.parse(string));
        int hour = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        int milliseconds =calendar.get(Calendar.MILLISECOND);
        calendar.setTime(plottingDate);
        calendar.set(Calendar.HOUR,hour);
        calendar.set(Calendar.MINUTE,minutes);
        calendar.set(Calendar.SECOND,seconds);
        calendar.set(Calendar.MILLISECOND,milliseconds);
        return calendar.getTime();
    }


    public static void InitialGraph(LineChart<Number, Number> chart, NumberAxis xAxis, NumberAxis yAxis, XYChart.Series<Number, Number> series) {
        yAxis.setLabel("Stress*Thickness(GPa*um)");
        xAxis.setLabel("Time");
        xAxis.setAutoRanging(false);
        yAxis.setAutoRanging(false);
        chart.setCreateSymbols(false);
        Date date = new Date();
        xAxis.setLowerBound(date.getTime());
        xAxis.setUpperBound(date.getTime() + 40000000);
        xAxis.setTickUnit(10000000);
        xAxis.setTickLabelsVisible(true);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(150);
        yAxis.setTickUnit(30);
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
                return Double.valueOf(string);
            }
        });
    }

}
