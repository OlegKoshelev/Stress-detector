package sample.DataGetting.Tasks;

import de.gsi.dataset.spi.DefaultErrorDataSet;
import javafx.scene.image.ImageView;
import org.apache.log4j.Logger;
import sample.DataBase.Entities.AveragingTable;
import sample.DataBase.Entities.DetailedTable;
import sample.DataBase.HibernateUtil;
import sample.DataBase.TableHelper;
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
    private TemporaryValues<DetailedTable> detailedTableValues;
    private TemporaryValues<AveragingTable> averageTableValues;
    private HibernateUtil hibernateUtil;
    private final Logger logger = Logger.getLogger(GetValues.class);

    public GetValues(DefaultErrorDataSet dataSet, ImageView imageView, BlockingQueue<Snapshot> snapshots, BlockingQueue<Values> values,
                     double d0,List<Values> bufferForAveraging, ExecutorService executorService, Lock dataSetLock, Lock bufferLock,
                     TemporaryValues<DetailedTable> detailedTableValues, TemporaryValues<AveragingTable> averageTableValues,HibernateUtil hibernateUtil) {
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
        logger.info(bufferForAveraging.size());


        while (true){
            if (averageTableValues.size() >= 1000){
                bufferLock.lock();
                dataSetLock.lock();
                System.out.println("> 1000");
                // заносим данные в подробную таблицу
                TableHelper<DetailedTable> detailedTableHelper = new TableHelper<>(hibernateUtil,DetailedTable.class);
                detailedTableHelper.addTableList(detailedTableValues.getList());
                detailedTableValues.reset();
                // заносим данные в усредненную таблицу
                TableHelper<AveragingTable> averageTableHelper = new TableHelper<>(hibernateUtil,AveragingTable.class);
                averageTableHelper.addTableList(averageTableValues.getList());
                averageTableValues.reset();

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
