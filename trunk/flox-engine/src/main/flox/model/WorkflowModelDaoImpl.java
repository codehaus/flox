package flox.model;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.util.PropertiesHelper;

import flox.def.State;

import java.util.Iterator;
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
        extends DaoBase implements WorkflowModelDao, PageableDao
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
    
    public WorkflowModel get(String processName, Object flowedObject)  throws NoSuchModelObjectException
    {
        try
        {
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
    

    public List getAll(String processName, State currentState)
    {
        Criteria criteria = createCriteria( WorkflowModel.class );

        criteria.add( Expression.eq( "processName", processName ) );
        
        criteria.createCriteria( "currentState" )
            .add( Expression.eq( "name", currentState.getName() ) );
        
        try
        {
            return criteria.list();
        }
        catch (HibernateException e)
        {
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

    public Iterator getCurrentPageRows( int first, int pageSize, String sortColumn, boolean sortOrder, Criteria criteria )
    {
        return getCurrentPageRows( WorkflowModel.class, first, pageSize, sortColumn, sortOrder, criteria );
    }
    
    public int countAll(Criteria criteria)
    {
        return countAll( WorkflowModel.class, criteria );
    }
    
    public Criteria getCriteria( String processName)
    {
        Criteria criteria = createCriteria( WorkflowModel.class );
        
        criteria.add( Expression.eq( "processName", processName ) );
        
        return criteria;
    }
    
    public Criteria getCriteria( String processName, String currentState )
    {
        Criteria criteria = getCriteria( processName );
        
        if ( currentState == null )
        {
            return criteria;
        }
        
        criteria.createCriteria( "currentState" )
            .add( Expression.eq( "name", currentState ) );
        
        return criteria;
    }
}
