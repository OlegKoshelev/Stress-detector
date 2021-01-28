package sample.DataSaving.SettingsSaving.StaticSettings;



public class SetupCustomizations {
    private static SetupCustomizations instance;
    private double distance;
    private int angle;

    private SetupCustomizations(){}

    public static SetupCustomizations getInstance() {
        if (instance == null) {
            instance = new SetupCustomizations();
        }
        return instance;
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
