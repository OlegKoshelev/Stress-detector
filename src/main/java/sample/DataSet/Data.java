package sample.DataSet;




import de.gsi.dataset.spi.DefaultErrorDataSet;

public class Data {
    private DefaultErrorDataSet dataSet;

    public Data(String name) {
        dataSet = new DefaultErrorDataSet(name);
    }

    public void addData(long x,double y){
        dataSet.add(x,y);

    }

    public DefaultErrorDataSet getDataSet (){
        System.out.println("CAPACITY-----" + dataSet.getXValues().length);
        DefaultErrorDataSet dataSetClone = new DefaultErrorDataSet(dataSet.getName());
        for (int i = 0; i < dataSet.getCapacity(); i++){
            dataSetClone.add(dataSet.getX(i),dataSet.getY(i));
        }
        return dataSetClone;
    }

    public  double [] xValues(){
        double [] result = dataSet.getXValues();
        return result;
    }
    public  double [] yValues(){
        double [] result = dataSet.getYValues();
        return result;
    }
}
