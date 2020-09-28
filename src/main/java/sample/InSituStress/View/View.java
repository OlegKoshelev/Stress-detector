package sample.InSituStress.View;

import sample.InSituStress.Model.GettingData.Values;

import java.util.concurrent.BlockingQueue;

public interface View {
        void showValuesQueue(BlockingQueue<Values> values);
        void stop();
}
