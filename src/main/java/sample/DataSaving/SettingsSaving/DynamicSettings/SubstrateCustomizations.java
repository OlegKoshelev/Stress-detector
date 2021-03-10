package sample.DataSaving.SettingsSaving.DynamicSettings;


import sample.InitialDataSetting.Substrate.BiaxialModulus;

public class SubstrateCustomizations {
    private BiaxialModulus biaxialModulus = BiaxialModulus.Al2O3;
    private int thickness = 403;
    private int rotationTime = 1800;
    private int additionalBiaxialModule = 0;

    public SubstrateCustomizations() {
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
