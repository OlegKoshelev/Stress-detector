package sample.DataGetting.Tasks;

import de.gsi.dataset.spi.DefaultErrorDataSet;
import javafx.scene.image.ImageView;
import sample.DataBase.AverageTableHelper;
import sample.DataBase.DetailedTableHelper;
import sample.DataBase.HibernateUtil;
import sample.DataBase.RegularTableHelper;
import sample.DataGetting.Snapshot;
import sample.DataGetting.Values;
import sample.Utils.TemporaryValues;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;

public class GetValues implements Runnable{
    private DefaultErrorDataSet dataSet;
    private ImageView imageView;
    private BlockingQueue<Snapshot> snapshots;
    private BlockingQueue<Values> values;
    private Lock dataSetLock;
    private double d0;
    private ExecutorService executorService;
    private List<Values> bufferForAveraging;
    private Lock bufferLock;
    private TemporaryValues detailedTableValues;
    private TemporaryValues averageTableValues;
    private HibernateUtil hibernateUtil;

    public GetValues(DefaultErrorDataSet dataSet, ImageView imageView, BlockingQueue<Snapshot> snapshots, BlockingQueue<Values> values,
                     double d0,List<Values> bufferForAveraging, ExecutorService executorService, Lock dataSetLock, Lock bufferLock,
                     TemporaryValues detailedTableValues, TemporaryValues averageTableValues,HibernateUtil hibernateUtil) {
        this.imageView = imageView;
        this.dataSet = dataSet;
        this.snapshots = snapshots;
        this.values = values;
        this.dataSetLock = dataSetLock;
        this.d0 = d0;
        this.executorService = executorService;
        this.bufferForAveraging = bufferForAveraging;
        this.bufferLock = bufferLock;
        this.detailedTableValues = detailedTableValues;
        this.averageTableValues = averageTableValues;
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public void run() {

        while (true){
            if (averageTableValues.size() >= 1000){
                bufferLock.lock();
                dataSetLock.lock();
                System.out.println("> 1000");
                // заносим данные в усредненную таблицу
              //  AverageTableHelper averageTableHelper = new AverageTableHelper(hibernateUtil);
              //  averageTableHelper.addTableList(averageTableValues.getList());
                averageTableValues.reset();
                // заносим данные в подробную таблицу
                DetailedTableHelper detailedTableHelper = new DetailedTableHelper(hibernateUtil);
                detailedTableHelper.addTableList(detailedTableValues.getList());
                detailedTableValues.reset();
                dataSetLock.unlock();
                bufferLock.unlock();

            }
            if (snapshots.size() > 0){
                executorService.submit(new CalculateValues(dataSet,imageView,snapshots,values,bufferForAveraging,d0,dataSetLock,bufferLock,detailedTableValues, averageTableValues,hibernateUtil));
                try {
                    Thread.sleep ( 1);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }

    }
}
