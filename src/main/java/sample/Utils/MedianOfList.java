package sample.Utils;

import sample.DataGetting.Values;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MedianOfList {

    private List<Values> list ;

    public MedianOfList() {
       list = new ArrayList<>(30);
    }

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
        result.setTimestamp(list.get(0).getTimestamp()); // берем самую первую дату в выборке
        for (Values val:list) {
            System.out.println(val.getDistance());
        }
        list.clear();
        return  result;
    }

    private Values getAverageMagnitude (Values v1, Values v2){
        Values result = new Values();
        result.setDistance((v1.getDistance() + v2.getDistance())/2);
        result.setCurvature((v1.getCurvature() + v2.getCurvature())/2);
        result.setStressThickness((v1.getStressThickness() + v2.getStressThickness())/2);
        result.setImg(v1.getImage());
        return result;
    }

    public List<Values> getList() {
        return list;
    }
}
