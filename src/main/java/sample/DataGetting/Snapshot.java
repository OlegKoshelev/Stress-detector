package sample.DataGetting;

import org.opencv.core.Mat;

import java.util.Date;

public class Snapshot {
    private Mat img;
    private Date date;

    public Snapshot(Mat img, Date date) {
        this.img = img;
        this.date = date;
    }

    public Mat getImg() {
        return img;
    }

    public Date getDate() {
        return date;
    }
}
