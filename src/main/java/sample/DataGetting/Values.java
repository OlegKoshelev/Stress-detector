package sample.DataGetting;


import javafx.scene.image.Image;
import sample.InitialDataSetting.Graph.GraphType;

import java.util.Date;

public class Values  implements Comparable<Values>{
    private double stressThickness;
    private double curvature;
    private Date timestamp;
    private double distance;

    public Values(double stressThickness, double curvature, Date timestamp, double distance) {
        this.stressThickness = stressThickness;
        this.curvature = curvature;
        this.timestamp = timestamp;
        this.distance = distance;
    }

    public Values() {
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

    public double getStressThickness() {
        return stressThickness;
    }

    public double getCurvature() {
        return curvature;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public double getDistance() { return distance; }


    public void setStressThickness(double stressThickness) {
        this.stressThickness = stressThickness;
    }

    public void setCurvature(double curvature) {
        this.curvature = curvature;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }


    @Override
    public int compareTo(Values o) {
        return (int) (timestamp.getTime() - o.getTimestamp().getTime());
    }
}
