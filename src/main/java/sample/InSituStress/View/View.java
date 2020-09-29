package sample.InSituStress.View;

import DataGetting.Values;

import java.util.concurrent.BlockingQueue;

public interface View {
        void showValuesQueue(BlockingQueue<Values> values);
        void stop();
}
