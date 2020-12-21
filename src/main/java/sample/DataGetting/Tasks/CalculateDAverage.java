package sample.DataGetting.Tasks;

import sample.DataGetting.CvUtils;
import sample.DataGetting.Spot;
import sample.DataGetting.Snapshot;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

public class CalculateDAverage implements Runnable{
    private BlockingQueue<Snapshot> queueIn;
    private BlockingQueue<Spot> queueD;
    private CopyOnWriteArrayList<Spot> averageD;
    private CountDownLatch countDownLatch;
    private boolean stop = false;


    public CalculateDAverage(BlockingQueue<Snapshot> queueIn, BlockingQueue<Spot> queueD, CopyOnWriteArrayList<Spot> averageD, CountDownLatch countDownLatch) {
        this.queueIn = queueIn;
        this.queueD = queueD;
        this.averageD = averageD;
        this.countDownLatch = countDownLatch;
    }


    @Override
    public void run() {
        Date startTime = new Date();
        while ((new Date().getTime() - startTime.getTime() < 1000) ) {
            Snapshot spots;
            try {
                spots = queueIn.take();
                if (spots == null) continue;
                double d = CvUtils.coordinates(spots.getImg());
                Spot distance = new Spot(d, spots.getDate(), spots.getImg());
                queueD.add(distance);
                averageD.add(distance);
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.println(Thread.currentThread().getName() + "is finished");
        countDownLatch.countDown();
    }

}
