package sample.DataGetting;

import sample.DataGetting.Tasks.CalculateStartingDistance;
import sample.DataGetting.Tasks.GetValues;
import sample.DataGetting.Tasks.ReadFromCamera;

import java.util.concurrent.*;

public class Calculator {
    private ExecutorService executorService;
    private BlockingQueue<Snapshot> snapshots;
    private BlockingQueue<Values> values;
    private double d0;


    public Calculator(int count, double d0) {
        snapshots = new LinkedBlockingQueue<>();
        values = new LinkedBlockingQueue<>();
        this.executorService = Executors.newFixedThreadPool(count);
        this.d0 = d0;
    }

    public void start() throws Exception {
        executorService.execute(new ReadFromCamera(snapshots));// запуск считывания изображений с камеры, задача выполняется до прекращения работы программы
        if (d0 == 0) { // даем executorService задачи  по вычислению стартового расстояния, кол-во задач зависит от заполненности листа snapshots, задачи подаются в течении определенного времени
            Future<Double> future = executorService.submit(new CalculateStartingDistance(snapshots, executorService));
            d0 = future.get();
            System.out.println(d0 + " -------D0");
            executorService.execute(new GetValues(snapshots,values,d0,executorService));
        }else{ // даем executorService задачи  по вычислению values, задача выполняется до прекращения работы программы, кол-во задач зависит от заполненности листа snapshots
            executorService.execute(new GetValues(snapshots,values,d0,executorService));
        }
    }

    public void stop() {
        executorService.shutdownNow();
    }

    public double getD0(){
        return d0;
    }

    public BlockingQueue<Values> getValuesQueue() throws Exception {
        start();
        return values;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}
