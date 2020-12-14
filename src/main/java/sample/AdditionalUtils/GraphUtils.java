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
import sample.Graph.BoundaryValues;
import sample.InitialDataSetting.Graph.GraphType;

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
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        int milliseconds =calendar.get(Calendar.MILLISECOND);
        calendar.setTime(plottingDate);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minutes);
        calendar.set(Calendar.SECOND,seconds);
        calendar.set(Calendar.MILLISECOND,milliseconds);
        return calendar.getTime();
    }

    public static  void changeBoundaries (long xValue, double yValue, NumberAxis xAxis, NumberAxis yAxis, BoundaryValues boundaryValues, boolean autoRangingIsSelected ){
        if (autoRangingIsSelected) {
            double deltaX  =  (boundaryValues.getMaxX() - boundaryValues.getMinX()) * 0.4;

            if (xValue > (boundaryValues.getMaxX() + deltaX/2)) {
                boundaryValues.setMaxX(xValue);
                deltaX  =  (boundaryValues.getMaxX() - boundaryValues.getMinX()) * 0.4;
                setAxisSettings(xAxis,boundaryValues.getMinX(),(long) (boundaryValues.getMaxX() + deltaX));
            }

            if (yValue > boundaryValues.getMaxY()) {
                boundaryValues.setMaxY(yValue);
                double deltaY =   Math.abs(boundaryValues.getMaxY() - boundaryValues.getMinY());
                setAxisSettings(yAxis,(boundaryValues.getMinY() - deltaY),(boundaryValues.getMaxY() + deltaY));
            }

            if (yValue < boundaryValues.getMinY()) {
                boundaryValues.setMinY(yValue);
                double deltaY =    Math.abs(boundaryValues.getMaxY() - boundaryValues.getMinY());
                setAxisSettings(yAxis,(boundaryValues.getMinY() - deltaY),(boundaryValues.getMaxY() + deltaY));
            }
        }
    }



    public static void setAxisSettings(NumberAxis axis, long minValue, long maxValue){
        axis.setLowerBound(minValue);
        axis.setUpperBound(maxValue);
        axis.setTickUnit((double) (maxValue - minValue) / 5);
        axis.setAutoRanging(false);
        axis.setAnimated(true);
    }

    public static   void setAxisSettings (NumberAxis axis, double minValue, double maxValue){
        axis.setLowerBound(minValue);
        axis.setUpperBound(maxValue);
        axis.setTickUnit((Math.abs(maxValue - minValue)) / 5);
        axis.setAutoRanging(false);
        axis.setAnimated(true);
    }


    public static void setBoundaries(NumberAxis xAxis, NumberAxis yAxis, long minX, long maxX, double minY, double maxY){
        double deltaX  =  (maxX - minX) * 0.4;
        double deltaY = Math.abs(maxY - minY) * 0.8;
        maxX = (long) (maxX + deltaX);
        minY =  minY - deltaY;
        maxY =  maxY + deltaY;
        setAxisSettings(xAxis,minX,maxX);
        setAxisSettings(yAxis,minY,maxY);
    }




    public static void InitialGraph(GraphType graphType,LineChart<Number, Number> chart, NumberAxis xAxis, NumberAxis yAxis, XYChart.Series<Number, Number> series) {

        yAxis.setLabel(graphType.getName());
        chart.setLegendVisible(false);
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



    public static void InitialGraph(GraphType graphType,LineChart<Number, Number> chart) {
        NumberAxis xAxis = (NumberAxis) chart.getXAxis();
        NumberAxis yAxis = (NumberAxis) chart.getYAxis();

        yAxis.setLabel(graphType.getName());
        chart.setLegendVisible(false);
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
