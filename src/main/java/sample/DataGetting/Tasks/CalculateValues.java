package sample.DataGetting.Tasks;

import de.gsi.dataset.spi.DefaultErrorDataSet;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Point;
import sample.AdditionalUtils.CalculatorUtils;
import sample.DataBase.Entities.AveragingTable;
import sample.DataBase.Entities.DetailedTable;
import sample.DataBase.HibernateUtil;
import sample.DataGetting.Snapshot;
import sample.DataGetting.Values;
import sample.DataSaving.SettingsSaving.SettingsData;
import sample.Utils.ImageUtils;
import sample.Utils.TemporaryValues;

import java.util.Date;
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
    private TemporaryValues<DetailedTable> detailedTableValues;
    private TemporaryValues<AveragingTable> averageTableValues;
    private HibernateUtil hibernateUtil;

    public CalculateValues(DefaultErrorDataSet dataSet, ImageView imageView, BlockingQueue<Snapshot> snapshots, BlockingQueue<Values> values, List<Values> bufferForAveraging
            , double d0, Lock dataSetLock, Lock bufferLock, TemporaryValues<DetailedTable> detailedTableValues, TemporaryValues<AveragingTable> averageTableValues,HibernateUtil hibernateUtil) {
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
            Point [] points = new Point[2];
            double [] sizes = new double[2];
            bufferLock.lock();
            dataSetLock.lock();
            Snapshot snapshot = snapshots.take();
//            Platform.runLater(() -> imageView.setImage(ImageUtils.getHsvImage(snapshot.getImg())));
            Image image = ImageUtils.getHsvImageWithСenters(snapshot.getImg(),points,sizes);
            Platform.runLater(() -> imageView.setImage(image));
            if (points[0] != null && points[1] != null) {
                double distance = CalculatorUtils.getDistance(points);
                double curvature = CalculatorUtils.getCurvature(distance,d0);
                double stressThickness = CalculatorUtils.getStressThickness(curvature);
                double x1 = points[0].x;
                double y1 = points[0].y;
                double x2 = points[1].x;
                double y2 = points[1].y;
                double size1 = sizes[0];
                double size2 = sizes[1];
                Values usualValues = new Values(stressThickness, curvature, snapshot.getDate(),distance,x1,y1,x2,y2,size1,size2);
                values.put(usualValues);


                bufferForAveraging.add(usualValues);
                DetailedTable dtValues = new DetailedTable(usualValues);
                detailedTableValues.addValue(dtValues);

                Values averageValues = CalculatorUtils.getAverageValues(bufferForAveraging);
                bufferForAveraging.remove(0);
                dataSet.add(averageValues.getTimestamp().getTime(),averageValues.getMeasuredValue(SettingsData.getInstance().getType()));
                AveragingTable avValues = new AveragingTable(averageValues);
                averageTableValues.addValue(avValues);
            }
        } catch (InterruptedException e) {
           return;
        }
        finally {
            dataSetLock.unlock();
            bufferLock.unlock();
        }
    }

}
