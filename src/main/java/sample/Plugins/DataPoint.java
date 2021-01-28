package sample.Plugins;

import de.gsi.chart.plugins.DataPointTooltip;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataPoint  extends DataPointTooltip {


    @Override
    protected String formatLabel(DataPoint dataPoint) {
        return String.format("%s", formatDataPoint(dataPoint));
    }

    private static String formatDataPoint(final DataPoint dataPoint) {
        Date date = new Date((long) dataPoint.getX());
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SS");
        return String.format("DataPoint (%s; %.3f)", dateFormat.format(date), dataPoint.getY());
    }
}
