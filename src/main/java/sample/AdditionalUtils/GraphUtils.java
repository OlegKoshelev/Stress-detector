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
                StackPane.setMargin(stP.getChildren().get(1), new Insets(0, 0, 50, 5));
                textField.setText(axis.getLowerBound() + "");
                return AxisBoundaries.MinY;
            }
        } else { // ось Х
            textField.setMaxSize(80, 30);
            if ((x > axis.getTickMarks().get(axis.getTickMarks().size() - 1).getPosition() - 25)) {// max X
                stP.getChildren().add(textField);
                StackPane.setAlignment(stP.getChildren().get(1), Pos.BOTTOM_RIGHT);
                StackPane.setMargin(stP.getChildren().get(1), new Insets(0, 2, 24, 0));
                textField.setText(dateToString(new Date((long) axis.getUpperBound())));
                return AxisBoundaries.MaxX;
            }
            if ((x < axis.getTickMarks().get(0).getPosition() + 25)) { // min X
                stP.getChildren().add(textField);
                StackPane.setAlignment(stP.getChildren().get(1), Pos.BOTTOM_LEFT);
                StackPane.setMargin(stP.getChildren().get(1), new Insets(0, 0, 24, 75));
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

    public static Date stringToDate(String string, Date plottingDate) throws ParseException { // выясняем в кааом формате ввел время пользователь
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
        int milliseconds = calendar.get(Calendar.MILLISECOND);
        calendar.setTime(plottingDate);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);
        calendar.set(Calendar.MILLISECOND, milliseconds);
        return calendar.getTime();
    }

    public static void changeBoundaries(long xValue, double yValue, NumberAxis xAxis, NumberAxis yAxis, BoundaryValues boundaryValues, boolean autoRangingIsSelected) {
        if (autoRangingIsSelected) {
            double deltaX = (boundaryValues.getMaxX() - boundaryValues.getMinX()) * 0.4;

            if (xValue > (boundaryValues.getMaxX() + deltaX / 2)) {
                boundaryValues.setMaxX(xValue);
                deltaX = (boundaryValues.getMaxX() - boundaryValues.getMinX()) * 0.4;
                setAxisSettings(xAxis, boundaryValues.getMinX(), (long) (boundaryValues.getMaxX() + deltaX));
            }

            if (yValue > boundaryValues.getMaxY()) {
                boundaryValues.setMaxY(yValue);
                double deltaY = Math.abs(boundaryValues.getMaxY() - boundaryValues.getMinY());
                setAxisSettings(yAxis, (boundaryValues.getMinY() - deltaY), (boundaryValues.getMaxY() + deltaY));
            }

            if (yValue < boundaryValues.getMinY()) {
                boundaryValues.setMinY(yValue);
                double deltaY = Math.abs(boundaryValues.getMaxY() - boundaryValues.getMinY());
                setAxisSettings(yAxis, (boundaryValues.getMinY() - deltaY), (boundaryValues.getMaxY() + deltaY));
            }
        }
    }


    public static void setAxisSettings(NumberAxis axis, long minValue, long maxValue) {
        axis.setLowerBound(minValue);
        axis.setUpperBound(maxValue);
        axis.setTickUnit((double) (maxValue - minValue) / 5);
        axis.setAutoRanging(false);
        axis.setAnimated(true);
    }

    public static void setAxisSettings(NumberAxis axis, double minValue, double maxValue) {
        axis.setLowerBound(minValue);
        axis.setUpperBound(maxValue);
        axis.setTickUnit((Math.abs(maxValue - minValue)) / 5);
        axis.setAutoRanging(false);
        axis.setAnimated(true);
    }


    public static void setBoundaries(NumberAxis xAxis, NumberAxis yAxis, long minX, long maxX, double minY, double maxY) {
        double deltaX = (maxX - minX) * 0.4;
        double deltaY = Math.abs(maxY - minY) * 0.8;
        maxX = (long) (maxX + deltaX);
        minY = minY - deltaY;
        maxY = maxY + deltaY;
        setAxisSettings(xAxis, minX, maxX);
        setAxisSettings(yAxis, minY, maxY);
    }


    public static void InitialGraph(GraphType graphType, LineChart<Number, Number> chart, NumberAxis xAxis, NumberAxis yAxis, XYChart.Series<Number, Number> series) {

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



    public static void InitialGraph(GraphType graphType, LineChart<Number, Number> chart) {
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
        chart.setAnimated(false);


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
                if (xAxis.getMax() -  xAxis.getMin() < 1000)
                    dateFormat = new SimpleDateFormat("HH:mm:ss.SS");
                if (xAxis.getMax() -  xAxis.getMin() >= 1000 && xAxis.getMax() -  xAxis.getMin() <3600000)
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

    public static void InitialGraph(GraphType graphType, de.gsi.chart.XYChart chart) {

        DefaultNumericAxis xAxis = (DefaultNumericAxis) chart.getXAxis();
        DefaultNumericAxis yAxis = (DefaultNumericAxis) chart.getYAxis();
        yAxis.setName(graphType.getName());
        xAxis.setName("Time");


        yAxis.setTickLabelFont(Font.font("Times", 15));

        final DefaultErrorDataSet dataSet = new DefaultErrorDataSet("TestData");
        generateData(dataSet);
        chart.getDatasets().add(dataSet);
        chart.getPlugins().add(new Zoomer());


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


    private static void generateData(final DefaultErrorDataSet dataSet) {
        final long startTime = ProcessingProfiler.getTimeStamp();

        dataSet.autoNotification().set(false);
        dataSet.clearData();
        final double now = new Date().getTime() + 10; // N.B. '+1'
        System.out.println(now);
        // to check
        // for
        // resolution
        for (int n = 0; n < 300000; n++) {
            double t = now + n * 10;

            final double y = 100 * Math.cos(Math.PI * t * 0.0005) + 0 * 0.001 * (t - now) + 0 * 1e4;
            final double ex = 1;
            final double ey = 10;
            dataSet.add(t, y, ex, ey);
        }
        dataSet.autoNotification().set(true);


    }


}
