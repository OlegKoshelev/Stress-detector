package sample.Controllers;

import javafx.collections.ObservableList;
import sample.DataBase.AbbreviatedTableHelper;
import sample.DataBase.DetailedTableHelper;
import sample.DataBase.Entities.AbbreviatedTable;
import sample.DataBase.Entities.RegularTable;
import sample.DataBase.Entities.DetailedTable;
import sample.DataBase.HibernateUtil;
import sample.DataBase.RegularTableHelper;
import sample.DataSaving.SettingsData;
import sample.DataSaving.SettingsTransfer;
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
import sample.DataGetting.ModelLayer;
import sample.DataGetting.ValuesLayer;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;

public class MainController implements Initializable {


    @FXML
    private Stage settings;
    private BlockingQueue<Values> values;
    private ModelLayer modelLayer;
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

    public void setValues(BlockingQueue<Values> values) {
        this.values = values;
    }

    private GraphValuesFromAbbreviatedTable graphValuesFromAbbreviatedTable = new GraphValuesFromAbbreviatedTable();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chart.setTitle("Graph");
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
            chart.getData().add(series);
        }
        modelLayer = new ValuesLayer(2);
        values = modelLayer.getValuesQueue();
        createThread();
        thread.start();
    }

    private void createThread() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Date date = new Date();
                int counter = 0;
                TemporaryValues detailedTableValues = new TemporaryValues();
                TemporaryValues regularTableValues = new TemporaryValues();
                TemporaryValues abbreviatedTableValues = new TemporaryValues();

                int pushPoint = SettingsData.getInstance().getRotationTime() * SettingsData.getInstance().getFps() / 2000; // коэффициент используемый для выгрузки данных в таблицы
                MedianOfList regularTableMedian = new MedianOfList();
                MedianOfList abbreviatedTableMedian = new MedianOfList();
                while (true) {
                    Values nextValue = null;
                    try {
                        nextValue = values.take();
                        if (nextValue == null) continue;
                        counter++;

                        Image image = nextValue.getImage();
                        regularTableMedian.save(nextValue);
                        DetailedTable dtValues = new DetailedTable(nextValue);
                        detailedTableValues.addValue(dtValues);
                        System.out.println(counter);

                        if (counter % 1000 == 0) {
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

                        if (counter % 1100 == 0){
                            DetailedTableHelper detailedTableHelper1 = new DetailedTableHelper(hibernateUtil);
                            System.out.println(detailedTableHelper1.getInitialId()+ " -------- первая строчка");
                            System.out.println(detailedTableHelper1.getSecondId()+ " -------- Вторая строчка");
                            System.out.println(detailedTableHelper1.getIncrement() + " -------- приращение");
                        }

                        if (counter % pushPoint == 0) { // вычисляем медиану выборки для обычной таблицы (учитывая скорость вращеняи подложки и частоту камеры) и выводим ее на график
                            Values regularTableValue = regularTableMedian.getMedianValueAndClear();
                            abbreviatedTableMedian.save(regularTableValue);
                            System.out.println("размер -- " + abbreviatedTableMedian.getList());
                            RegularTable rtValue = new RegularTable(regularTableValue);
                            regularTableValues.addValue(rtValue);
                            Number x = regularTableValue.getTimestamp().getTime();
                            Number y = regularTableValue.getDistance();
                            System.out.println(x + "   " + regularTableValue.getTimestamp().toString());
                            if (counter % (pushPoint * 20) != 0 ) {
                                Platform.runLater(() -> series.getData().add(new XYChart.Data<>(x, y)));
                            }
                        }


                        if ((counter % (pushPoint * 12) == 0) && (abbreviatedTableMedian.getList().size() > 0)) {
                            System.out.println(abbreviatedTableMedian.getList().size() + " -----РАЗМЕР");
                            Values abbreviatedTableValue = abbreviatedTableMedian.getMedianValueAndClear();
                            AbbreviatedTable atValue = new AbbreviatedTable(abbreviatedTableValue);
                            graphValuesFromAbbreviatedTable.addData(abbreviatedTableValue.getTimestamp().getTime(),abbreviatedTableValue.getDistance());
                            abbreviatedTableValues.addValue(atValue);
                        }

                        if (counter % (pushPoint * 20) == 0 && counter !=0) {
                            Platform.runLater(() -> {
                                series.getData().clear();
                                ObservableList<XYChart.Data<Number,Number>> clone = graphValuesFromAbbreviatedTable.clone();
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
            modelLayer.stop();
            thread.interrupt();
        }
    }


    @FXML
    public void Edit() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("SQLite", "*.db"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("DOC", "*.doc"));
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

            hibernateUtil = new HibernateUtil(file.getAbsolutePath());


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

}
