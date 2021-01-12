package sample.InitialDataSetting.Graph;

public enum GraphType {
    StressThickness("Stress X Thickness (GPa X µm)"),
    Curvature("Curvature (1/m)"),
    Distance("Distance (pixels)");

    private String name;

    GraphType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
