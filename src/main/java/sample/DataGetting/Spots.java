package sample.DataGetting;

import org.opencv.core.Mat;

import java.util.Date;

public class Spots {
    private Mat img;
    private Date date;

    public Spots(Mat img, Date date) {
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
