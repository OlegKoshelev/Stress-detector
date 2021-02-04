package sample.Controllers;

import de.gsi.chart.axes.spi.DefaultNumericAxis;
import de.gsi.dataset.spi.DefaultErrorDataSet;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import org.apache.log4j.Logger;
import sample.AdditionalUtils.GraphUtils;
import sample.DataBase.*;
import sample.DataBase.Entities.AveragingTable;
import sample.DataBase.Entities.DetailedTable;
import sample.DataGetting.Calculator;
import sample.DataSaving.SettingsSaving.SettingsData;
import sample.DataSaving.SettingsSaving.SettingsTransfer;
import sample.Utils.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import sample.DataGetting.Values;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class MainController implements Initializable {

    @FXML
    CheckMenuItem autoRanging;
    private double d0 = 0;
    @FXML
    private StackPane stackPane;
    private BlockingQueue<Values> values;
    private Calculator calculator;
    @FXML
    private ImageView imageView;
    private  de.gsi.chart.XYChart chart = new de.gsi.chart.XYChart(new DefaultNumericAxis(), new DefaultNumericAxis()) ;
    private  DefaultErrorDataSet dataSet = new DefaultErrorDataSet("distance");
    private TemporaryValues<DetailedTable> detailedTableValues ;
    private TemporaryValues<AveragingTable> averageTableValues ;
    private HibernateUtil hibernateUtil;



    public void setValues(BlockingQueue<Values> values) {
        this.values = values;
    }



    private final Logger logger = Logger.getLogger(MainController.class);



   @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            SettingsTransfer.getFullSettingsFromFile();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        stackPane.getChildren().add(chart);
        GraphUtils.initialGraph(chart, dataSet);

    }
    public void graphRepaint() {
        GraphUtils.repaint(chart);
    }

    @FXML
    public void showWorkWindow() throws IOException {
        if (hibernateUtil == null) {
            showCheckingDBWindow();
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/WorkWindow.fxml"));
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Work window");
        primaryStage.setScene(new Scene(root));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();

    }

    @FXML
    public void showStaticSettings() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/StaticSettings.fxml"));
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Static settings");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    @FXML
    public void showSettings() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        Parent root = loader.load();
        SettingsController controller = loader.getController();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Settings");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
        primaryStage.setOnHidden(event -> {
            graphRepaint();
            controller.shutdown();
        });
    }



    public void showCheckingDBWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CheckingDB.fxml"));
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Checking DB");
        primaryStage.setScene(new Scene(root));
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    @FXML
    public void print() throws Exception {
        if (hibernateUtil == null) {
            showCheckingDBWindow();
            return;
        }
        if (d0 == 0){
          d0 =  new InitialDistanceHelper(hibernateUtil).getDistance();
        }
        detailedTableValues = new TemporaryValues<DetailedTable>();
        averageTableValues = new TemporaryValues<AveragingTable>();
        calculator = new Calculator(dataSet,imageView, d0,hibernateUtil,detailedTableValues,averageTableValues);
        values = calculator.getValuesQueue();
        calculator.start();
//        new InitialDistanceHelper(hibernateUtil).saveDistance(d0);
//        logger.debug("d0 is --- " + d0);

    }



    public void addDataToTable() {
        // заносим данные в подробную таблицу
        if (detailedTableValues != null && detailedTableValues.getList().size() > 0) {
            TableHelper<DetailedTable> detailedTableHelper = new TableHelper<>(hibernateUtil,DetailedTable.class);
            detailedTableHelper.addTableList(detailedTableValues.getList());
            detailedTableValues.reset();
        }

        // заносим данные в усредненную таблицу
        if (averageTableValues != null && averageTableValues.getList().size() > 0) {
            TableHelper<AveragingTable> averageTableHelper = new TableHelper<>(hibernateUtil,AveragingTable.class);
            averageTableHelper.addTableList(averageTableValues.getList());
            averageTableValues.reset();
        }

    }


    @FXML
    public void stop() {
        shutdown();
        addDataToTable();
    }

    public void shutdown() {
        if (calculator != null)
            calculator.stop();
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
            dataSet.clearData();
            SettingsData.getInstance().setPathToDB(file.getAbsolutePath());
        }

    }

    @FXML
    public void openDB() {
        dataSet.clearData();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Opening DB");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("SQLite", "*.db"));
        File file = fileChooser.showOpenDialog(null);
        if (file == null)
            return;
        SettingsData.getInstance().setPathToDB(file.getAbsolutePath());

        dataSet.clearData();
        hibernateUtil = new HibernateUtilForOpening(SettingsData.getInstance().getPathToDB());
        TableHelper<AveragingTable> averagingTableHelper = new TableHelper<>(hibernateUtil,AveragingTable.class);
        List<AveragingTable> data = averagingTableHelper.getTable();
        d0 = new InitialDistanceHelper(hibernateUtil).getDistance();
        for (AveragingTable avTable : data) {
            dataSet.add(avTable.getTimestamp(),avTable.getMeasuredValue(SettingsData.getInstance().getType()));
        }
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
            TableHelper<DetailedTable> detailedTableHelper = new TableHelper<>(hibernateUtil,DetailedTable.class);
            detailedTableHelper.tableToTxt(file.getAbsolutePath(), d0);
        }
    }

    @FXML
    public void SaveAsAveragingTable() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Saving to file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT", "*.txt"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            file.createNewFile();
            addDataToTable();
            TableHelper<AveragingTable> detailedTableHelper = new TableHelper<>(hibernateUtil,AveragingTable.class);
            detailedTableHelper.tableToTxt(file.getAbsolutePath(),d0);
        }
    }

}
