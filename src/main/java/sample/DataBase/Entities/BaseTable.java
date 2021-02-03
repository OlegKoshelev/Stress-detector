package sample.DataBase.Entities;

import sample.DataGetting.Values;
import sample.InitialDataSetting.Graph.GraphType;

import javax.persistence.*;
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

    public BaseTable(long timestamp) {
        this.timestamp = timestamp;
    }

    public BaseTable(Values values) {
        this.timestamp = values.getTimestamp().getTime();
        this.distance = values.getDistance();
        this.stressThickness = values.getStressThickness();
        this.curvature = values.getCurvature();
    }

    public double getMeasuredValue (GraphType graphType) {
        switch (graphType){
            case StressThickness:
                return getStressThickness();
            case Curvature:
                return getCurvature();
            case Distance:
                return getDistance();
            default:
                return 0;
        }
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
