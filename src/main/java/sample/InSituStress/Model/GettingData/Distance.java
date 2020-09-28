package sample.InSituStress.Model.GettingData;

import org.opencv.core.Mat;

import java.util.Date;

public class Distance {
    private double distance;
    private Date timestamp;
    private Mat img;

    public Distance(double distance, Date timestamp, Mat img) {
        this.distance = distance;
        this.timestamp = timestamp;
        this.img = img;
    }

    public double getDistance(){
        return distance;
    }

    public Date getTimestamp(){
        return timestamp;
    }

    public Mat getImg() {
        return img;
    }
}
