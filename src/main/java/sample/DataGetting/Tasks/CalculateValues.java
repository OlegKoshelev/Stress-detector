package sample.DataGetting.Tasks;

import javafx.scene.image.Image;
import sample.AdditionalUtils.CalculatorUtils;
import sample.DataGetting.Snapshot;
import sample.DataGetting.Values;
import sample.DataSaving.SettingsSaving.SettingsData;
import sample.Utils.ImageUtils;

import java.util.concurrent.BlockingQueue;

public class CalculateValues implements Runnable{
    private BlockingQueue<Snapshot> snapshots;
    private BlockingQueue<Values> values;
    private double d0;

    public CalculateValues(BlockingQueue<Snapshot> snapshots, BlockingQueue<Values> values, double d0) {
        this.snapshots = snapshots;
        this.values = values;
        this.d0 = d0;
    }

    @Override
    public void run() {
        try {
            Snapshot snapshot = snapshots.take();
            double distance = CalculatorUtils.coordinates(snapshot.getImg());
            double curvature = CalculatorUtils.curvature(distance,d0);
            double stressThickness = CalculatorUtils.stressThickness(602,0.00043,curvature);
            Image image = ImageUtils.getHsvImage(snapshot.getImg(), SettingsData.getInstance());
            values.put(new Values(stressThickness, curvature, snapshot.getDate(),distance,image));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
