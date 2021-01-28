package sample.DataSaving.SettingsSaving.DynamicSettings;


import sample.InitialDataSetting.Substrate.BiaxialModulus;

public class SubstrateCustomizations {
    private static SubstrateCustomizations instance;
    private BiaxialModulus biaxialModulus;
    private int thickness;
    private int rotationTime;
    private int additionalBiaxialModule;

    private SubstrateCustomizations() {
    }

    public static SubstrateCustomizations getInstance() {
        if (instance == null) {
            instance = new SubstrateCustomizations();
        }
        return instance;
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
}
