package sample.DataGetting.Tasks;

import sample.DataGetting.CvUtils;
import sample.DataGetting.Snapshot;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class CalculateDistance implements Runnable {
    private BlockingQueue<Snapshot> snapshots;
    private CopyOnWriteArrayList<Double> distance;

    public CalculateDistance(BlockingQueue<Snapshot> snapshots, CopyOnWriteArrayList<Double> distance) {
        this.snapshots = snapshots;
        this.distance = distance;
    }

    @Override
    public void run() {
        Snapshot snapshot;
        try {
            snapshot = snapshots.take();
            double d = CvUtils.coordinates(snapshot.getImg());
            distance.add(d);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
