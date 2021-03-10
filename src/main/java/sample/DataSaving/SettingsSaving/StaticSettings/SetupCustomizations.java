package sample.DataSaving.SettingsSaving.StaticSettings;



public class SetupCustomizations {
    private double distance = 0.5;
    private int angle = 70;
    private double d0 = 0;


    public SetupCustomizations() {
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

    public double getD0() {
        return d0;
    }

    public void setD0(double d0) {
        this.d0 = d0;
    }
}
