package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import sample.AdditionalUtils.GraphUtils;
import sample.DataBase.*;
import sample.DataBase.Entities.AbbreviatedTable;
import sample.DataBase.Entities.BaseTable;
import sample.DataBase.Entities.RegularTable;
import sample.DataBase.Entities.DetailedTable;
import sample.DataSaving.SettingsSaving.SettingsData;
import sample.DataSaving.SettingsSaving.SettingsTransfer;
import sample.Graph.AxisBoundaries;
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
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;

public class MainController implements Initializable {
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

    private HibernateUtil hibernateUtil;

    private double maxY = 0;

    private double minY = 0;

    private long maxX = 0;

    private long minX = 0;

    private long stratTime = new Date().getTime();

    private TextField textField = null;

    private TemporaryValues detailedTableValues = new TemporaryValues();
    private TemporaryValues regularTableValues = new TemporaryValues();
    private TemporaryValues abbreviatedTableValues = new TemporaryValues();

    @FXML
    private Button run;

    public void setValues(BlockingQueue<Values> values) {
        this.values = values;
    }

    private GraphValuesFromAbbreviatedTable graphValuesFromAbbreviatedTable = new GraphValuesFromAbbreviatedTable();
    private AxisBoundaries axisBoundary = null;


    @FXML
    public void singleMouseClickAndPushingEnter(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {// сброс отображения полей ввода диапазонов осей
            if (event.getClickCount() == 1) {
                if (stackPane.getChildren().size() == 2) {
                    stackPane.getChildren().remove(1);
                    switch (axisBoundary) {
                        case MinY:
                            System.out.println(textField.getText());
                            yAxis.setLowerBound(Double.parseDouble(textField.getText()));
                            yAxis.setTickUnit((yAxis.getUpperBound() - yAxis.getLowerBound())/5);
                            break;
                        case MaxY:
                            yAxis.setUpperBound(Double.parseDouble(textField.getText()));
                            yAxis.setTickUnit((yAxis.getUpperBound() - yAxis.getLowerBound())/5);
                            break;
                        case MinX:
                            break;
                        case MaxX:
                            break;
                    }
                    textField = null;
                }
            }
        }
    }
    @FXML
    public void doubleMouseClick(MouseEvent event){
        if (event.getButton().equals(MouseButton.PRIMARY)) { // отобразить поле ввода для изменения диапазона оси
            if (event.getClickCount() == 2){
                if ((stackPane.getChildren().size() == 1)) {
                    textField = new TextField("");
                     axisBoundary = GraphUtils.setBoundaryValue(event.getX(),event.getY(),(NumberAxis) event.getSource(),textField,stackPane);
                     textField.addEventHandler(KeyEvent.KEY_PRESSED, event1 -> {
                         if (event1.getCode() == KeyCode.ENTER) {
                             if (stackPane.getChildren().size() == 2) {
                                 stackPane.getChildren().remove(1);
                                 switch (axisBoundary) {
                                     case MinY:
                                         System.out.println(textField.getText());
                                         yAxis.setLowerBound(Double.parseDouble(textField.getText()));
                                         yAxis.setTickUnit((yAxis.getUpperBound() - yAxis.getLowerBound())/5);
                                         break;
                                     case MaxY:
                                         yAxis.setUpperBound(Double.parseDouble(textField.getText()));
                                         yAxis.setTickUnit((yAxis.getUpperBound() - yAxis.getLowerBound())/5);
                                         break;
                                     case MinX:
                                         break;
                                     case MaxX:
                                         break;
                                 }
                                 textField = null;
                             }

                         }
                     });
/*
                    textField.setFont(new Font("SansSerif", 12));
                    textField.setMaxSize(45, 5);
                    AxisBoundaries axisBoundary = GraphUtils.getBoundary(event.getX(), event.getY(),(NumberAxis) event.getSource());
                    GraphUtils.addToStackPain(axisBoundary,textField, stackPane);
 */
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chart.setTitle("Graph");
        yAxis.setAutoRanging(false);
        xAxis.setAutoRanging(false);
        xAxis.setAnimated(true);
        yAxis.setAnimated(true);



        try {
            SettingsTransfer.readFromSettingsFile(SettingsData.getInstance());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        primaryStage.setOnCloseRequest(event -> {
            controller.shutdown();
        });
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
            GraphsSettings.DistanceGraph(chart, xAxis, yAxis, series);
            chart.getData().clear();
            series = new XYChart.Series<>();
            chart.getData().add(series);
        }
        modelLayer = new ValuesLayer(2, d0);
        values = modelLayer.getValuesQueue();
        createThread();
        thread.start();
    }


    private void changeBoundaries(long xValue, double yValue) {
        if (xValue > maxX - 50000) {
            maxX = xValue + 100000;
            xAxis.setUpperBound(maxX);
            double v = (double) (maxX - stratTime) / 5;
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
                xAxis.setLowerBound(stratTime);
                xAxis.setUpperBound(maxX);

                yAxis.setLowerBound(minY);
                yAxis.setUpperBound(maxY);

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

                        changeBoundaries(nextValue.getTimestamp().getTime(), nextValue.getStressThickness());
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
                            Number y = regularTableValue.getStressThickness();
                            System.out.println(x + "   " + regularTableValue.getTimestamp().toString());
                            if (counter % (pushPoint * 20) != 0) {
                                Platform.runLater(() -> series.getData().add(new XYChart.Data<>(x, y)));
                            }
                        }


                        if ((counter % (pushPoint * 12) == 0) && (abbreviatedTableMedian.getList().size() > 0)) {
                            System.out.println(abbreviatedTableMedian.getList().size() + " -----РАЗМЕР");
                            Values abbreviatedTableValue = abbreviatedTableMedian.getMedianValueAndClear();
                            AbbreviatedTable atValue = new AbbreviatedTable(abbreviatedTableValue);
                            graphValuesFromAbbreviatedTable.addData(abbreviatedTableValue.getTimestamp().getTime(), abbreviatedTableValue.getStressThickness());
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

           /*
                       String shortPath = file.getParentFile().getPath() + "\\";
            System.out.println(shortPath);
            String name = file.getName();
            String[] nameParts = name.split("\\.");
            String shortName = nameParts[0];
            System.out.println(shortName);
            File txtFile = new File(String.format("%s%s.txt", shortPath, shortName));
            txtFile.createNewFile();
            */

            hibernateUtil = new HibernateUtilForSaving(file.getAbsolutePath());


            /*
                      String url = "jdbc:sqlite:" + file.getAbsolutePath();
            System.out.println(url);
            dBConfiguration = new Configuration()
                    .setProperty("hibernate.dialect", "org.hibernate.dialect.SQLiteDialect")
                    .setProperty("hibernate.connection.url", url)
                    .setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC")
                    .setProperty("hibernate.current_session_context_class", "thread")
                    .setProperty("hibernate.show_sql", "true")
                    .setProperty("hibernate.hbm2ddl.auto", "update")
                    .configure();
             */
        }

    }

    @FXML
    public void openDB() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Opening DB");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("SQLite", "*.db"));
        File file = fileChooser.showOpenDialog(null);
        hibernateUtil = new HibernateUtilForOpening(file.getAbsolutePath());
        RegularTableHelper regularTableHelper = new RegularTableHelper(hibernateUtil);
        List<BaseTable> data = regularTableHelper.getTable();
        GraphsSettings.DistanceGraph(chart, xAxis, yAxis, series);
        chart.getData().clear();
        chart.getData().add(series);
        ObservableList<XYChart.Data<Number, Number>> result = FXCollections.observableArrayList();
        for (BaseTable values :
                data) {
            result.add(new XYChart.Data<>(values.getTimestamp(), values.getStressThickness()));
        }
        double max = 0;
        double min = 0;
        for (BaseTable values :
                data) {
            if (max < values.getStressThickness())
                max = values.getStressThickness();
            if (min > values.getStressThickness())
                min = values.getStressThickness();
        }
        System.out.println(max);
        System.out.println(min);

        series.getData().addAll(result);


        System.out.println(new Date(data.get(0).getTimestamp()));
        System.out.println(new Date(data.get(data.size() - 1).getTimestamp()));
        xAxis.setLowerBound(data.get(0).getTimestamp());
        xAxis.setUpperBound(data.get(data.size() - 1).getTimestamp());
        xAxis.setTickUnit((double) (data.get(data.size() - 1).getTimestamp() - data.get(0).getTimestamp()) / 5);
        yAxis.setLowerBound(min);
        yAxis.setUpperBound(max);
        yAxis.setTickUnit((max - min) / 5);
        yAxis.setAutoRanging(false);
        xAxis.setAutoRanging(false);
        xAxis.setAnimated(true);
        yAxis.setAnimated(true);
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
