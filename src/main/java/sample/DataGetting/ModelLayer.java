package sample.DataGetting;

import java.util.concurrent.BlockingQueue;

public interface ModelLayer {
    BlockingQueue<Values> getValuesQueue() throws InterruptedException;
    void stop();
}
