package sample.DataBase.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class InitialDistance {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private double d0;

    public InitialDistance() {
    }

    public InitialDistance(double d0) {
        this.d0 = d0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getD0() {
        return d0;
    }

    public void setD0(double d0) {
        this.d0 = d0;
    }
}
