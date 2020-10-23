package sample.DataBase.Entities;

import sample.DataGetting.Values;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public abstract class BaseTable {
    @Id
    @Column(name = "DATE")
    private long timestamp;
    @Column(name = "Distance")
    private double distance;
    @Column(name = "StressThickness")
    private double stressThickness;
    @Column(name ="Curvature")
    private double curvature;

    public BaseTable() {
    }

    public BaseTable(Values values) {
        this.timestamp = values.getTimestamp().getTime();
        this.distance = values.getDistance();
        this.stressThickness = values.getStressThickness();
        this.curvature = values.getCurvature();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getStressThickness() {
        return stressThickness;
    }

    public void setStressThickness(double stressThickness) {
        this.stressThickness = stressThickness;
    }

    public double getCurvature() {
        return curvature;
    }

    public void setCurvature(double curvature) {
        this.curvature = curvature;
    }
}
