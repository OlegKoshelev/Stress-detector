package sample.DataBase.Entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
public class DetailedTable extends BaseTable {
    public DetailedTable() {
    }

    public DetailedTable(Date timestamp, double distance, double stressThickness, double curvature) {
        super( timestamp, distance, stressThickness, curvature);
    }
}
