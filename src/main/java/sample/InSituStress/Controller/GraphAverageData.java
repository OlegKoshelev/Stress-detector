package sample.InSituStress.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GraphAverageData {
  private static List<Double> averageValue = new ArrayList<>(30);

    public void save (double value){
        averageValue.add(value);
    }
    public double submit (){
        Collections.sort(averageValue);
        int size = averageValue.size();
        double value = 0;
        if (size== 0){
            return 0;
        }
        if (size == 1){
            value = averageValue.get(0);
        }
        if (size == 2)
            value =  (averageValue.get(0) + averageValue.get(1))/2;
        if (size %2 == 0 && size >2 ){
          double firstValue =  averageValue.get(size/2);
          double secondValue = averageValue.get((size/2)-1);
          value =  (firstValue + secondValue)/2;
        }else{
            value = averageValue.get((size-1)/2);
        }
        averageValue.clear();
        return  value;
    }
}
