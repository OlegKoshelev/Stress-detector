package sample.DataGetting.Tasks;

import de.gsi.dataset.spi.DefaultErrorDataSet;
import javafx.scene.image.ImageView;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import sample.DataBase.Entities.AveragingTable;
import sample.DataBase.Entities.DetailedTable;
import sample.DataBase.HibernateUtil;
import sample.DataBase.TableHelper;
import sample.DataGetting.Snapshot;
import sample.DataGetting.Values;
import sample.DataSaving.SettingsSaving.SettingsData;
import sample.Utils.TemporaryValues;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReadFromCamera implements Runnable {
    private DefaultErrorDataSet dataSet;
    private ImageView imageView;
    private ExecutorService executorService;
    private BlockingQueue<Snapshot> snapshots;
    private CopyOnWriteArrayList<Double> distances;
    private boolean read;
    private VideoCapture camera;
    private BlockingQueue<Values> values;
    private List<Values> bufferForAveraging;
    private boolean bufferIsFull = false;
    private Lock dataSetLock;
    private Lock bufferLock;
    private TemporaryValues<DetailedTable> detailedTableValues;
    private TemporaryValues<AveragingTable> averageTableValues;
    private HibernateUtil hibernateUtil;
    private List<Future<?>> futures = new ArrayList<Future<?>>();
    private Date startTime;
    private int dotsAmount = 100;
    private double d0;

    static {
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }





    public ReadFromCamera(DefaultErrorDataSet dataSet, ImageView imageView, double d0, HibernateUtil hibernateUtil,
                          TemporaryValues<DetailedTable> detailedTableValues, TemporaryValues<AveragingTable> averageTableValues) throws Exception {

        this.dataSet = dataSet;
        this.imageView = imageView;
        snapshots = new LinkedBlockingQueue<>();
        values = new LinkedBlockingQueue<>();
        dataSetLock = new ReentrantLock();
        bufferLock = new ReentrantLock();
        bufferForAveraging = new ArrayList<>();
        this.executorService = Executors.newCachedThreadPool();
        this.detailedTableValues = detailedTableValues;
        this.averageTableValues = averageTableValues;
        this.d0 = d0;
        this.hibernateUtil = hibernateUtil;
        this.distances = new CopyOnWriteArrayList<>();;
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

    public void Stop() {
        read = false;
    }

    @Override
    public void run() {
        try {
            // Считываем кадры
            Mat frame = new Mat();
            while (read) {
                if (camera.read(frame)) {
                    if (startTime == null) {
                        startTime = new Date();
                    }
                    snapshots.put(new Snapshot(frame, new Date()));
                    selectTask();
                } else {
                    throw new Exception("Не удалось захватить кадр");
                }
                try {
                    Thread.sleep(1000 / SettingsData.getInstance().getFps()); // ? кадров в секунду
                } catch (InterruptedException e) {
                    return;
                }
            }
        } catch (Exception e) {
            return;
        } finally {
            camera.release();
            read = false;
        }
    }

    private void selectTask() throws ExecutionException, InterruptedException {
        if (d0 == 0) {
           d0 = getInitialDistance();
            return;
        }
        if (!bufferIsFull) {
            fillBuffer();
        } else {
            getValues();
        }

    }

    private double getInitialDistance() throws ExecutionException, InterruptedException {
        if (new Date().getTime() - startTime.getTime() < 1000) {
            Future initialDistance = executorService.submit(new CalculateDistance(snapshots, distances));
            futures.add(initialDistance);
            return 0;
        } else {
            for (Future<?> future : futures) {
                future.get();
            }
            Future<Double> startDistance = executorService.submit(new CalculateInitialDistance(distances,hibernateUtil));
            return startDistance.get();
        }
    }

    private void fillBuffer() throws ExecutionException, InterruptedException {
        if (futures.size() <= dotsAmount) {
            Future future = executorService.submit(new AddToBuffer(bufferForAveraging, snapshots, d0, bufferLock, detailedTableValues));
            futures.add(future);
        } else {
            for (Future<?> future : futures) {
                future.get();
            }
            bufferIsFull = true;
        }
    }

    private void getValues (){
        if (averageTableValues.size() >= 1000){
            bufferLock.lock();
            dataSetLock.lock();
            System.out.println("> 1000");
            // заносим данные в подробную таблицу
            TableHelper<DetailedTable> detailedTableHelper = new TableHelper<>(hibernateUtil,DetailedTable.class);
            detailedTableHelper.addTableList(detailedTableValues.getList());
            detailedTableValues.reset();
            // заносим данные в усредненную таблицу
            TableHelper<AveragingTable> averageTableHelper = new TableHelper<>(hibernateUtil,AveragingTable.class);
            averageTableHelper.addTableList(averageTableValues.getList());
            averageTableValues.reset();
            dataSetLock.unlock();
            bufferLock.unlock();
        }
        executorService.submit(new CalculateValues(dataSet,imageView,snapshots,values,bufferForAveraging,d0,dataSetLock,bufferLock,detailedTableValues, averageTableValues,hibernateUtil));
    }
}

