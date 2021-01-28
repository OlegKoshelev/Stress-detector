package sample.AdditionalUtils;

import org.hibernate.Session;
import sample.DataBase.Entities.BaseTable;
import sample.DataBase.Entities.BaseTable_;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

public class TableHelperUtils {
    public static Object getMaxBoundaryValue(Session session, CriteriaBuilder cb, CriteriaQuery cq, Root<BaseTable> root, Class <? extends Object> resultClass, SingularAttribute singularAttribute ){
        cb.construct(resultClass, root.get( singularAttribute));
        cq.select(cb.greatest(root.get(singularAttribute)));
        Query query = session.createQuery(cq);
        return query.getSingleResult();
    }
    public static Object getMinBoundaryValue(Session session, CriteriaBuilder cb, CriteriaQuery cq, Root<BaseTable> root, Class <? extends Object> resultClass, SingularAttribute singularAttribute){
        cb.construct(resultClass, root.get( singularAttribute));
        cq.select(cb.least(root.get(singularAttribute)));
        Query query = session.createQuery(cq);
        return query.getSingleResult();
    }
}
