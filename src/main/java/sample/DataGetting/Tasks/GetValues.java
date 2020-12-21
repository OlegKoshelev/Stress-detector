package sample.DataGetting.Tasks;

import sample.DataGetting.Snapshot;
import sample.DataGetting.Values;
import sample.DataSaving.SettingsSaving.SettingsData;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class GetValues implements Runnable{
    private BlockingQueue<Snapshot> snapshots;
    private BlockingQueue<Values> values;
    private double d0;
    private ExecutorService executorService;

    public GetValues(BlockingQueue<Snapshot> snapshots, BlockingQueue<Values> values, double d0, ExecutorService executorService) {
        this.snapshots = snapshots;
        this.values = values;
        this.d0 = d0;
        this.executorService = executorService;
    }

    @Override
    public void run() {
        while (true){
            if (snapshots.size() > 0){
                executorService.submit(new CalculateValues(snapshots,values,d0));
                try {
                    Thread.sleep ( 1000 /SettingsData.getInstance().getFps());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
