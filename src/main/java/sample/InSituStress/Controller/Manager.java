package sample.InSituStress.Controller;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import sample.InSituStress.Model.ModelLayer;
import DataGetting.Values;
import sample.InSituStress.Model.ValuesLayer;
import sample.InSituStress.View.ConsoleView;
import sample.InSituStress.View.GraphView;
import sample.InSituStress.View.View;

import java.util.concurrent.BlockingQueue;

public class Manager {

    private   ModelLayer modelLayer;
    private  View view;
    private XYChart.Series<Number, Number> series;
    LineChart<Number, Number> chart;
    NumberAxis xAxis;
    NumberAxis yAxis;


    public Manager() throws Exception {
        modelLayer = new ValuesLayer(6);
        view = new ConsoleView();
    }


    public Manager(LineChart<Number, Number> chart, NumberAxis xAxis, NumberAxis yAxis) throws Exception {
        this.chart = chart;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        modelLayer = new ValuesLayer(6);
        view = new GraphView(chart,xAxis,yAxis);
    }

    public void execute() throws InterruptedException {
        BlockingQueue<Values> values = modelLayer.getValuesQueue();
        view.showValuesQueue(values);
    }

    public  void stop(){
        view.stop();
        modelLayer.stop();

    }
}
