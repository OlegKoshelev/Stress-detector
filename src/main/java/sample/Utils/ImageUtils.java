package sample.Utils;

import javafx.scene.image.Image;
import org.apache.log4j.Logger;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import sample.DataSaving.SettingsSaving.DynamicSettings.CameraCustomizations;
import sample.DataSaving.SettingsSaving.SettingsData;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageUtils {

    public static Logger logger = Logger.getLogger(ImageUtils.class);

    public static Mat matReform(Mat img, CameraCustomizations cameraCustomizations) {
        int hueMax = cameraCustomizations.getHueMax();
        int hueMin = cameraCustomizations.getHueMin();
        int saturationMax = cameraCustomizations.getSaturationMax();
        int saturationMin = cameraCustomizations.getSaturationMin();
        int valueMax = cameraCustomizations.getValueMax();
        int valueMin = cameraCustomizations.getValueMin();
        Mat hsv = new Mat();
        Imgproc.cvtColor(img, hsv, Imgproc.COLOR_BGR2HSV);
        Mat img2 = new Mat();

        Core.inRange(hsv, new Scalar(hueMin, saturationMin, valueMin),
                new Scalar(hueMax, saturationMax, valueMax), img2);

        return img2;
    }

    public static Mat matReform(Mat img) {
        int hueMax = SettingsData.getInstance().getHueMax();
        int hueMin = SettingsData.getInstance().getHueMin();
        int saturationMax = SettingsData.getInstance().getSaturationMax();
        int saturationMin = SettingsData.getInstance().getSaturationMin();
        int valueMax = SettingsData.getInstance().getValueMax();
        int valueMin = SettingsData.getInstance().getValueMin();
        Mat hsv = new Mat();
        Imgproc.cvtColor(img, hsv, Imgproc.COLOR_BGR2HSV);
        Mat img2 = new Mat();

        Core.inRange(hsv, new Scalar(hueMin, saturationMin, valueMin),
                new Scalar(hueMax, saturationMax, valueMax), img2);

        return img2;
    }

    public static Image getPrimaryImage(Mat img) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        return new Image(new ByteArrayInputStream(byteArray));
    }

    public static Image getHsvImage(Mat img, CameraCustomizations cameraCustomizations) {
        Mat img2 = matReform(img, cameraCustomizations);
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img2, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        return new Image(new ByteArrayInputStream(byteArray));
    }

    public static Image getHsvImageWithСenters(Mat img, CameraCustomizations cameraCustomizations, Point [] points, double [] sizes, List<MatOfPoint> contoursList){
        Point pointOne = new Point();
        Point pointTwo = new Point();
        if (img == null)
            return null;
        Mat img2 = matReform(img,cameraCustomizations);
        Imgproc.findContours(img2, contoursList, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
        if (contoursList.size() < 2)
            return null;
        Collections.sort(contoursList, (o1, o2) -> (int) (o2.size().area() - o1.size().area()));

        if (contoursList.size() == 2) {
            sizes[0] = contoursList.get(0).size().area();
            sizes[1] = contoursList.get(1).size().area();
            Imgproc.minEnclosingCircle(new MatOfPoint2f(contoursList.get(0).toArray()), pointOne, new float[1]);
            Imgproc.minEnclosingCircle(new MatOfPoint2f(contoursList.get(1).toArray()), pointTwo, new float[1]);
        } else {
            for(int i = 0; i < contoursList.size() - 1; i++){
                if (contoursList.get(i).size().area() <= contoursList.get(i+1).size().area()*2){
                    sizes[0] = contoursList.get(i).size().area();
                    sizes[1] = contoursList.get(i+1).size().area();
                    Imgproc.minEnclosingCircle(new MatOfPoint2f(contoursList.get(i).toArray()), pointOne, new float[1]);
                    Imgproc.minEnclosingCircle(new MatOfPoint2f(contoursList.get(i+1).toArray()), pointTwo, new float[1]);
                    break;
                }
            }
        }
        Imgproc.cvtColor(img2, img2, Imgproc.COLOR_BayerGB2BGR);
        Imgproc.circle(img2, pointOne, 1, new Scalar(0, 0, 255), 15);
        Imgproc.circle(img2, pointTwo, 1, new Scalar(0, 0, 255), 15);
        points[0] = pointOne;
        points[1] = pointTwo;
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img2, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        return new Image(new ByteArrayInputStream(byteArray));
    }

    public static Image getHsvImageWithСenters(Mat img, Point [] points, double [] sizes){
        Point pointOne = new Point();
        Point pointTwo = new Point();
        if (img == null)
            return null;
        Mat img2 = matReform(img);
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(img2, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
        if (contours.size() < 2)
            return null;
        Collections.sort(contours, (o1, o2) -> (int) (o2.size().area() - o1.size().area()));
        if (contours.size() == 2) {
            sizes[0] = contours.get(0).size().area();
            sizes[1] = contours.get(1).size().area();
            Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(0).toArray()), pointOne, new float[1]);
            Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(1).toArray()), pointTwo, new float[1]);
        } else {
            for(int i = 0; i < contours.size() - 1; i++){
                if (contours.get(i).size().area() <= contours.get(i+1).size().area()*2){
                    sizes[0] = contours.get(i).size().area();
                    sizes[1] = contours.get(i+1).size().area();
                    Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(i).toArray()), pointOne, new float[1]);
                    Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(i+1).toArray()), pointTwo, new float[1]);
                    break;
                }
            }
        }


        Imgproc.cvtColor(img2, img2, Imgproc.COLOR_BayerGB2BGR);
        Imgproc.circle(img2, pointOne, 1, new Scalar(0, 0, 255), 15);
        Imgproc.circle(img2, pointTwo, 1, new Scalar(0, 0, 255), 15);
        points[0] = pointOne;
        points[1] = pointTwo;
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img2, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        return new Image(new ByteArrayInputStream(byteArray));
    }

    public static Image getHsvImage(Mat img) {
        Mat img2 = matReform(img);
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img2, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        return new Image(new ByteArrayInputStream(byteArray));
    }


    public static Image getImage(Mat img, CameraCustomizations cameraCustomizations) {
        Mat img2 = ImageUtils.matReform(img, cameraCustomizations);
        // Mat img2 = img;
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img2, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        return new Image(new ByteArrayInputStream(byteArray));
    }


}
