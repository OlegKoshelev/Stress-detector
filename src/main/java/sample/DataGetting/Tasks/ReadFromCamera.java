package sample.DataGetting.Tasks;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import sample.DataGetting.Spots;
import sample.DataSaving.SettingsSaving.SettingsData;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

public class ReadFromCamera implements Runnable {
    private SettingsData settingsData = SettingsData.getInstance();
    static {
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private BlockingQueue<Spots> inputQueue;
    private boolean read;
    private VideoCapture camera;

    public ReadFromCamera(BlockingQueue<Spots> inputQueue) throws Exception {
        this.inputQueue = inputQueue;
        read = true;
        // Подключаемся к камере
        camera = new VideoCapture(settingsData.getCameraId());
        if (!camera.isOpened()) throw new Exception("Не удалось подключить камеру.");

        // Задаем размеры кадра

        camera.set(Videoio.CAP_PROP_FRAME_WIDTH, settingsData.getResolution().getWidth());
        camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, settingsData.getResolution().getHeight());
        System.out.println(settingsData.getResolution().getWidth());
        System.out.println(settingsData.getResolution().getHeight());
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
                    System.out.println(frame.empty());
                    inputQueue.put(new Spots(frame, new Date()));
                    System.out.println(inputQueue.size() + "размер очереди");
                    System.out.println("изображенеи захвачено" );
                    try {
                        Thread.sleep(1000/ settingsData.getFps()); // 10 кадров в секунду
                    } catch (InterruptedException e) {}
                }
                else {
                    throw new Exception("Не удалось захватить кадр");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            camera.release();
            read = false;
        }
    }
}

