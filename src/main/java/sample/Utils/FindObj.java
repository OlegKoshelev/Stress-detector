package sample.Utils;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import sample.DataGetting.CvUtils;

import java.util.ArrayList;
import java.util.List;

public class FindObj {

    public static void main(String[] args) {
        System.load("C:\\opencv_3_3\\build\\java\\x64\\opencv_java330.dll");
        Mat img = Imgcodecs.imread("E:\\2.png");
        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        CvUtils.showResult(img);

        Mat hsv = new Mat();
        Imgproc.cvtColor(img, hsv, Imgproc.COLOR_BGR2HSV);
        CvUtils.showResult(hsv);

        Mat img2 = new Mat();
        Core.inRange(hsv, new Scalar(0, 0, 220),
                new Scalar(200, 70, 255), img2);
        CvUtils.showResult(img2);

        //find Contours
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(img2, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

        Point pointOne = new Point();
        Point pointTwo = new Point();
        Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(0).toArray()),pointOne, new float[1]);
        Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(1).toArray()),pointTwo, new float[1]);
        Imgproc.cvtColor(img2,img2, Imgproc.COLOR_BayerGB2BGR);

        Imgproc.circle(img2,pointOne,1, new Scalar(0,0,255), 8);
        Imgproc.circle(img2,pointTwo,1, new Scalar(0,0,255),8);
        CvUtils.showResult(img2);

        System.out.println(pointOne);
        System.out.println(pointTwo);

        System.out.println("cols"+img2.cols());
        System.out.println("rows"+img2.rows());
        img.release(); img2.release();
        System.out.println(String.format("distance: %f", distance(pointOne,pointTwo)));
    }

    public static double distance (Point one, Point two){
        double result;
        result = Math.sqrt(Math.pow(one.x - two.x,2) + Math.pow(one.y - two.y,2));
        return result;
    }
}
