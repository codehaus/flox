package flox.model;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;
import org.springframework.orm.hibernate.SessionFactoryUtils;
import net.sf.hibernate.Criteria;
import net.sf.hibernate.Session;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.Criterion;
import net.sf.hibernate.expression.Order;

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
            List results = criteria.list();

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
            List results = criteria.list();

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
}
