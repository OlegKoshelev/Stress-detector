package sample.InSituStress.Controller;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class DBGraph {
    @Id
    @Column(name = "DATE")
    private Date timestamp;
    @Column(name = "Value")
    private double value;

    public DBGraph() {
    }

    public DBGraph(Date timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
