package sample.DataBase.Entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BaseTable.class)
public abstract class BaseTable_ {

	public static volatile SingularAttribute<BaseTable, Double> distance;
	public static volatile SingularAttribute<BaseTable, Double> stressThickness;
	public static volatile SingularAttribute<BaseTable, Long> timestamp;
	public static volatile SingularAttribute<BaseTable, Double> curvature;

	public static final String DISTANCE = "distance";
	public static final String STRESS_THICKNESS = "stressThickness";
	public static final String TIMESTAMP = "timestamp";
	public static final String CURVATURE = "curvature";

}

