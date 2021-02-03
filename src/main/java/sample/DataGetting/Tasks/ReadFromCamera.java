package sample.DataGetting.Tasks;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import sample.DataGetting.Snapshot;
import sample.DataSaving.SettingsSaving.SettingsData;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

public class ReadFromCamera implements Runnable {
    private BlockingQueue<Snapshot> snapshots;
    private boolean read;
    private VideoCapture camera;

    static {
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }



    public ReadFromCamera(BlockingQueue<Snapshot> snapshots) throws Exception {
        this.snapshots = snapshots;
        read = true;
        // Подключаемся к камере
        camera = new VideoCapture(SettingsData.getInstance().getCameraId());
        if (!camera.isOpened()) throw new Exception("Не удалось подключить камеру.");


        // Задаем размеры кадра
        camera.set(Videoio.CAP_PROP_FRAME_WIDTH, SettingsData.getInstance().getResolution().getWidth());
        camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, SettingsData.getInstance().getResolution().getHeight());
        System.out.println(SettingsData.getInstance().getResolution().getWidth());
        System.out.println(SettingsData.getInstance().getResolution().getHeight());

    }

    public void Stop(){
        read = false;
    }

    @Override
    public void run() {


        try {
            // Считываем кадры
            Mat frame = new Mat();


            while ( read ) {
                if (camera.read(frame)) {
                    snapshots.put(new Snapshot(frame, new Date()));
                    try {
                        Thread.sleep(1000/ SettingsData.getInstance().getFps()); // ? кадров в секунду
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                else {
                    throw new Exception("Не удалось захватить кадр");
                }
            }
        } catch (Exception e) {
            return;
        } finally {
            camera.release();
            read = false;
        }
    }
}

