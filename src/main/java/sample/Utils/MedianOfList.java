package sample.Utils;

import sample.DataGetting.Values;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MedianOfList {

    private List<Values> list = new ArrayList<>(30);

    public void save (Values values ){
        list.add(values);
    }

    public Values getMedianValueAndClear (){
        Collections.sort(list);
        int size = list.size();
        Values result = null;
        if (size == 0){
            return null;
        }
        if (size == 1){
            result = list.get(0);
        }
        if (size == 2){
            result = getAverageMagnitude(list.get(0), list.get(1));
        }
        if (size % 2 == 0){  //для четной выборки
            result =  getAverageMagnitude(list.get(size/2),list.get((size/2)-1));
        }
        else{ // для нечетной выборки
            result = list.get((size-1)/2);
        }
        result.setTimestamp(list.get(list.size()-1).getTimestamp()); // берем самую последнюю дату в выборке
        list.clear();
        return  result;
    }

    private Values getAverageMagnitude (Values v1, Values v2){
        Values result = new Values();
        result.setDistance((v1.getDistance() + v2.getDistance())/2);
        result.setCurvature(v1.getCurvature() + v2.getCurvature());
        result.setStressThickness(v1.getStressThickness() + v2.getStressThickness());
        result.setImg(v1.getImage());
        return result;
    }


}
