package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.Chart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sample.AdditionalUtils.GraphUtils;
import sample.AdditionalUtils.MainControllerUtils;
import sample.DataBase.*;
import sample.DataBase.Entities.AbbreviatedTable;
import sample.DataBase.Entities.BaseTable;
import sample.DataBase.Entities.RegularTable;
import sample.DataBase.Entities.DetailedTable;
import sample.DataSaving.SettingsSaving.SettingsData;
import sample.DataSaving.SettingsSaving.SettingsTransfer;
import sample.Graph.AxisBoundaries;
import sample.Graph.BoundaryValues;
import sample.Utils.*;
import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.hibernate.cfg.Configuration;
import sample.DataGetting.Values;
import sample.DataGetting.ValuesLayer;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;

public class MainController implements Initializable {

    @FXML
    CheckMenuItem autoRanging;
    private double d0 = 0;
    @FXML
    private StackPane stackPane;
    @FXML
    private VBox vBox;

    @FXML
    private Stage settings;
    private BlockingQueue<Values> values;
    private ValuesLayer modelLayer;

    @FXML
    private ImageView imageView;
    @FXML
    private LineChart<Number, Number> chart;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    private XYChart.Series<Number, Number> series = new XYChart.Series<>();
    private Thread thread;
    private Configuration dBConfiguration;
    private Boolean autoRangingFlag = false;

    private HibernateUtil hibernateUtil;

    private BoundaryValues boundaryValues = new BoundaryValues(0, 0, 0, 0);

    private double maxY = 0;

    private double minY = 0;


    private long maxX = 0;

    private long minX = 0;

    private TextField textField = null;

    private TemporaryValues detailedTableValues = new TemporaryValues();
    private TemporaryValues regularTableValues = new TemporaryValues();
    private TemporaryValues abbreviatedTableValues = new TemporaryValues();

    @FXML
    private Button run;

    public void setValues(BlockingQueue<Values> values) {
        this.values = values;
    }

    private GraphValuesFromTable graphValuesFromAbbreviatedTable = new GraphValuesFromTable();
    private GraphValuesFromTable graphValuesFromRegularTable = new GraphValuesFromTable();
    private AxisBoundaries axisBoundary = null;


    @FXML
    public void singleMouseClickAndPushingEnter(MouseEvent event) throws ParseException {
        if (event.getButton().equals(MouseButton.PRIMARY)) {// сброс отображения полей ввода диапазонов осей
            if (event.getClickCount() == 1) {
                if (stackPane.getChildren().size() == 2) {
                    stackPane.getChildren().remove(1);
                    switch (axisBoundary) {
                        case MinY:
                            System.out.println(textField.getText());
                            yAxis.setLowerBound(Double.parseDouble(textField.getText()));
                            yAxis.setTickUnit((yAxis.getUpperBound() - yAxis.getLowerBound()) / 5);
                            break;
                        case MaxY:
                            yAxis.setUpperBound(Double.parseDouble(textField.getText()));
                            yAxis.setTickUnit((yAxis.getUpperBound() - yAxis.getLowerBound()) / 5);
                            break;
                        case MinX:
                            if (textField.getText() == null ||
                                    textField.getText().isEmpty() ||
                                    GraphUtils.stringToDate(textField.getText(), new Date((long) xAxis.getLowerBound())) == null) {
                                break;
                            }
                            xAxis.setLowerBound(Objects.requireNonNull(GraphUtils.stringToDate(textField.getText(), new Date((long) xAxis.getLowerBound()))).getTime());
                            xAxis.setTickUnit((xAxis.getUpperBound() - xAxis.getLowerBound()) / 5);
                            break;
                        case MaxX:
                            if (textField.getText() == null ||
                                    textField.getText().isEmpty() ||
                                    GraphUtils.stringToDate(textField.getText(), new Date((long) xAxis.getUpperBound())) == null) {
                                break;
                            }
                            xAxis.setUpperBound(Objects.requireNonNull(GraphUtils.stringToDate(textField.getText(), new Date((long) xAxis.getUpperBound()))).getTime());
                            xAxis.setTickUnit((xAxis.getUpperBound() - xAxis.getLowerBound()) / 5);
                            break;
                    }
                    autoRangingFlag = MainControllerUtils.removeAutoRanging(autoRanging);
                    textField = null;
                }
            }
        }
    }

    @FXML
    public void doubleMouseClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) { // отобразить поле ввода для изменения диапазона оси
            if (event.getClickCount() == 2) {
                if ((stackPane.getChildren().size() == 1)) {
                    textField = new TextField("");
                    axisBoundary = GraphUtils.setBoundaryValue(event.getX(), event.getY(), (NumberAxis) event.getSource(), textField, stackPane);
                    textField.addEventHandler(KeyEvent.KEY_PRESSED, event1 -> {
                        if (event1.getCode() == KeyCode.ENTER) {
                            if (stackPane.getChildren().size() == 2) {
                                stackPane.getChildren().remove(1);
                                switch (axisBoundary) {
                                    case MinY:
                                        System.out.println(textField.getText());
                                        yAxis.setLowerBound(Double.parseDouble(textField.getText()));
                                        yAxis.setTickUnit((yAxis.getUpperBound() - yAxis.getLowerBound()) / 5);
                                        break;
                                    case MaxY:
                                        yAxis.setUpperBound(Double.parseDouble(textField.getText()));
                                        yAxis.setTickUnit((yAxis.getUpperBound() - yAxis.getLowerBound()) / 5);
                                        break;
                                    case MinX:
                                        try {
                                            if (textField.getText() == null ||
                                                    textField.getText().isEmpty() ||
                                                    GraphUtils.stringToDate(textField.getText(), new Date((long) xAxis.getLowerBound())) == null) {
                                                break;
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            xAxis.setLowerBound(Objects.requireNonNull(GraphUtils.stringToDate(textField.getText(), new Date((long) xAxis.getLowerBound()))).getTime());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        xAxis.setTickUnit((xAxis.getUpperBound() - xAxis.getLowerBound()) / 5);
                                        break;
                                    case MaxX:
                                        try {
                                            if (textField.getText() == null ||
                                                    textField.getText().isEmpty() ||
                                                    GraphUtils.stringToDate(textField.getText(), new Date((long) xAxis.getUpperBound())) == null) {
                                                break;
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            xAxis.setUpperBound(Objects.requireNonNull(GraphUtils.stringToDate(textField.getText(), new Date((long) xAxis.getUpperBound()))).getTime());
                                            xAxis.setTickUnit((xAxis.getUpperBound() - xAxis.getLowerBound()) / 5);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        break;
                                }
                                autoRangingFlag = MainControllerUtils.removeAutoRanging(autoRanging);
                                textField = null;
                            }

                        }
                    });
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            SettingsTransfer.readFromSettingsFile(SettingsData.getInstance());
        } catch (IOException e) {
            e.printStackTrace();
        }
        GraphUtils.InitialGraph(SettingsData.getInstance().getType(), chart, xAxis, yAxis, series);
    }


    @FXML
    void showSettings() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        Parent root = loader.load();
        SettingsController controller = loader.getController();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Settings");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnHidden(event -> {
            GraphUtils.InitialGraph(SettingsData.getInstance().getType(), chart, xAxis, yAxis, series);
            controller.shutdown();
        });
    }

    public void graphRepaint (){
        GraphUtils.InitialGraph(SettingsData.getInstance().getType(), chart, xAxis, yAxis, series);
    }

    @FXML
    public void print() throws Exception {

        if (hibernateUtil == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CheckingDB.fxml"));
            Parent root = loader.load();
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Checking DB");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            return;
        }
        if (thread == null) {
            chart.getData().clear();
            xAxis.setUpperBound(xAxis.getUpperBound() + 40000);
            series = new XYChart.Series<>();
            chart.getData().add(series);
            chart.setLegendVisible(false);
        }

        modelLayer = new ValuesLayer(2, d0);
        values = modelLayer.getValuesQueue();
        createThread();
        thread.start();
    }

    @FXML
    public void changeAutoRanging() {
        if (autoRanging.isSelected()) {
            autoRangingFlag = MainControllerUtils.setAutoRanging(autoRanging);
            boundaryValues = new BoundaryValues(graphValuesFromRegularTable.getMinX(), graphValuesFromRegularTable.getMinY(), graphValuesFromRegularTable.getMaxX(), graphValuesFromRegularTable.getMaxY());
            /*
                        minY = graphValuesFromRegularTable.getMinY();
            maxY = graphValuesFromRegularTable.getMaxY();
            minX = graphValuesFromRegularTable.getMinX();
            maxX = graphValuesFromRegularTable.getMaxX();
             */
            GraphUtils.setBoundaries(xAxis, yAxis, boundaryValues.getMinX(), boundaryValues.getMaxX(), boundaryValues.getMinY(), boundaryValues.getMaxY());
        } else {
            autoRangingFlag = MainControllerUtils.removeAutoRanging(autoRanging);
        }
    }


/*
    private void changeBoundaries(long xValue, double yValue) {
        if (autoRangingFlag) {
            if (xValue > maxX - 50000) {
                maxX = xValue + 100000;
                xAxis.setUpperBound(maxX);
                double v = (double) (maxX - minX) / 5;
                xAxis.setTickUnit(v);
            }

            if (yValue > maxY) {
                maxY = yValue + Math.abs(yValue * 0.1);
                yAxis.setUpperBound(maxY);
                yAxis.setTickUnit((maxY - minY) / 5);
            }

            if (yValue < minY) {
                minY = yValue - Math.abs(yValue * 0.1);
                yAxis.setLowerBound(minY);
                yAxis.setTickUnit((maxY - minY) / 5);
            }
        }
    }
 */

    public void addDataToTable() {
        // заносим данные в подробную таблицу
        DetailedTableHelper detailedTableHelper = new DetailedTableHelper(hibernateUtil);
        detailedTableHelper.addTableList(detailedTableValues.getList());
        detailedTableValues.reset();
        // заносим данные в обычную таблицу
        RegularTableHelper regularTableHelper = new RegularTableHelper(hibernateUtil);
        regularTableHelper.addTableList(regularTableValues.getList());
        regularTableValues.reset();
        // заносим данные в сокращенную таблицу
        AbbreviatedTableHelper abbreviatedTableHelper = new AbbreviatedTableHelper(hibernateUtil);
        abbreviatedTableHelper.addTableList(abbreviatedTableValues.getList());
        abbreviatedTableValues.reset();
    }


    private void createThread() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (boundaryValues.getMinX() == 0) {
                    boundaryValues.setMinX(new Date().getTime());
                    GraphUtils.setAxisSettings(xAxis,boundaryValues.getMinX(),boundaryValues.getMaxX());
                    GraphUtils.setAxisSettings(yAxis,boundaryValues.getMinY(),boundaryValues.getMaxY());
                }


                int counter = 0;


                int pushPoint = SettingsData.getInstance().getRotationTime() * SettingsData.getInstance().getFps() / 2000; // коэффициент используемый для выгрузки данных в таблицы
                MedianOfList regularTableMedian = new MedianOfList();
                MedianOfList abbreviatedTableMedian = new MedianOfList();


                while (true) {
                    Values nextValue = null;
                    try {
                        nextValue = values.take();
                        if (nextValue == null) continue;
                        counter++;

                        //changeBoundaries(nextValue.getTimestamp().getTime(), nextValue.getStressThickness());
                        Image image = nextValue.getImage();
                        regularTableMedian.save(nextValue);
                        DetailedTable dtValues = new DetailedTable(nextValue);
                        detailedTableValues.addValue(dtValues);
                        System.out.println(counter);

                        if (counter % 1000 == 0) {
                            addDataToTable();
                        }


                        if (counter % pushPoint == 0) { // вычисляем медиану выборки для обычной таблицы (учитывая скорость вращеняи подложки и частоту камеры) и выводим ее на график
                            Values regularTableValue = regularTableMedian.getMedianValueAndClear();
                            abbreviatedTableMedian.save(regularTableValue);
                            System.out.println("размер -- " + abbreviatedTableMedian.getList());
                            RegularTable rtValue = new RegularTable(regularTableValue);
                            regularTableValues.addValue(rtValue);
                            Number x = regularTableValue.getTimestamp().getTime();
                            Number y = regularTableValue.getMeasuredValue(SettingsData.getInstance().getType());
                            GraphUtils.changeBoundaries(regularTableValue.getTimestamp().getTime(),regularTableValue.getMeasuredValue(SettingsData.getInstance().getType()),xAxis,yAxis,boundaryValues,autoRangingFlag);
                            graphValuesFromRegularTable.addData(regularTableValue.getTimestamp().getTime(), regularTableValue.getMeasuredValue(SettingsData.getInstance().getType()));
                            System.out.println(x + "   " + regularTableValue.getTimestamp().toString());
                            Platform.runLater(() -> series.getData().add(new XYChart.Data<>(x, y)));
                            if (counter % (pushPoint * 20) != 0) {

                            }
                        }


                        if ((counter % (pushPoint * 12) == 0) && (abbreviatedTableMedian.getList().size() > 0)) {
                            System.out.println(abbreviatedTableMedian.getList().size() + " -----РАЗМЕР");
                            Values abbreviatedTableValue = abbreviatedTableMedian.getMedianValueAndClear();
                            AbbreviatedTable atValue = new AbbreviatedTable(abbreviatedTableValue);
                            graphValuesFromAbbreviatedTable.addData(abbreviatedTableValue.getTimestamp().getTime(), abbreviatedTableValue.getMeasuredValue(SettingsData.getInstance().getType()));
                            abbreviatedTableValues.addValue(atValue);
                        }

                        if (counter % (pushPoint * 1000) == 0 && counter != 0) {
                            Platform.runLater(() -> {
                                series.getData().clear();
                                ObservableList<XYChart.Data<Number, Number>> clone = graphValuesFromAbbreviatedTable.clone();
                                series.getData().addAll(clone);
                            });
                        }
                        Platform.runLater(() -> imageView.setImage(image));
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
    }

    @FXML
    public void stop() {
        shutdown();
    }

    public void shutdown() {
        if (thread != null) {
            d0 = modelLayer.getD0();
            modelLayer.stop();
            thread.interrupt();
        }
    }

    @FXML
    public void Edit() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("SQLite", "*.db"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            file.createNewFile();
            System.out.println(file.getAbsolutePath());
            hibernateUtil = new HibernateUtilForSaving(file.getAbsolutePath());
            chart.getData().clear();
        }

    }

    @FXML
    public void openDB() {
        chart.getData().clear();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Opening DB");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("SQLite", "*.db"));
        File file = fileChooser.showOpenDialog(null);
        if (file == null)
            return;
        chart.getData().clear();
        hibernateUtil = new HibernateUtilForOpening(file.getAbsolutePath());
        RegularTableHelper regularTableHelper = new RegularTableHelper(hibernateUtil);
        List<BaseTable> data = regularTableHelper.getTable();
        GraphUtils.InitialGraph(SettingsData.getInstance().getType(), chart, xAxis, yAxis, series);
       // GraphsSettings.DistanceGraph(chart, xAxis, yAxis, series);
        chart.getData().clear();
        chart.getData().add(series);
        ObservableList<XYChart.Data<Number, Number>> result = FXCollections.observableArrayList();
        for (BaseTable values :
                data) {
            graphValuesFromRegularTable.addData(values.getTimestamp(), values.getMeasuredValue(SettingsData.getInstance().getType()));
        }
        boundaryValues = new BoundaryValues(graphValuesFromRegularTable.getMinX(),graphValuesFromRegularTable.getMinY(),graphValuesFromRegularTable.getMaxX(),graphValuesFromRegularTable.getMaxY());
      /*
        maxX = graphValuesFromRegularTable.getMaxX();
        minX = graphValuesFromRegularTable.getMinX();
        maxY = graphValuesFromRegularTable.getMaxY();
        minY = graphValuesFromRegularTable.getMinY();
       */
        series.getData().addAll(graphValuesFromRegularTable.clone());
        GraphUtils.setBoundaries(xAxis, yAxis, boundaryValues.getMinX(), boundaryValues.getMaxX(), boundaryValues.getMinY(), boundaryValues.getMaxY());
    }

    @FXML
    public void SaveAsDetailedTable() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Saving to file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT", "*.txt"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            file.createNewFile();
            addDataToTable();
            DetailedTableHelper detailedTableHelper = new DetailedTableHelper(hibernateUtil);
            detailedTableHelper.tableToTxt(file.getAbsolutePath());
        }
    }

    @FXML
    public void SaveAsRegularTable() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Saving to file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT", "*.txt"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            file.createNewFile();
            addDataToTable();
            RegularTableHelper detailedTableHelper = new RegularTableHelper(hibernateUtil);
            detailedTableHelper.tableToTxt(file.getAbsolutePath());
        }
    }

    @FXML
    public void SaveAsAbbreviatedTable() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Saving to file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT", "*.txt"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            file.createNewFile();
            addDataToTable();
            AbbreviatedTableHelper abbreviatedTableHelper = new AbbreviatedTableHelper(hibernateUtil);
            abbreviatedTableHelper.tableToTxt(file.getAbsolutePath());
        }
    }

}
