package sample.DataGetting.Tasks;

import de.gsi.dataset.spi.DefaultErrorDataSet;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import sample.AdditionalUtils.CalculatorUtils;
import sample.DataBase.AverageTableHelper;
import sample.DataBase.DetailedTableHelper;
import sample.DataBase.Entities.AveragingTable;
import sample.DataBase.Entities.DetailedTable;
import sample.DataBase.HibernateUtil;
import sample.DataGetting.Snapshot;
import sample.DataGetting.Values;
import sample.DataSaving.SettingsSaving.SettingsData;
import sample.Utils.ImageUtils;
import sample.Utils.TemporaryValues;

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
    private TemporaryValues detailedTableValues;
    private TemporaryValues averageTableValues;
    private HibernateUtil hibernateUtil;

    public CalculateValues(DefaultErrorDataSet dataSet, ImageView imageView, BlockingQueue<Snapshot> snapshots, BlockingQueue<Values> values, List<Values> bufferForAveraging
            , double d0, Lock dataSetLock, Lock bufferLock, TemporaryValues detailedTableValues, TemporaryValues averageTableValues,HibernateUtil hibernateUtil) {
        this.imageView = imageView;
        this.dataSet = dataSet;
        this.snapshots = snapshots;
        this.values = values;
        this.dataSetLock = dataSetLock;
        this.d0 = d0;
        this.bufferForAveraging = bufferForAveraging;
        this.bufferLock = bufferLock;
        this.detailedTableValues = detailedTableValues;
        this.averageTableValues = averageTableValues;
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public void run() {

        try {
            Snapshot snapshot = snapshots.take();
            double distance = CalculatorUtils.getDistance(snapshot.getImg());
            double curvature = CalculatorUtils.getCurvature(distance,d0);
            double stressThickness = CalculatorUtils.getStressThickness(curvature);
            Platform.runLater(() -> imageView.setImage(ImageUtils.getHsvImage(snapshot.getImg())));
            Values usualValues = new Values(stressThickness, curvature, snapshot.getDate(),distance);
            values.put(usualValues);

            bufferLock.lock();
            dataSetLock.lock();
            bufferForAveraging.add(usualValues);
            DetailedTable dtValues = new DetailedTable(usualValues);
            detailedTableValues.addValue(dtValues);

            Values averageValues = CalculatorUtils.getAverageValues(bufferForAveraging);
            bufferForAveraging.remove(0);
            dataSet.add(averageValues.getTimestamp().getTime(),averageValues.getMeasuredValue(SettingsData.getInstance().getType()));
            AveragingTable avValues = new AveragingTable(averageValues);
            averageTableValues.addValue(avValues);
            System.out.println(averageTableValues.size() + "-----------AVERAGE");
            System.out.println(detailedTableValues.size() + "-----------DETAILED");

        } catch (InterruptedException e) {
           return;
        }
        finally {
            dataSetLock.unlock();
            bufferLock.unlock();
        }
    }

}
