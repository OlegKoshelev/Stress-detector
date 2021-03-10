package sample.DataBase.Entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BaseTable.class)
public abstract class BaseTable_ {

	public static volatile SingularAttribute<BaseTable, Double> distance;
	public static volatile SingularAttribute<BaseTable, Double> size1;
	public static volatile SingularAttribute<BaseTable, Double> size2;
	public static volatile SingularAttribute<BaseTable, Double> y1;
	public static volatile SingularAttribute<BaseTable, Double> x1;
	public static volatile SingularAttribute<BaseTable, Double> y2;
	public static volatile SingularAttribute<BaseTable, Double> x2;
	public static volatile SingularAttribute<BaseTable, Double> stressThickness;
	public static volatile SingularAttribute<BaseTable, Long> timestamp;
	public static volatile SingularAttribute<BaseTable, Double> curvature;

	public static final String DISTANCE = "distance";
	public static final String SIZE1 = "size1";
	public static final String SIZE2 = "size2";
	public static final String Y1 = "y1";
	public static final String X1 = "x1";
	public static final String Y2 = "y2";
	public static final String X2 = "x2";
	public static final String STRESS_THICKNESS = "stressThickness";
	public static final String TIMESTAMP = "timestamp";
	public static final String CURVATURE = "curvature";

}

