package sample.InSituStress.Controller;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class DBValues {
    @Id
    @Column(name = "DATE")
    private Date timestamp;
    @Column(name = "Distance")
    private double distance;
    @Column(name = "StressThickness")
    private double stressThickness;
    @Column(name ="Curvature")
    private double curvature;

    public DBValues() {
    }

    public DBValues( Date timestamp, double distance, double stressThickness, double curvature) {
        this.timestamp = timestamp;
        this.distance = distance;
        this.stressThickness = stressThickness;
        this.curvature = curvature;
    }



    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
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
