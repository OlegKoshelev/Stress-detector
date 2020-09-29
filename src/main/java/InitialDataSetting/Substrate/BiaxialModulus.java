package InitialDataSetting.Substrate;

public enum BiaxialModulus {

    AlN(480),
    GaN (430),
    InN(270),
    Al2O3(602),
    SiC(602),
    Si(176);

    private int module;

    BiaxialModulus(int module) {
        this.module = module;
    }

    public int getModule() {
        return module;
    }

    public String getName(){
        return String.format("%s (%d GPa)",name(),module);
    }
}
