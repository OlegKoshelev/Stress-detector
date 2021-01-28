package sample.DataGetting.Tasks;

import de.gsi.dataset.spi.DefaultErrorDataSet;
import sample.DataGetting.Values;

import java.util.concurrent.BlockingQueue;

public class addValuesToChart implements Runnable {
    private DefaultErrorDataSet dataSet;
    private BlockingQueue<Values> values;

    public addValuesToChart(DefaultErrorDataSet dataSet, BlockingQueue<Values> values) {
        this.dataSet = dataSet;
        this.values = values;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (values.size() == 0) continue;
                Values value = values.take();
                dataSet.add(value.getTimestamp().getTime(),value.getDistance());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
