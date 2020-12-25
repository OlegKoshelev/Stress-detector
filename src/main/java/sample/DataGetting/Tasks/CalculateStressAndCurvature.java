package sample.DataGetting.Tasks;


import javafx.scene.image.Image;
import sample.AdditionalUtils.CalculatorUtils;
import sample.DataGetting.CvUtils;
import sample.DataGetting.Spot;
import sample.DataGetting.Snapshot;
import sample.DataGetting.Values;
import sample.Utils.ImageUtils;
import sample.DataSaving.SettingsSaving.SettingsData;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

public class CalculateStressAndCurvature implements Runnable {
    private BlockingQueue<Snapshot> queueIn;
    private BlockingQueue<Spot> queueD;
    private BlockingQueue<Values> values;
    private double D0;
    private SettingsData settingsData = SettingsData.getInstance();



    public CalculateStressAndCurvature(BlockingQueue<Snapshot> queueIn, BlockingQueue<Spot> queueD, double D0, BlockingQueue<Values> values) {
        this.queueIn = queueIn;
        this.queueD = queueD;
        this.D0 = D0;
        this.values = values;
    }

    @Override
    public void run() {
        while (true) {
            Snapshot spots = null;
            try {
                spots = queueIn.take();
                System.out.println(spots + " пятна");
                if (spots == null) continue;
                System.out.println(spots.getImg() + " изображеие");
                Double d = CalculatorUtils.getDistance(spots.getImg());
                if (d == null) continue;

                Spot distance = new Spot(d, spots.getDate(),spots.getImg());
                queueD.put(distance);
                Spot D = queueD.take();
                Date timestamp = D.getTimestamp();
                double curvature = CalculatorUtils.getCurvature( D.getDistance(), D0);
                double stressThickness = CalculatorUtils.getStressThickness(602, 0.00043, curvature);
                Image image = ImageUtils.getHsvImage(D.getImg());
             //   values.put(new Values(stressThickness, curvature, timestamp,D.getDistance(),image));
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.println(Thread.currentThread().getName() + "is finished");
    }

}
