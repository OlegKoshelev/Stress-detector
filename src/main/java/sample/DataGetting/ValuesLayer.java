package sample.DataGetting;


import sample.DataGetting.Tasks.CalculateDAverage;
import sample.DataGetting.Tasks.CalculateStressAndCurvature;
import sample.DataGetting.Tasks.ReadFromCamera;

import java.util.concurrent.*;

public class ValuesLayer implements ModelLayer {

    private ReadFromCamera cameraReader;
    private BlockingQueue<Snapshot> inputQueue;
    private BlockingQueue<Spot> calculatorDQueue;
    private CopyOnWriteArrayList<Spot> averageD;
    private BlockingQueue<Values> values;
    private int threadsCount;
    private ExecutorService averageDCalculating;
    private ExecutorService valuesCalculating;
    private double d0 = 0;


    public ValuesLayer(int threadsCount, double d0) throws Exception {
        this.d0 = d0;
        this.threadsCount = threadsCount;
        inputQueue = new LinkedBlockingQueue<>();
        calculatorDQueue = new LinkedBlockingQueue<>();
        averageD = new CopyOnWriteArrayList<>();
        this.cameraReader = new ReadFromCamera(inputQueue);
        this.values = new LinkedBlockingQueue<>();
        averageDCalculating = Executors.newFixedThreadPool(threadsCount);
        valuesCalculating = Executors.newFixedThreadPool(threadsCount);
    }


    public void start() throws InterruptedException {
        new Thread(cameraReader).start();
        if (d0 == 0) {
            CountDownLatch countDownLatchList = new CountDownLatch(threadsCount); // to wait until the list averageD is filled
            for (int i = 0; i < threadsCount; i++) {
                averageDCalculating.submit(new CalculateDAverage(inputQueue, calculatorDQueue, averageD, countDownLatchList));
            }
            averageDCalculating.shutdown();
            countDownLatchList.await();
            d0 = calculateD0();
        }
        for (int i = 0; i < threadsCount; i++) {
            valuesCalculating.submit(new CalculateStressAndCurvature(inputQueue, calculatorDQueue, d0, values));
        }
        valuesCalculating.shutdown();
    }

    public void setD0 (double value){
        d0 = value;
    }

    public double getD0(){
        return d0;
    }

    public double calculateD0() {
        double result = 0;
        for (Spot d : averageD) {
            result += d.getDistance();
        }
        return result / averageD.size();
    }

    @Override
    public BlockingQueue<Values> getValuesQueue() throws InterruptedException {
        start();
        return values;
    }

    @Override
    public void stop() {
        cameraReader.Stop();
        averageDCalculating.shutdownNow();
        valuesCalculating.shutdownNow();
    }
}
