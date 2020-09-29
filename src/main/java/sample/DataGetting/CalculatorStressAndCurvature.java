package sample.DataGetting;


import javafx.scene.image.Image;
import sample.Utils.ImageUtils;
import sample.DataSaving.SettingsData;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

public class CalculatorStressAndCurvature implements Runnable {
    private BlockingQueue<Spots> queueIn;
    private BlockingQueue<Distance> queueD;
    private BlockingQueue<Values> values;
    private double D0;
    private SettingsData settingsData = SettingsData.getInstance();



    public CalculatorStressAndCurvature(BlockingQueue<Spots> queueIn, BlockingQueue<Distance> queueD, double D0, BlockingQueue<Values> values) {
        this.queueIn = queueIn;
        this.queueD = queueD;
        this.D0 = D0;
        this.values = values;
    }

    @Override
    public void run() {
        while (true) {
            Spots spots;
            try {
                spots = queueIn.take();
                if (spots == null) continue;
                double d = CvUtils.coordinates(spots.getImg());
                Distance distance = new Distance(d, spots.getDate(),spots.getImg());
                queueD.put(distance);
                Distance D = queueD.take();
                Date timestamp = D.getTimestamp();
                double curvature = CvUtils.curvature(1.22173, 0.5, D.getDistance(), D0);
                double stressThickness = CvUtils.stressThickness(602, 0.00043, curvature);
                Image image = ImageUtils.getHsvImage(D.getImg(),settingsData);
                values.put(new Values(stressThickness, curvature, timestamp,D.getDistance(),image));
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.println(Thread.currentThread().getName() + "is finished");
    }

}
