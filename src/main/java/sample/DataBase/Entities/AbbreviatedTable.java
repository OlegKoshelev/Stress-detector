package sample.DataBase.Entities;


import javax.persistence.Entity;
import java.util.Date;

@Entity
public class AbbreviatedTable extends BaseTable {
    public AbbreviatedTable() {
    }

    public AbbreviatedTable(Date timestamp, double distance, double stressThickness, double curvature) {
        super(timestamp, distance, stressThickness, curvature);
    }
}
