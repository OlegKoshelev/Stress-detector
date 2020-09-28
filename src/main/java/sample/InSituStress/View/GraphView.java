package sample.InSituStress.View;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import sample.InSituStress.Model.GettingData.CvUtils;
import sample.InSituStress.Model.GettingData.Values;

import java.util.concurrent.BlockingQueue;

public class GraphView implements View {
    private boolean stop = false;
    private Thread thread;

    LineChart<Number, Number> chart;
    NumberAxis xAxis;
    NumberAxis yAxis;
    private XYChart.Series<Number, Number> series;

    public GraphView(LineChart<Number, Number> chart, NumberAxis xAxis, NumberAxis yAxis) {
        this.chart = chart;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    @Override
    public void showValuesQueue(BlockingQueue<Values> values) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                chart.setTitle("55555");
                while (!stop) {
                    Values nextValue = null;
                    try {
                        nextValue = values.take();
                        if (nextValue == null) continue;
                        Number x = nextValue.getTimestamp().getTime();
                        Number y = nextValue.getDistance();
              //          series.getData().add(new XYChart.Data<>(x,y));
                        System.out.println(String.format("Time: %s;  StressThickness: %f; Curvature: %f; distance: %f", nextValue.getTimestamp().toString(), nextValue.getStressThickness(), nextValue.getCurvature(),nextValue.getDistance()));
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void stop() {
        thread.interrupt();
    }
}
