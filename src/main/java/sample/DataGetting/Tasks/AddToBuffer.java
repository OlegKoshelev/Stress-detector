package sample.DataGetting.Tasks;

import org.opencv.core.Point;
import sample.AdditionalUtils.CalculatorUtils;
import sample.DataBase.Entities.DetailedTable;
import sample.DataGetting.Snapshot;
import sample.DataGetting.Values;
import sample.Utils.ImageUtils;
import sample.Utils.TemporaryValues;


import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;

public class AddToBuffer implements Runnable{
    private List<Values> bufferForAveraging;
    private Lock bufferLock;
    private BlockingQueue<Snapshot> snapshots;
    private double d0;
    private TemporaryValues detailedTableValues;

    public AddToBuffer(List<Values> bufferForAveraging, BlockingQueue<Snapshot> snapshots, double d0, Lock bufferLock, TemporaryValues detailedTableValues) {
        this.bufferForAveraging = bufferForAveraging;
        this.bufferLock = bufferLock;
        this.snapshots = snapshots;
        this.d0 = d0;
        this.detailedTableValues = detailedTableValues;
    }

    @Override
    public void run() {
        try {
            Point [] points = new Point[2];
            double [] sizes = new double[2];
            Snapshot snapshot = snapshots.take();
            ImageUtils.getHsvImageWithСenters(snapshot.getImg(),points,sizes);
            if (points[0] != null && points[1] != null) {
                double distance = CalculatorUtils.getDistance(points);
                double curvature = CalculatorUtils.getCurvature(distance, d0);
                double stressThickness = CalculatorUtils.getStressThickness(curvature);
                double x1 = points[0].x;
                double y1 = points[0].y;
                double x2 = points[1].x;
                double y2 = points[1].y;
                double size1 = sizes[0];
                double size2 = sizes[1];
                bufferLock.lock();
                Values val = new Values(stressThickness, curvature, snapshot.getDate(),distance,x1,y1,x2,y2,size1,size2);
                bufferForAveraging.add(val);
                DetailedTable dtValues = new DetailedTable(val);
                detailedTableValues.addValue(dtValues);
                bufferLock.unlock();
            }
        } catch (InterruptedException e) {
           return;
        }







    }
}
