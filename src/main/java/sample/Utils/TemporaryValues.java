package sample.Utils;

import sample.DataBase.Entities.BaseTable;

import java.util.ArrayList;
import java.util.List;

public class TemporaryValues <Type extends BaseTable> {
    private List<Type> list = new ArrayList<>();

    public TemporaryValues() {
    }

    public synchronized void addValue(Type table){
        list.add(table);
    }

    public synchronized List<Type> getList() {
        return list;
    }

    public synchronized void  reset(){
        list.clear();
    }

    public int size(){
        return list.size();
    }
}
