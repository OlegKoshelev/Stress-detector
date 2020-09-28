package sample.InSituStress.Model;

import sample.InSituStress.Model.GettingData.Values;

import java.util.concurrent.BlockingQueue;

public interface ModelLayer {
    BlockingQueue<Values> getValuesQueue() throws InterruptedException;
    void stop();
}
