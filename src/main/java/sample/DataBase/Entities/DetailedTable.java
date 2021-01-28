package sample.DataBase.Entities;

import sample.DataGetting.Values;

import javax.persistence.Entity;


@Entity
public class DetailedTable extends BaseTable {
    public DetailedTable() {
    }

    public DetailedTable(Values values) {
        super(values);
    }

    public DetailedTable(long timestamp) {
        super(timestamp);
    }
}
