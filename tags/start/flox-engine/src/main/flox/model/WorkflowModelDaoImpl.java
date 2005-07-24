package flox.model;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.util.PropertiesHelper;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.Criterion;

import java.util.List;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 10:02:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorkflowModelDaoImpl
        extends DaoBase implements WorkflowModelDao
{
    public WorkflowModelDaoImpl()
    {

    }

    public void save(WorkflowModel model)
    {
        super.save( model );
    }

    public WorkflowModel get(Long id)
            throws NoSuchModelObjectException
    {
        return (WorkflowModel) get( WorkflowModel.class, id );
    }
    
    public WorkflowModel get(String processName,
                             Class flowedObjectClass,
                             Criterion flowedObjectCriterion)
            throws NoSuchModelObjectException
    {
        try
        {
            Criteria criteria = createCriteria( flowedObjectClass );
            criteria.add( flowedObjectCriterion );

            criteria.setMaxResults( 1 );

            List matchingFlowedObjects = criteria.list();

            if ( matchingFlowedObjects.isEmpty() )
            {
                throw new NoSuchModelObjectException( Object.class,
                                                      criteria );
            }

            Object flowedObject = matchingFlowedObjects.get( 0 );

            return (WorkflowModel) get(WorkflowModel.class,
                                       Expression.and( Expression.eq( "processName",
                                                                      processName ),
                                                       Expression.eq( "flowedObject",
                                                                      flowedObject ) ) );
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
            throw convertHibernateAccessException( e );
        }
    }

    public List getAll(String processName)
    {
        Criteria criteria = createCriteria( WorkflowModel.class );

        criteria.add( Expression.eq( "processName",
                                     processName ) );

        try
        {
            return criteria.list();
        }
        catch (HibernateException e)
        {
            throw convertHibernateAccessException( e );
        }
    }
}