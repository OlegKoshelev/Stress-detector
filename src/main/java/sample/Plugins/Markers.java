package sample.Plugins;




import de.gsi.chart.plugins.AbstractDataFormattingPlugin;


import javafx.geometry.Orientation;
import javafx.scene.Node;



public class Markers extends AbstractDataFormattingPlugin{

    MyXValueIndicator xValueIndicator1;
    MyXValueIndicator xValueIndicator2;



    public Markers() {

        chartProperty().addListener((obs, oldChart, newChart) -> {

            if (newChart != null) {

                newChart.addListener(observable ->
                {
                    if (xValueIndicator1 == null && newChart.getFirstAxis(Orientation.HORIZONTAL).getTickMarks().size() > 0){
                        xValueIndicator1 = new MyXValueIndicator(getChart().getFirstAxis(Orientation.HORIZONTAL),getChart().getFirstAxis(Orientation.HORIZONTAL).getMin() + 1,"1");
                        xValueIndicator2 = new MyXValueIndicator(getChart().getFirstAxis(Orientation.HORIZONTAL),getChart().getFirstAxis(Orientation.HORIZONTAL).getMax() - 1,"2");
                        xValueIndicator1.updateStyleClass();
                        xValueIndicator2.updateStyleClass();

                        getChart().getPlugins().addAll(xValueIndicator1,xValueIndicator2);
                    }

                    if (xValueIndicator1 != null && xValueIndicator2 != null){

                        if (xValueIndicator1.getValue() < getChart().getFirstAxis(Orientation.HORIZONTAL).getMin())
                            xValueIndicator1.setValue(getChart().getFirstAxis(Orientation.HORIZONTAL).getMin() + 1);

                        if (xValueIndicator1.getValue() > getChart().getFirstAxis(Orientation.HORIZONTAL).getMax())
                            xValueIndicator1.setValue(getChart().getFirstAxis(Orientation.HORIZONTAL).getMax() - 1);

                        if (xValueIndicator2.getValue() < getChart().getFirstAxis(Orientation.HORIZONTAL).getMin())
                            xValueIndicator2.setValue(getChart().getFirstAxis(Orientation.HORIZONTAL).getMin() + 1);

                        if (xValueIndicator2.getValue() > getChart().getFirstAxis(Orientation.HORIZONTAL).getMax())
                            xValueIndicator2.setValue(getChart().getFirstAxis(Orientation.HORIZONTAL).getMax() - 1);

                    }
                }
                );
            }
        });
    }

    protected void addChildNodeIfNotPresent(final Node node) {
        if (!getChartChildren().contains(node)) {
            getChartChildren().add(node);
        }
    }

}
