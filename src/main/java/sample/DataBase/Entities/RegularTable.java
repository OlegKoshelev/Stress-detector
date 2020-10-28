package sample.DataBase.Entities;

import sample.DataGetting.Values;

import javax.persistence.Entity;

@Entity
public class RegularTable extends BaseTable {
    public RegularTable() {
    }

    public RegularTable(Values values) {
        super(values);
    }

    public RegularTable(long timestamp) {
        super(timestamp);
    }
}
