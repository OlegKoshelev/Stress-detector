package sample.DataGetting.Tasks;

import sample.AdditionalUtils.CalculatorUtils;
import sample.DataBase.HibernateUtil;
import sample.DataBase.InitialDistanceHelper;

import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

public class CalculateInitialDistance implements Callable<Double> {
    private CopyOnWriteArrayList<Double> distance ;
    private HibernateUtil hibernateUtil;

    public CalculateInitialDistance(CopyOnWriteArrayList<Double> distance, HibernateUtil hibernateUtil) {
        this.distance = distance;
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public Double call() throws Exception {
        double initialDistance = CalculatorUtils.getAverageDistance(distance);
        new InitialDistanceHelper(hibernateUtil).saveDistance(initialDistance);
        return initialDistance;
    }
}
