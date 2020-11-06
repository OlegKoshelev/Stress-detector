package sample.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

public class GraphValuesFromAbbreviatedTable {
    private ObservableList<XYChart.Data<Number,Number>> data;

    public GraphValuesFromAbbreviatedTable() {
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


}
