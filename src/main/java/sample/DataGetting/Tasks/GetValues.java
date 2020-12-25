package sample.DataGetting.Tasks;

import de.gsi.dataset.spi.DefaultErrorDataSet;
import javafx.scene.image.ImageView;
import sample.DataGetting.Snapshot;
import sample.DataGetting.Values;

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

    public GetValues(DefaultErrorDataSet dataSet, ImageView imageView, BlockingQueue<Snapshot> snapshots, BlockingQueue<Values> values,
                     double d0,List<Values> bufferForAveraging, ExecutorService executorService, Lock dataSetLock, Lock bufferLock) {
        this.imageView = imageView;
        this.dataSet = dataSet;
        this.snapshots = snapshots;
        this.values = values;
        this.dataSetLock = dataSetLock;
        this.d0 = d0;
        this.executorService = executorService;
        this.bufferForAveraging = bufferForAveraging;
        this.bufferLock = bufferLock;
    }

    @Override
    public void run() {

        while (true){
            if (snapshots.size() > 0){
                executorService.submit(new CalculateValues(dataSet,imageView,snapshots,values,bufferForAveraging,d0,dataSetLock,bufferLock));
                try {
                    Thread.sleep ( 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
