package sample.DataGetting.Tasks;

import de.gsi.dataset.spi.DefaultErrorDataSet;
import javafx.scene.image.ImageView;
import sample.AdditionalUtils.CalculatorUtils;
import sample.DataGetting.Snapshot;
import sample.DataGetting.Values;
import sample.DataSaving.SettingsSaving.SettingsData;
import sample.Utils.ImageUtils;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;

public class CalculateValues implements Runnable{
    private DefaultErrorDataSet dataSet;
    private ImageView imageView;
    private BlockingQueue<Snapshot> snapshots;
    private BlockingQueue<Values> values;
    private Lock dataSetLock;
    private double d0;
    private List<Values> bufferForAveraging;
    private Lock bufferLock;

    public CalculateValues(DefaultErrorDataSet dataSet, ImageView imageView, BlockingQueue<Snapshot> snapshots, BlockingQueue<Values> values,List<Values> bufferForAveraging
            ,double d0, Lock dataSetLock,Lock bufferLock) {
        this.imageView = imageView;
        this.dataSet = dataSet;
        this.snapshots = snapshots;
        this.values = values;
        this.dataSetLock = dataSetLock;
        this.d0 = d0;
        this.bufferForAveraging = bufferForAveraging;
        this.bufferLock = bufferLock;
    }

    @Override
    public void run() {

        try {
            Snapshot snapshot = snapshots.take();
            double distance = CalculatorUtils.getDistance(snapshot.getImg());
            double curvature = CalculatorUtils.getCurvature(distance,d0);
            double stressThickness = CalculatorUtils.getStressThickness(602,0.00043,curvature);
            imageView.setImage(ImageUtils.getHsvImage(snapshot.getImg()));
            Values usualValues = new Values(stressThickness, curvature, snapshot.getDate(),distance);
            values.put(usualValues);

            bufferLock.lock();
            dataSetLock.lock();
            bufferForAveraging.add(usualValues);
            Values averageValues = CalculatorUtils.getAverageValues(bufferForAveraging);
            bufferForAveraging.remove(0);
            dataSet.add(averageValues.getTimestamp().getTime(),averageValues.getDistance());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            dataSetLock.unlock();
            bufferLock.unlock();
        }
    }

}
