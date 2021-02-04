package sample.DataGetting;

import de.gsi.dataset.spi.DefaultErrorDataSet;
import javafx.scene.image.ImageView;
import org.apache.log4j.Logger;
import sample.DataBase.Entities.AveragingTable;
import sample.DataBase.Entities.DetailedTable;
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
//    private List<Values> bufferForAveraging;
//    private Lock dataSetLock;
//    private Lock bufferLock;
    private TemporaryValues<DetailedTable> detailedTableValues;
    private TemporaryValues<AveragingTable> averageTableValues;
    private HibernateUtil hibernateUtil;
    private double d0;
    private final Logger logger = Logger.getLogger(Calculator.class);


    public Calculator(DefaultErrorDataSet dataSet, ImageView imageView, double d0, HibernateUtil hibernateUtil,
                      TemporaryValues<DetailedTable> detailedTableValues, TemporaryValues<AveragingTable> averageTableValues) {
        this.dataSet = dataSet;
        this.imageView = imageView;
        snapshots = new LinkedBlockingQueue<>();
        values = new LinkedBlockingQueue<>();
//        dataSetLock = new ReentrantLock();
//        bufferLock = new ReentrantLock();
//        bufferForAveraging = new ArrayList<>();
        this.executorService = Executors.newCachedThreadPool();
        this.detailedTableValues = detailedTableValues;
        this.averageTableValues = averageTableValues;
        this.d0 = d0;
        this.hibernateUtil = hibernateUtil;
    }

    public void start() throws Exception {
        executorService.execute(new ReadFromCamera(dataSet,imageView,d0,hibernateUtil,detailedTableValues,averageTableValues));
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
