package sample.DataGetting.Tasks;

import sample.DataBase.Entities.AveragingTable;
import sample.DataBase.Entities.DetailedTable;
import sample.DataBase.HibernateUtil;
import sample.DataBase.TableHelper;
import sample.Utils.TemporaryValues;

import java.util.concurrent.locks.Lock;

public class AddDataToTables implements Runnable {
    private Lock dataSetLock;
    private Lock bufferLock;
    private TemporaryValues<DetailedTable> detailedTableValues;
    private TemporaryValues<AveragingTable> averageTableValues;
    private HibernateUtil hibernateUtil;

    public AddDataToTables(Lock dataSetLock, Lock bufferLock, TemporaryValues<DetailedTable> detailedTableValues,
                           TemporaryValues<AveragingTable> averageTableValues, HibernateUtil hibernateUtil) {
        this.dataSetLock = dataSetLock;
        this.bufferLock = bufferLock;
        this.detailedTableValues = detailedTableValues;
        this.averageTableValues = averageTableValues;
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public void run() {
        bufferLock.lock();
        dataSetLock.lock();
        // заносим данные в подробную таблицу
        TableHelper<DetailedTable> detailedTableHelper = new TableHelper<>(hibernateUtil, DetailedTable.class);
        detailedTableHelper.addTableList(detailedTableValues.getList());
        detailedTableValues.reset();
        // заносим данные в усредненную таблицу
        TableHelper<AveragingTable> averageTableHelper = new TableHelper<>(hibernateUtil, AveragingTable.class);
        averageTableHelper.addTableList(averageTableValues.getList());
        averageTableValues.reset();
        dataSetLock.unlock();
        bufferLock.unlock();
    }
}
