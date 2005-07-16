package flox.model;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;

import flox.def.Process;
import flox.def.State;

import java.util.Iterator;
import java.util.List;

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
    
    public WorkflowModel get(String processHandle,
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
                                       Expression.and( Expression.eq( "processHandle",
                                                                      processHandle ),
                                                       Expression.eq( "flowedObject",
                                                                      flowedObject ) ) );
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
            throw convertHibernateAccessException( e );
        }
    }
    
    public WorkflowModel get(Process process, Object flowedObject)  throws NoSuchModelObjectException
    {
        String processHandle = process.getProcessHandle().getHandle();
        
        try
        {
            return (WorkflowModel) get(WorkflowModel.class,
                                       Expression.and( Expression.eq( "processHandle",
                                                                      processHandle ),
                                                       Expression.eq( "flowedObject",
                                                                      flowedObject ) ) );
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
            throw convertHibernateAccessException( e );
        }
    }
    

    public List getAll(Process process, State currentState)
    {
        Criteria criteria = createCriteria( WorkflowModel.class );
        
        String processHandle = process.getProcessHandle().getHandle();

        criteria.add( Expression.eq( "processHandle", processHandle ) );
        
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

    public List getAll(Process process)
    {
        Criteria criteria = createCriteria( WorkflowModel.class );
        
        String processHandle = process.getProcessHandle().getHandle();

        criteria.add( Expression.eq( "processHandle",
                                     processHandle ) );

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
    
    public Criteria getCriteria( String processHandle)
    {
        Criteria criteria = createCriteria( WorkflowModel.class );
        
        criteria.add( Expression.eq( "processHandle", processHandle ) );
        
        return criteria;
    }
    
    public Criteria getCriteria( String processHandle, String currentState )
    {
        Criteria criteria = getCriteria( processHandle );
        
        if ( currentState == null )
        {
            return criteria;
        }
        
        criteria.createCriteria( "currentState" )
            .add( Expression.eq( "name", currentState ) );
        
        return criteria;
    }
}
