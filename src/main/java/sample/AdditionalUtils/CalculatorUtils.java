package sample.AdditionalUtils;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import sample.DataGetting.Values;
import sample.DataSaving.SettingsSaving.DynamicSettings.CameraCustomizations;
import sample.DataSaving.SettingsSaving.SettingsData;
import sample.Utils.ImageUtils;

import java.util.*;

public class CalculatorUtils {

    public static Values getAverageValues(List<Values> values){
        Collections.sort(values);
        double distance = 0;
        double curvature = 0;
        double stressThickness = 0;
        for (Values value :
                values) {
            distance +=value.getDistance();
            curvature += value.getCurvature();
            stressThickness += value.getStressThickness();
        }
        return new Values(stressThickness / values.size(),curvature / values.size(), values.get(0).getTimestamp(), distance / values.size());
    }

    public static Values getMedianValueS( List <Values> values ){
        Collections.sort(values);
        int size = values.size();
        Values result = null;
        if (size == 0){
            return null;
        }
        if (size == 1){
            result = values.get(0);
        }
        if (size == 2){
            result = getAverageMagnitude(values.get(0), values.get(1));
        }
        if (size % 2 == 0){  //для четной выборки
            result =  getAverageMagnitude(values.get(size/2),values.get((size/2)-1));
        }
        else{ // для нечетной выборки
            result = values.get((size-1)/2);
        }
        result.setTimestamp(values.get(0).getTimestamp()); // берем самую первую дату в выборке
        return  result;
    }

    private static Values getAverageMagnitude (Values v1, Values v2){
        Values result = new Values();
        result.setDistance((v1.getDistance() + v2.getDistance())/2);
        result.setCurvature((v1.getCurvature() + v2.getCurvature())/2);
        result.setStressThickness((v1.getStressThickness() + v2.getStressThickness())/2);
        return result;
    }

    public static double getAverageDistance(List<Double> list){
        return list.stream().mapToDouble(a -> a).average().getAsDouble();
    }




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

    public static double getCurvature(double D, double D0) {
        return (Math.cos(Math.toRadians(SettingsData.getInstance().getAngle())) / (2 * SettingsData.getInstance().getDistance())) * (1 - (D / D0));
    }

    public static double getStressThickness( double curvature) {
        return (curvature * SettingsData.getInstance().getBiaxialModulusValue() * 1000000 * (Math.pow(getSubstrateThickness(), 2))) / 6;
    }
    
    private static double getSubstrateThickness (){
        return (double) SettingsData.getInstance().getThickness()/1000000;
    }

    public static double getDistance(Mat img) {
        if (img == null)
            return 0;
        //  System.load("C:\\opencv_3_3\\build\\java\\x64\\opencv_java330.dll");
        Mat img2 = ImageUtils.matReform(img);
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(img2, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
        if (contours.size() < 2)
            return  0;
        Collections.sort(contours, new Comparator<MatOfPoint>() {
            @Override
            public int compare(MatOfPoint o1, MatOfPoint o2) {
                return   (int) (o2.size().area() - o1.size().area());
            }
        });

        Point pointOne = new Point();
        Point pointTwo = new Point();
        Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(0).toArray()), pointOne, new float[1]);
        Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(1).toArray()), pointTwo, new float[1]);
        Imgproc.cvtColor(img2, img2, Imgproc.COLOR_BayerGB2BGR);
        Imgproc.circle(img2, pointOne, 1, new Scalar(0, 0, 255), 8);
        Imgproc.circle(img2, pointTwo, 1, new Scalar(0, 0, 255), 8);
        return calculateDistance(pointOne, pointTwo);
    }

    private static double calculateDistance(Point one, Point two) {
        return Math.sqrt(Math.pow(one.x - two.x, 2) + Math.pow(one.y - two.y, 2));
    }

    public static int getNumberOfContours(Mat img, CameraCustomizations cameraCustomizations){
        Mat img2 = ImageUtils.matReform(img, cameraCustomizations.getInstance());
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(img2, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
        return contours.size();
    }



}
