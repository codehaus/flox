package flox.model;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 10:37:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class DaoBase
        extends HibernateDaoSupport 
{
    public DaoBase()
    {

    }

    protected void save(Object object)
    {
        try
        {
            getSession().saveOrUpdate( object );
        }
        catch (HibernateException e)
        {
            throw convertHibernateAccessException( e );
        }
    }

    protected Object get(Class t, Long id)
            throws NoSuchModelObjectException
    {
        Criteria criteria = createCriteria( t );

        criteria.add( Expression.eq( "id",
                                     id ) );

        criteria.setMaxResults( 1 );

        try
        {
            List<?> results = criteria.list();
            
            if ( results.isEmpty() )
            {
                throw new NoSuchModelObjectException( t,
                                                      criteria );
            }

            return results.get( 0 );
        }
        catch (HibernateException e)
        {
            throw convertHibernateAccessException( e );
        }
    }

    protected Object get(Class t,
                         Criterion criterion)
            throws NoSuchModelObjectException
    {
        Criteria criteria = createCriteria( t );

        criteria.add( criterion );

        return get(  t,
                     criteria );
    }
    
    protected List getAll(Class t,
                          Criterion criterion,
                          Order order)
    {
        Criteria criteria = createCriteria( t );
        criteria.add( criterion );
        criteria.addOrder( order );

        return getAll( t,
                       criteria );
    }

    private Object get(Class t,
                       Criteria criteria)
            throws NoSuchModelObjectException
    {
        criteria.setMaxResults( 1 );

        try
        {
            List<?> results = criteria.list();

            if ( results.isEmpty() )
            {
                throw new NoSuchModelObjectException( t,
                                                      criteria );
            }

            return results.get( 0 );
        }
        catch (HibernateException e)
        {
            throw convertHibernateAccessException( e );
        }

    }
    
    private List getAll(Class t,
                        Criteria criteria)
    {
        try
        {
            return criteria.list();
        }
        catch (HibernateException e)
        {
            throw convertHibernateAccessException( e );
        }
    }


    protected Criteria createCriteria(Class dataClass)
    {
        return getSession().createCriteria( dataClass );
    }

    public Iterator getCurrentPageRows(Class t, int first, int pageSize, String sortColumn, boolean sortOrder, Criteria criteria)
    {
        if ( criteria == null )
        {
            criteria = createCriteria( t );
        }
        
        criteria.setFetchSize( pageSize );
        criteria.setMaxResults( pageSize );
        criteria.setFirstResult( first );
        
        return criteria.list().iterator();
    }
    
    public int countAll(Class t, Criteria criteria)
    {
        if ( criteria == null )
        {
            criteria = createCriteria( t );
        }
        return criteria.list().size();
    }
}
