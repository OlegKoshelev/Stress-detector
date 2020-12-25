package sample.DataGetting.Tasks;

import sample.AdditionalUtils.CalculatorUtils;
import sample.DataGetting.Snapshot;
import sample.DataGetting.Values;


import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;

public class AddToBuffer implements Runnable{
    private List<Values> bufferForAveraging;
    private Lock bufferLock;
    private BlockingQueue<Snapshot> snapshots;
    private double d0;

    public AddToBuffer(List<Values> bufferForAveraging, BlockingQueue<Snapshot> snapshots, double d0, Lock bufferLock) {
        this.bufferForAveraging = bufferForAveraging;
        this.bufferLock = bufferLock;
        this.snapshots = snapshots;
        this.d0 = d0;
    }

    @Override
    public void run() {
        try {
            Snapshot snapshot = snapshots.take();
            double distance = CalculatorUtils.getDistance(snapshot.getImg());
            double curvature = CalculatorUtils.getCurvature(distance,d0);
            double stressThickness = CalculatorUtils.getStressThickness(602,0.00043,curvature);
            bufferLock.lock();
            bufferForAveraging.add(new Values(stressThickness, curvature, snapshot.getDate(),distance));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bufferLock.unlock();
        }







    }
}
