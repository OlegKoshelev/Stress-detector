package sample.DataGetting.Tasks;

import sample.DataGetting.Snapshot;
import sample.DataGetting.Values;
import sample.DataSaving.SettingsSaving.SettingsData;

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

    public FillBuffer(int dotsAmount, List<Values> bufferForAveraging, Lock bufferLock, BlockingQueue<Snapshot> snapshots, ExecutorService executorService, double d0) {
        this.dotsAmount = dotsAmount;
        this.bufferForAveraging = bufferForAveraging;
        this.bufferLock = bufferLock;
        this.snapshots = snapshots;
        this.executorService = executorService;
        this.d0 = d0;
    }

    @Override
    public void run() {
        int counter = 0;
        List<Future<?>> futures = new ArrayList<Future<?>>();
        while (futures.size() != dotsAmount - 1){
            if (snapshots.size() > 0) {
             Future future = executorService.submit(new AddToBuffer(bufferForAveraging,snapshots,d0,bufferLock));
             futures.add(future);
                try {
                    Thread.sleep( 1000 / SettingsData.getInstance().getFps());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        for(Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }


        }
    }
}
