package sample.InSituStress.Model.GettingData;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

public class CalculatorDAverage implements Runnable{
    private BlockingQueue<Spots> queueIn;
    private BlockingQueue<Distance> queueD;
    private CopyOnWriteArrayList<Distance> averageD;
    private CountDownLatch countDownLatch;
    private boolean stop = false;


    public CalculatorDAverage(BlockingQueue<Spots> queueIn, BlockingQueue<Distance> queueD, CopyOnWriteArrayList<Distance> averageD, CountDownLatch countDownLatch) {
        this.queueIn = queueIn;
        this.queueD = queueD;
        this.averageD = averageD;
        this.countDownLatch = countDownLatch;
    }


    @Override
    public void run() {
        Date startTime = new Date();
        while ((new Date().getTime() - startTime.getTime() < 1000) ) {
            Spots spots;
            try {
                spots = queueIn.take();
                if (spots == null) continue;
                double d = CvUtils.coordinates(spots.getImg());
                Distance distance = new Distance(d, spots.getDate(), spots.getImg());
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
