package sample.InSituStress.View;

import javafx.stage.Stage;
import sample.InSituStress.Model.GettingData.CvUtils;
import sample.InSituStress.Model.GettingData.Values;

import java.util.concurrent.BlockingQueue;

public class ConsoleView implements View {
    private boolean stop = false;
    private Thread thread;

    @Override
    public void showValuesQueue(BlockingQueue<Values> values) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stop) {
                    Values nextValue = null;
                    try {
                        nextValue = values.take();
                        if (nextValue == null) continue;
                        System.out.println(String.format("Time: %s;  StressThickness: %f; Curvature: %f; distance: %f", nextValue.getTimestamp().toString(), nextValue.getStressThickness(), nextValue.getCurvature(),nextValue.getDistance()));
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                System.out.println("View is closed");
            }
        });
        thread.start();
    }

    @Override
    public void stop() {
        thread.interrupt();
    }


}
