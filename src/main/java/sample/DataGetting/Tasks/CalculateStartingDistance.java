package sample.DataGetting.Tasks;

import sample.AdditionalUtils.CalculatorUtils;
import sample.DataGetting.Snapshot;
import sample.DataSaving.SettingsSaving.SettingsData;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class CalculateStartingDistance implements Callable <Double> {
    private CopyOnWriteArrayList<Double> distance ;
    private BlockingQueue<Snapshot> snapshots;
    private ExecutorService executorService;


    public CalculateStartingDistance(BlockingQueue<Snapshot> snapshots, ExecutorService executorService) {
        this.distance = new CopyOnWriteArrayList<>();
        this.snapshots = snapshots;
        this.executorService = executorService;
    }

    @Override
    public Double call() throws Exception {
        List<Future<?>> futures = new ArrayList<Future<?>>();
        Date startTime = new Date();
        while ((new Date().getTime() - startTime.getTime()) < 1000 ){
            if (snapshots.size() > 0){
                Future future = executorService.submit(new CalculateDistance(snapshots,distance));
                futures.add(future);
                Thread.sleep( 1000 /SettingsData.getInstance().getFps());
            }
        }
        for(Future<?> future : futures) {
            try {
                future.get(); // get will block until the future is done
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return CalculatorUtils.getMedian(distance);
    }
}
