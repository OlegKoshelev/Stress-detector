package sample.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.util.Collections;
import java.util.Comparator;

public class graphValuesFromTable {
    private ObservableList<XYChart.Data<Number,Number>> data;

    public graphValuesFromTable() {
      data = FXCollections.observableArrayList();
    }

    public void addData (Long x, Double y){
        data.add(new XYChart.Data<>(x, y));
    }

    public ObservableList<XYChart.Data<Number,Number>> getData (){
        return data;
    }

    public ObservableList<XYChart.Data<Number,Number>> clone(){
        ObservableList<XYChart.Data<Number,Number>> result = FXCollections.observableArrayList();
        for (XYChart.Data<Number, Number> values :
                data) {
            result.add(new XYChart.Data<>(values.getXValue(), values.getYValue()));
        }
        return result;
    }

    public long getMAxX(){
        ObservableList<XYChart.Data<Number,Number>> cloneData = clone();
        sortXData(cloneData);
        return cloneData.get(cloneData.size()-1).getXValue().longValue();
    }

    public long getMinX(){
        ObservableList<XYChart.Data<Number,Number>> cloneData = clone();
        sortXData(cloneData);
        return cloneData.get(0).getXValue().longValue();
    }
    public double getMAxY(){
        ObservableList<XYChart.Data<Number,Number>> cloneData = clone();
        sortYData(cloneData);
        return cloneData.get(cloneData.size()-1).getYValue().doubleValue();
    }

    public double getMinY(){
        ObservableList<XYChart.Data<Number,Number>> cloneData = clone();
        sortYData(cloneData);
        return cloneData.get(0).getYValue().doubleValue();
    }

    private void sortXData(ObservableList<XYChart.Data<Number,Number>> data){
        Collections.sort(data, new Comparator<XYChart.Data<Number, Number>>() {
            @Override
            public int compare(XYChart.Data<Number, Number> o1, XYChart.Data<Number, Number> o2) {
                return(int) (o1.getXValue().longValue() - o2.getXValue().longValue());
            }
        });
    }

    private void sortYData(ObservableList<XYChart.Data<Number,Number>> data){
        Collections.sort(data, new Comparator<XYChart.Data<Number, Number>>() {
            @Override
            public int compare(XYChart.Data<Number, Number> o1, XYChart.Data<Number, Number> o2) {
                return(int) ((o1.getYValue().doubleValue() - o2.getYValue().doubleValue())*100000000);
            }
        });
    }


}
