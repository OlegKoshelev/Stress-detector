package sample.DataBase.Entities;

import sample.DataGetting.Values;

import javax.persistence.Entity;

@Entity
public class AveragingTable extends BaseTable {
    public AveragingTable() {
    }
    public AveragingTable(Values values) {
        super(values);
    }

    public AveragingTable(long timestamp) {
        super(timestamp);
    }
}
