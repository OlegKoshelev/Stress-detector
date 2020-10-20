package sample.Controllers;

import sample.DataBase.DetailedTableHelper;
import sample.DataBase.Entities.RegularTable;
import sample.DataBase.Entities.DetailedTable;
import sample.DataBase.HibernateUtil;
import sample.DataSaving.SettingsData;
import sample.DataSaving.SettingsTransfer;
import sample.Utils.GraphAverageData;
import sample.Utils.GraphsSettings;
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

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import sample.DataGetting.Values;
import sample.DataGetting.ModelLayer;
import sample.DataGetting.ValuesLayer;
import sample.Utils.MedianOfList;
import sample.Utils.TemporaryValues;


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
            SettingsController controller = loader.getController();
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Checking DB");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> {
                controller.shutdown();
            });
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
                TemporaryValues AbbreviatedValues = new TemporaryValues();

                int checkPoint = SettingsData.getInstance().getRotationTime() * SettingsData.getInstance().getFps() / 2000;
                // SessionFactory sessionFactory = dBConfiguration.buildSessionFactory();
                // Session session = sessionFactory.openSession();
                //   session.beginTransaction();
                MedianOfList medianOfList = new MedianOfList();
                while (true) {
                    Values nextValue = null;
                    // GraphAverageData graphAverageData = new GraphAverageData();
                    try {
                        nextValue = values.take();
                        if (nextValue == null) continue;
                        counter++;

                        Image image = nextValue.getImage();
                        medianOfList.save(nextValue);
                        //  graphAverageData.save(nextValue.getDistance());
                        DetailedTable dtValues = new DetailedTable(nextValue.getTimestamp(), nextValue.getDistance(), nextValue.getStressThickness(), nextValue.getCurvature());
                        detailedTableValues.addValue(dtValues);
                        //  session.save(dbValues);
                        System.out.println(counter);
                        if (counter % 1000 == 0) {
                            //  session.getTransaction().commit();
                            //  session.beginTransaction();
                            //  series.getData().clear();
                            DetailedTableHelper detailedTableHelper = new DetailedTableHelper(hibernateUtil);
                            detailedTableHelper.addTableList(detailedTableValues.getList());
                            detailedTableValues.reset();
                        }

                        if (counter % checkPoint == 0) {
                            Values RegularTableValues = medianOfList.getMedianValueAndClear();

                            Number x = RegularTableValues.getTimestamp().getTime();
                            Number y = RegularTableValues.getDistance();
                            //  double value = graphAverageData.submit();
                            //  Number y = value;
                            //  session.save(new RegularTable(nextValue.getTimestamp(),value));
                            System.out.println(x + "   " + RegularTableValues.getTimestamp().toString());
                            Platform.runLater(() -> series.getData().add(new XYChart.Data<>(x, y)));
                        }
                        Platform.runLater(() -> imageView.setImage(image));
                        //System.out.println(String.format("Time: %s;  StressThickness: %f; Curvature: %f; distance: %f", nextValue.getTimestamp().toString(), nextValue.getStressThickness(), nextValue.getCurvature(), nextValue.getDistance()));
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                // session.close();
                // sessionFactory.close();
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
