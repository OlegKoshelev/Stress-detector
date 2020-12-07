package sample.Utils;

import javafx.scene.image.Image;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import sample.DataSaving.SettingsSaving.DynamicSettings.CameraCustomizations;
import sample.DataSaving.SettingsSaving.SettingsData;

import java.io.ByteArrayInputStream;

public class ImageUtils {

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

    public static Image getHsvImage(Mat img, SettingsData settingsData) {
        Mat img2 = matReform(img, settingsData);
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img2, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        return new Image(new ByteArrayInputStream(byteArray));
    }

    private static Mat matReform(Mat img, SettingsData settingsData) {
        int hueMax = settingsData.getHueMax();
        int hueMin = settingsData.getHueMin();
        int saturationMax = settingsData.getSaturationMax();
        int saturationMin = settingsData.getSaturationMin();
        int valueMax = settingsData.getValueMax();
        int valueMin = settingsData.getValueMin();
        Mat hsv = new Mat();
        Imgproc.cvtColor(img, hsv, Imgproc.COLOR_BGR2HSV);
        Mat img2 = new Mat();

        Core.inRange(hsv, new Scalar(hueMin, saturationMin, valueMin),
                new Scalar(hueMax, saturationMax, valueMax), img2);

        return img2;
    }
}
