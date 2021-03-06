package sample.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.util.Collections;
import java.util.Comparator;

public class GraphValuesFromTable {
    private ObservableList<XYChart.Data<Number,Number>> data;

    public GraphValuesFromTable() {
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

    public long getMaxX(){
        ObservableList<XYChart.Data<Number,Number>> cloneData = clone();
        sortXData(cloneData);
        return cloneData.get(cloneData.size()-1).getXValue().longValue();
    }

    public long getMinX(){
        ObservableList<XYChart.Data<Number,Number>> cloneData = clone();
        sortXData(cloneData);
        return cloneData.get(0).getXValue().longValue();
    }
    public double getMaxY(){
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
                if ((o1.getXValue().longValue() - o2.getXValue().longValue()) < 0)
                    return -1;
                if ((o1.getXValue().longValue() - o2.getXValue().longValue()) > 0)
                    return 1;
                return 0;
            }
        });
    }

    private void sortYData(ObservableList<XYChart.Data<Number,Number>> data){
        Collections.sort(data, new Comparator<XYChart.Data<Number, Number>>() {
            @Override
            public int compare(XYChart.Data<Number, Number> o1, XYChart.Data<Number, Number> o2) {
                if ((o1.getYValue().doubleValue() - o2.getYValue().doubleValue()) < 0)
                    return -1;
                if ((o1.getYValue().doubleValue() - o2.getYValue().doubleValue()) > 0)
                    return 1;
                return 0;
            }
        });
    }


}
