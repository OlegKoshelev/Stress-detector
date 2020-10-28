package sample.DataBase.Entities;


import sample.DataGetting.Values;

import javax.persistence.Entity;

@Entity
public class AbbreviatedTable extends BaseTable {
    public AbbreviatedTable() {
    }

    public AbbreviatedTable(Values values) {
        super(values);
    }

    public AbbreviatedTable(long timestamp) {
        super(timestamp);
    }
}
