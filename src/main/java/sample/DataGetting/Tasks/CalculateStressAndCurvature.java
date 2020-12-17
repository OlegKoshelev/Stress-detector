package sample.DataGetting.Tasks;


import javafx.scene.image.Image;
import sample.DataGetting.CvUtils;
import sample.DataGetting.Distance;
import sample.DataGetting.Spots;
import sample.DataGetting.Values;
import sample.Utils.ImageUtils;
import sample.DataSaving.SettingsSaving.SettingsData;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

public class CalculateStressAndCurvature implements Runnable {
    private BlockingQueue<Spots> queueIn;
    private BlockingQueue<Distance> queueD;
    private BlockingQueue<Values> values;
    private double D0;
    private SettingsData settingsData = SettingsData.getInstance();



    public CalculateStressAndCurvature(BlockingQueue<Spots> queueIn, BlockingQueue<Distance> queueD, double D0, BlockingQueue<Values> values) {
        this.queueIn = queueIn;
        this.queueD = queueD;
        this.D0 = D0;
        this.values = values;
    }

    @Override
    public void run() {
        while (true) {
            Spots spots = null;
            try {
                spots = queueIn.take();
                System.out.println(spots + " пятна");
                if (spots == null) continue;
                System.out.println(spots.getImg() + " изображеие");
                Double d = CvUtils.coordinates(spots.getImg());
                if (d == null) continue;

                Distance distance = new Distance(d, spots.getDate(),spots.getImg());
                queueD.put(distance);
                Distance D = queueD.take();
                Date timestamp = D.getTimestamp();
                double curvature = CvUtils.curvature( D.getDistance(), D0);
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
