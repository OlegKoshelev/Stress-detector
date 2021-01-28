package sample.Utils;

import sample.DataBase.Entities.BaseTable;

import java.util.ArrayList;
import java.util.List;

public class TemporaryValues {
    private List<BaseTable> list = new ArrayList<>();

    public TemporaryValues() {
    }

    public void addValue(BaseTable table){
        list.add(table);
    }

    public List<BaseTable> getList() {
        return list;
    }

    public void  reset(){
        list.clear();
    }

    public int size(){
        return list.size();
    }
}
