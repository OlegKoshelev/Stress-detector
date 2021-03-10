package sample.DataGetting.Tasks;

import sample.DataBase.Entities.DetailedTable;
import sample.DataBase.HibernateUtil;
import sample.DataBase.TableHelper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SaveAsDetailedTable implements Runnable{
    private double d0;
    private HibernateUtil hibernateUtil;
    private String absolutePath;
    private Future future;


    public SaveAsDetailedTable(double d0, HibernateUtil hibernateUtil, String absolutePath,Future future) {
        this.d0 = d0;
        this.hibernateUtil = hibernateUtil;
        this.absolutePath = absolutePath;
        this.future = future;
    }

    @Override
    public void run() {
        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        TableHelper<DetailedTable> detailedTableHelper = new TableHelper<>(hibernateUtil, DetailedTable.class);
        detailedTableHelper.tableToTxt(absolutePath, d0);
    }
}
