package sample.DataBase.Entities;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class RegularTable extends BaseTable {
    public RegularTable() {
    }

    public RegularTable(Date timestamp, double distance, double stressThickness, double curvature) {
        super(timestamp, distance, stressThickness, curvature);
    }
}
