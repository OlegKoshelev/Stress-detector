package sample.DataGetting;


import java.util.concurrent.*;

public class ValuesLayer implements ModelLayer {

    private CameraReader cameraReader;
    private BlockingQueue<Spots> inputQueue;
    private BlockingQueue<Distance> calculatorDQueue;
    private CopyOnWriteArrayList<Distance> averageD;
    private BlockingQueue<Values> values;
    private int threadsCount;
    private ExecutorService averageDCalculating;
    private ExecutorService valuesCalculating;


    public ValuesLayer(int threadsCount) throws Exception {
        this.threadsCount = threadsCount;
        inputQueue = new LinkedBlockingQueue<>();
        calculatorDQueue = new LinkedBlockingQueue<>();
        averageD = new CopyOnWriteArrayList<>();
        this.cameraReader = new CameraReader(inputQueue);
        this.values = new LinkedBlockingQueue<>();
        averageDCalculating = Executors.newFixedThreadPool(threadsCount);
        valuesCalculating = Executors.newFixedThreadPool(threadsCount);
    }


    public void start() throws InterruptedException {
        new Thread(cameraReader).start();
        CountDownLatch countDownLatchList = new CountDownLatch(threadsCount); // to wait until the list averageD is filled
        for (int i = 0; i < threadsCount; i++) {
            averageDCalculating.submit(new CalculatorDAverage(inputQueue, calculatorDQueue, averageD, countDownLatchList));
        }
        averageDCalculating.shutdown();
        countDownLatchList.await();
        double d0 = getD0();
        for (int i = 0; i < threadsCount; i++) {
            valuesCalculating.submit(new CalculatorStressAndCurvature(inputQueue, calculatorDQueue, d0, values));
        }
        valuesCalculating.shutdown();
    }


    public double getD0() {
        double result = 0;
        for (Distance d : averageD) {
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
