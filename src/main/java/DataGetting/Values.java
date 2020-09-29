package DataGetting;


import javafx.scene.image.Image;
import java.util.Date;

public class Values {
    private double stressThickness;
    private double curvature;
    private Date timestamp;
    private double distance;
    private Image img;

    public Values(double stressThickness, double curvature, Date timestamp, double distance, Image img) {
        this.stressThickness = stressThickness;
        this.curvature = curvature;
        this.timestamp = timestamp;
        this.distance = distance;
        this.img = img;
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

    public Image getImage() { return img; }
}
