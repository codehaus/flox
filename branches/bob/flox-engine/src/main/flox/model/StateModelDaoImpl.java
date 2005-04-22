package flox.model;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.Criterion;
import net.sf.hibernate.expression.Order;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 10:01:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class StateModelDaoImpl
        extends DaoBase implements StateModelDao
{
    public StateModelDaoImpl()
    {

    }

    public void save(StateModel model)
    {
        super.save( model );
    }

    public StateModel get(Long id)
            throws NoSuchModelObjectException
    {
        return (StateModel) get( StateModel.class, id );
    }

    public StateModel getCurrentState(WorkflowModel workflow)
            throws NoSuchModelObjectException
    {
        return (StateModel) get( StateModel.class,
                                 Expression.and( Expression.and( Expression.isNotNull( "enteredDate" ),
                                                                 Expression.isNull( "exitedDate" ) ),
                                                 Expression.eq( "workflow", workflow ) ) );
    }

    public List<StateModel> getStateSequence(WorkflowModel workflow)
    {
        return (List<StateModel>) getAll( StateModel.class,
                                          Expression.eq( "workflow", workflow ),
                                          Order.asc( "enteredDate" ) );
    }
}
