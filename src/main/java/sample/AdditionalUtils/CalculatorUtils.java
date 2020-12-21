package sample.AdditionalUtils;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import sample.DataGetting.Values;
import sample.DataSaving.SettingsSaving.DynamicSettings.CameraCustomizations;
import sample.DataSaving.SettingsSaving.SettingsData;
import sample.Utils.ImageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CalculatorUtils {
    public static double getMedian(List<Double> list){
        Collections.sort(list);
        int size = list.size();
        double result = 0;
        if (size == 0){
            return 0;
        }
        if (size == 1){
            result = list.get(0);
        }

        if (size % 2 == 0){  //для четной выборки
            result = (list.get(size/2) + list.get((size/2)-1)) / 2;
        }
        else{// для нечетной выборки
            result = list.get((size-1)/2);
        }
        return result;
    }

    public static Double coordinates(Mat img) {
        if (img == null)
            return null;
        //  System.load("C:\\opencv_3_3\\build\\java\\x64\\opencv_java330.dll");
        Mat img2 = ImageUtils.matReform(img, CameraCustomizations.getInstance());
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(img2, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
        if (contours.size() < 2)
            return  null;
        System.out.println("size of countours++++++++++++++++++++" + contours.size());
        Collections.sort(contours, new Comparator<MatOfPoint>() {
            @Override
            public int compare(MatOfPoint o1, MatOfPoint o2) {
                return   (int) (o2.size().area() - o1.size().area());
            }
        });

        System.out.println(contours.get(0).size() + "===============первая точка" );
        System.out.println(contours.get(1).size() + "===============вторая точка" );
        for (MatOfPoint point :
                contours) {
            System.out.println("размер точки ---------" + point.size());
        }
        Point pointOne = new Point();
        Point pointTwo = new Point();
        Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(0).toArray()), pointOne, new float[1]);
        Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(1).toArray()), pointTwo, new float[1]);
        Imgproc.cvtColor(img2, img2, Imgproc.COLOR_BayerGB2BGR);
        Imgproc.circle(img2, pointOne, 1, new Scalar(0, 0, 255), 8);
        Imgproc.circle(img2, pointTwo, 1, new Scalar(0, 0, 255), 8);
        System.out.println(pointOne + " точка 1" );
        System.out.println(pointTwo + "точка 2");
        return distance(pointOne, pointTwo);
    }
    private static double distance(Point one, Point two) {
        double result;
        result = Math.sqrt(Math.pow(one.x - two.x, 2) + Math.pow(one.y - two.y, 2));
        return result;
    }

    public static double curvature(double D, double D0) {
        return (Math.cos(Math.toRadians(SettingsData.getInstance().getAngle())) / (2 * SettingsData.getInstance().getDistance())) * (1 - (D / D0));
    }

    public static double stressThickness(int biaxialModulus, double substrateThickness, double curvature) {
        return (curvature * biaxialModulus * 1000000 * (Math.pow(substrateThickness, 2))) / 6;
    }


}
