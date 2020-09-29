package sample.InSituStress.Model;

import DataGetting.Values;

import java.util.concurrent.BlockingQueue;

public interface ModelLayer {
    BlockingQueue<Values> getValuesQueue() throws InterruptedException;
    void stop();
}
