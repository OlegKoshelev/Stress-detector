package sample.DataGetting;

import de.gsi.dataset.spi.DefaultErrorDataSet;
import javafx.scene.image.ImageView;
import sample.DataBase.HibernateUtil;
import sample.DataGetting.Tasks.*;
import sample.Utils.TemporaryValues;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Calculator {
    private DefaultErrorDataSet dataSet;
    private ImageView imageView;
    private ExecutorService executorService;
    private BlockingQueue<Snapshot> snapshots;
    private BlockingQueue<Values> values;
    private List<Values> bufferForAveraging;
    private Lock dataSetLock;
    private Lock bufferLock;
    private TemporaryValues detailedTableValues;
    private TemporaryValues averageTableValues;
    private HibernateUtil hibernateUtil;
    private double d0;


    public Calculator(DefaultErrorDataSet dataSet, ImageView imageView, int poolSize, double d0, HibernateUtil hibernateUtil, TemporaryValues detailedTableValues, TemporaryValues averageTableValues) {
        this.dataSet = dataSet;
        this.imageView = imageView;
        snapshots = new LinkedBlockingQueue<>();
        values = new LinkedBlockingQueue<>();
        dataSetLock = new ReentrantLock();
        bufferLock = new ReentrantLock();
        bufferForAveraging = new ArrayList<>();
        this.executorService = Executors.newFixedThreadPool(poolSize);
        this.detailedTableValues = detailedTableValues;
        this.averageTableValues = averageTableValues;
        this.d0 = d0;
        this.hibernateUtil = hibernateUtil;
    }

    public void start() throws Exception {
        executorService.execute(new ReadFromCamera(snapshots));// запуск считывания изображений с камеры, задача выполняется до прекращения работы программы
        if (d0 == 0) { // даем executorService задачи  по вычислению стартового расстояния, кол-во задач зависит от заполненности листа snapshots, задачи подаются в течении определенного времени
            Future<Double> future = executorService.submit(new CalculateStartingDistance(snapshots, executorService));
            d0 = future.get();
            System.out.println(d0 + " -------D0");
            executorService.execute(new FillBuffer(100, bufferForAveraging, bufferLock, snapshots, executorService, d0, detailedTableValues, hibernateUtil));
            executorService.execute(new GetValues(dataSet, imageView, snapshots, values, d0, bufferForAveraging, executorService, dataSetLock, bufferLock, detailedTableValues, averageTableValues, hibernateUtil));
        } else { // даем executorService задачи  по вычислению values, задача выполняется до прекращения работы программы, кол-во задач зависит от заполненности листа snapshots
            executorService.execute(new FillBuffer(100, bufferForAveraging, bufferLock, snapshots, executorService, d0, detailedTableValues, hibernateUtil));
            executorService.execute(new GetValues(dataSet, imageView, snapshots, values, d0, bufferForAveraging, executorService, dataSetLock, bufferLock, detailedTableValues, averageTableValues, hibernateUtil));
        }

    }


    public void stop() {
        executorService.shutdownNow();
    }

    public double getD0() {
        return d0;
    }

    public BlockingQueue<Values> getValuesQueue() throws Exception {
        return values;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}
