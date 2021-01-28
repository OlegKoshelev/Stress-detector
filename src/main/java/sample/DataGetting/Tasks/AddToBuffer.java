package sample.DataGetting.Tasks;

import sample.AdditionalUtils.CalculatorUtils;
import sample.DataBase.Entities.DetailedTable;
import sample.DataGetting.Snapshot;
import sample.DataGetting.Values;
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
            Snapshot snapshot = snapshots.take();
            double distance = CalculatorUtils.getDistance(snapshot.getImg());
            double curvature = CalculatorUtils.getCurvature(distance,d0);
            double stressThickness = CalculatorUtils.getStressThickness(curvature);
            bufferLock.lock();
            Values val = new Values(stressThickness, curvature, snapshot.getDate(),distance);
            bufferForAveraging.add(val);
            DetailedTable dtValues = new DetailedTable(val);
            detailedTableValues.addValue(dtValues);
        } catch (InterruptedException e) {
           return;
        } finally {
            bufferLock.unlock();
        }







    }
}
