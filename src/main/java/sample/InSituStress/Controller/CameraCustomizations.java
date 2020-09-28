package sample.InSituStress.Controller;

public class CameraCustomizations {
    private static CameraCustomizations instance;
    private Resolution resolution;
    private int fps;
    private int cameraId;
    private int hueMin = 0;
    private int hueMax = 200;
    private int saturationMin = 0;
    private int saturationMax = 70;
    private int valueMin = 220;
    private  int valueMax = 255;

    private CameraCustomizations() {
    }

    public static CameraCustomizations getInstance() {
        if (instance == null) {
            instance = new CameraCustomizations();
        }
        return instance;
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public int getHueMin() {
        return hueMin;
    }

    public void setHueMin(int hueMin) {
        this.hueMin = hueMin;
    }

    public int getHueMax() {
        return hueMax;
    }

    public void setHueMax(int hueMax) {
        this.hueMax = hueMax;
    }

    public int getSaturationMin() {
        return saturationMin;
    }

    public void setSaturationMin(int saturationMin) {
        this.saturationMin = saturationMin;
    }

    public int getSaturationMax() {
        return saturationMax;
    }

    public void setSaturationMax(int saturationMax) {
        this.saturationMax = saturationMax;
    }

    public int getValueMin() {
        return valueMin;
    }

    public void setValueMin(int valueMin) {
        this.valueMin = valueMin;
    }

    public int getValueMax() {
        return valueMax;
    }

    public void setValueMax(int valueMax) {
        this.valueMax = valueMax;
    }
}
