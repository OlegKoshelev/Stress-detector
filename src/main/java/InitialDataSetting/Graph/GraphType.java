package InitialDataSetting.Graph;

public enum GraphType {
    StressThickness("Stress X Thickness (GPa X um)"),
    Curvature("Curvature (1/km)"),
    Distance("Distance (pixels)");

    private String name;

    GraphType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
