package sample.DataGetting;

import javafx.scene.image.Image;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class CvUtils {
    public static BufferedImage MatToBufferedImage(Mat m) {
        if (m == null || m.empty()) return null;
        if (m.depth() == CvType.CV_8U) {
        } else if (m.depth() == CvType.CV_16U) { // CV_16U => CV_8U
            Mat m_16 = new Mat();
            m.convertTo(m_16, CvType.CV_8U, 255.0 / 65535);
            m = m_16;
        } else if (m.depth() == CvType.CV_32F) { // CV_32F => CV_8U
            Mat m_32 = new Mat();
            m.convertTo(m_32, CvType.CV_8U, 255);
            m = m_32;
        } else
            return null;
        int type = 0;
        if (m.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else if (m.channels() == 3)
            type = BufferedImage.TYPE_3BYTE_BGR;
        else if (m.channels() == 4)
            type = BufferedImage.TYPE_4BYTE_ABGR;
        else
            return null;
        byte[] buf = new byte[m.channels() * m.cols() * m.rows()];
        m.get(0, 0, buf);
        byte tmp = 0;
        if (m.channels() == 4) { // BGRA => ABGR
            for (int i = 0; i < buf.length; i += 4) {
                tmp = buf[i + 3];
                buf[i + 3] = buf[i + 2];
                buf[i + 2] = buf[i + 1];
                buf[i + 1] = buf[i];
                buf[i] = tmp;
            }
        }
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        byte[] data =
                ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buf, 0, data, 0, buf.length);
        return image;
    }

    public static double curvature(double angle, double length, double D, double D0) {
        return (Math.cos(angle) / (2 * length)) * (1 - (D / D0));
    }

    public static double stressThickness(int biaxialModulus, double substrateThickness, double curvature) {

        return (curvature * biaxialModulus * (Math.pow(substrateThickness, 2))) / 6;
    }

    public static double coordinates(Mat img) {
        //  System.load("C:\\opencv_3_3\\build\\java\\x64\\opencv_java330.dll");
        Mat img2 = matReform(img);
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(img2, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
        Point pointOne = new Point();
        Point pointTwo = new Point();
        Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(0).toArray()), pointOne, new float[1]);
        Imgproc.minEnclosingCircle(new MatOfPoint2f(contours.get(1).toArray()), pointTwo, new float[1]);
        Imgproc.cvtColor(img2, img2, Imgproc.COLOR_BayerGB2BGR);
        Imgproc.circle(img2, pointOne, 1, new Scalar(0, 0, 255), 8);
        Imgproc.circle(img2, pointTwo, 1, new Scalar(0, 0, 255), 8);
        return distance(pointOne, pointTwo);
    }

    private static Mat matReform(Mat img) {
        Mat hsv = new Mat();
        Imgproc.cvtColor(img, hsv, Imgproc.COLOR_BGR2HSV);
        Mat img2 = new Mat();
        Core.inRange(hsv, new Scalar(0, 0, 220),
                new Scalar(200, 70, 255), img2);
        return img2;
    }

    private static double distance(Point one, Point two) {
        double result;
        result = Math.sqrt(Math.pow(one.x - two.x, 2) + Math.pow(one.y - two.y, 2));
        return result;
    }

    public static void showResult(Mat img) {
        Imgproc.resize(img, img, new Size(1920, 1080));
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Image getImage(Mat img) {
        Mat img2 = matReform(img);
       // Mat img2 = img;
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img2, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        return new Image(new ByteArrayInputStream(byteArray));
    }
}