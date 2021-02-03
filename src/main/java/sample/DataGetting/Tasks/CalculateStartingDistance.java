package sample.DataGetting.Tasks;

import org.apache.log4j.Logger;
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
    private final Logger logger = Logger.getLogger(CalculateStartingDistance.class);


    public CalculateStartingDistance(BlockingQueue<Snapshot> snapshots, ExecutorService executorService) {
        this.distance = new CopyOnWriteArrayList<>();
        this.snapshots = snapshots;
        this.executorService = executorService;
    }

    @Override
    public Double call() throws Exception {
        logger.info("Start");
        List<Future<?>> futures = new ArrayList<Future<?>>();
        waiting();
        Date startTime = new Date();
        logger.debug(startTime);
        while ((new Date().getTime() - startTime.getTime()) < 1000 ){

            if (snapshots.size() > 0){
                logger.debug("Calculate distance");
                Future future = executorService.submit(new CalculateDistance(snapshots,distance));
                futures.add(future);
            }
            Thread.sleep(1000 / SettingsData.getInstance().getFps());
        }
        logger.debug(new Date());
        for(Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        logger.info("finish and distance size" + distance.size());
        return CalculatorUtils.getAverageDistance(distance);
    }

    private void waiting() throws InterruptedException {
        while (true){
            if (snapshots.size() > 0)
                break;
            Thread.sleep(1000 / SettingsData.getInstance().getFps());
        }
    }
}
