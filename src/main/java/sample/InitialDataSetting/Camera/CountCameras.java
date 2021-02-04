package sample.InitialDataSetting.Camera;

import sample.DataSaving.SettingsSaving.DynamicSettings.CameraCustomizations;
import javafx.scene.control.MenuItem;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class CountCameras {

    private VideoCapture camera;
    private LinkedBlockingQueue<Mat> inputQueue; // image storage queue
    private Map<MenuItem, Integer> itemsMap = new HashMap<>(); // use for setCameraItems in settings
    private Thread cameraLauncher; // launch reading from camera
    private boolean read = true; // getting new images while it is true
    private boolean camWasChanged = true; // installing a new camera and closing the last camera
    private boolean resWasChanged = false; // installing a new resolution
    private CameraCustomizations cameraCustomizations = CameraCustomizations.getInstance(); // camera settings


    static {
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public CountCameras() {
        for (int device = 0; device < 10; device++) {
            camera = new VideoCapture(device);
            if (!camera.isOpened()) {
                System.out.println(device);
                return;
            }
            camera.release();
            itemsMap.put(new MenuItem("Camera " + device), device);
        }
    }

    public void setInputQueue(LinkedBlockingQueue<Mat> inputQueue) {
        this.inputQueue = inputQueue;
    }

    public Map<MenuItem, Integer> getItemsMap() {
        return itemsMap;
    }


    public Thread getCameraLauncher() {
        return cameraLauncher;
    }

    public void newCamStart() {
        camWasChanged = true;
    }

    public void newResolution() {
        resWasChanged = true;
    }

    public void stop() {
        read = false;
    }

    public void start() {
        cameraLauncher = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Считываем кадры
                    Mat frame = new Mat();
                    while (read) {
                        if (camWasChanged)
                            changeCamera();
                        if (resWasChanged)
                            changeResolution();
                        if (camera.read(frame)) {
                            inputQueue.add(frame);
                            System.out.println("захват");
                            try {
                                Thread.sleep(1000 / cameraCustomizations.getFps());
                            } catch (InterruptedException e) {
                            }
                        } else {
                            throw new Exception("Не удалось захватить кадр");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    camera.release();
                    System.out.println("camera " + cameraCustomizations.getCameraId() + " was closed");
                }
            }
        });
        cameraLauncher.start();
    }

    private void changeResolution() {
        setResolution();
        resWasChanged = false;
        System.out.println("resolution was changed");
    }

    private void changeCamera() {
        camera.release();
        System.out.println("старт " + cameraCustomizations.getCameraId());
        camera = new VideoCapture(cameraCustomizations.getCameraId());
        if (!camera.isOpened()) try {
            throw new Exception("Не удалось подключить камеру.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        setResolution();
        camWasChanged = false;
    }

    private void setResolution(){
        camera.set(Videoio.CAP_PROP_FRAME_WIDTH, cameraCustomizations.getResolution().getWidth());
        camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, cameraCustomizations.getResolution().getHeight());
    }


}
