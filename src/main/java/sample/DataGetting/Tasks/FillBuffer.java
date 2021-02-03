package sample.DataGetting.Tasks;

import sample.DataBase.HibernateUtil;
import sample.DataGetting.Snapshot;
import sample.DataGetting.Values;
import sample.DataSaving.SettingsSaving.SettingsData;
import sample.Utils.TemporaryValues;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;

public class FillBuffer implements Runnable {
    private int dotsAmount;
    private List<Values> bufferForAveraging;
    private Lock bufferLock;
    private BlockingQueue<Snapshot> snapshots;
    private ExecutorService executorService;
    private double d0;
    private TemporaryValues detailedTableValues;
    private HibernateUtil hibernateUtil;


    public FillBuffer(int dotsAmount, List<Values> bufferForAveraging, Lock bufferLock, BlockingQueue<Snapshot> snapshots, ExecutorService executorService, double d0,
                      TemporaryValues detailedTableValues, HibernateUtil hibernateUtil) {
        this.dotsAmount = dotsAmount;
        this.bufferForAveraging = bufferForAveraging;
        this.bufferLock = bufferLock;
        this.snapshots = snapshots;
        this.executorService = executorService;
        this.d0 = d0;
        this.detailedTableValues = detailedTableValues;
        this.hibernateUtil = hibernateUtil;

    }

    @Override
    public void run() {

        List<Future<?>> futures = new ArrayList<Future<?>>();
        while (futures.size() != dotsAmount - 1){
            if (snapshots.size() > 0) {
             Future future = executorService.submit(new AddToBuffer(bufferForAveraging,snapshots,d0,bufferLock,detailedTableValues));
             futures.add(future);
                try {
                    Thread.sleep( 1000 / SettingsData.getInstance().getFps());
                } catch (InterruptedException e) {
                   return;
                }
            }
        }
        for(Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                return;
            }
        }
    }
}
