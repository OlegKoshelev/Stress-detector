package sample.DataBase.Entities;

import sample.DataGetting.Values;
import sample.InitialDataSetting.Graph.GraphType;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseTable {
    @Id
//    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private long id;
    @Column(name = "DATE")
    private long timestamp;
    @Column(name = "Distance")
    private double distance;
    @Column(name = "StressThickness")
    private double stressThickness;
    @Column(name ="Curvature")
    private double curvature;
    @Column(name = "x_1")
    private double x1;
    @Column(name = "y_1")
    private double y1;
    @Column(name = "x_2")
    private double x2;
    @Column(name = "y_2" )
    private double y2;
    @Column(name = "size_1")
    private double size1;
    @Column(name = "size_2")
    private double size2;

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
        this.x1 = values.getX1();
        this.y1 = values.getY1();
        this.x2 = values.getX2();
        this.y2 = values.getY2();
        this.size1 = values.getSize1();
        this.size2 = values.getSize2();
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

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }

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
}
