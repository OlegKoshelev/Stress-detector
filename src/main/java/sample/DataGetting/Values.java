package sample.DataGetting;



import sample.InitialDataSetting.Graph.GraphType;

import java.util.Date;

public class Values  implements Comparable<Values>{
    private double stressThickness;
    private double curvature;
    private Date timestamp;
    private double distance;
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private double size1;
    private double size2;


    public Values(double stressThickness, double curvature, Date timestamp,
                  double distance, double x1, double y1, double x2, double y2,
                  double size1, double size2) {
        this.stressThickness = stressThickness;
        this.curvature = curvature;
        this.timestamp = timestamp;
        this.distance = distance;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.size1 = size1;
        this.size2 = size2;
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

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    public double getSize1() {
        return size1;
    }

    public void setSize1(double size1) {
        this.size1 = size1;
    }

    public double getSize2() {
        return size2;
    }

    public void setSize2(double size2) {
        this.size2 = size2;
    }

    @Override
    public int compareTo(Values o) {
        return (int) (timestamp.getTime() - o.getTimestamp().getTime());
    }
}
