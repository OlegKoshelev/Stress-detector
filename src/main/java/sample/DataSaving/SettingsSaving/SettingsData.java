package sample.DataSaving.SettingsSaving;


import sample.InitialDataSetting.Camera.Resolution;
import sample.InitialDataSetting.Graph.GraphType;
import sample.InitialDataSetting.Substrate.BiaxialModulus;
import javafx.scene.paint.Color;

public class SettingsData {
    private static SettingsData instance;
    private int hueMin = 0;
    private int hueMax = 200;
    private int saturationMin = 0;
    private int saturationMax = 70;
    private int valueMin = 220;
    private  int valueMax = 255;
    private Resolution resolution = Resolution.FHD;
    private int fps = 30;
    private int cameraId = 0;
    private BiaxialModulus biaxialModulus = BiaxialModulus.Al2O3;
    private int thickness = 430;
    private int rotationTime = 1700;
    private int additionalBiaxialModule = 0;
    private GraphType type = GraphType.StressThickness;
    private Color line = Color.GREEN;
    private Color grid = Color.BLACK;
    private double distance = 0.5;
    private int angle = 70;

    private SettingsData() {
    }

    public static SettingsData getInstance(){
        if (instance == null) {
            instance = new SettingsData();
        }
        return instance;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public BiaxialModulus getBiaxialModulus() {
        return biaxialModulus;
    }

    public void setBiaxialModulus(BiaxialModulus biaxialModulus) {
        this.biaxialModulus = biaxialModulus;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public int getRotationTime() {
        return rotationTime;
    }

    public void setRotationTime(int rotationTime) {
        this.rotationTime = rotationTime;
    }

    public int getAdditionalBiaxialModule() {
        return additionalBiaxialModule;
    }

    public void setAdditionalBiaxialModule(int additionalBiaxialModule) {
        this.additionalBiaxialModule = additionalBiaxialModule;
    }

    public GraphType getType() {
        return type;
    }

    public void setType(GraphType type) {
        this.type = type;
    }

    public Color getLine() {
        return line;
    }

    public void setLine(Color line) {
        this.line = line;
    }

    public Color getGrid() {
        return grid;
    }

    public void setGrid(Color grid) {
        this.grid = grid;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }
}
