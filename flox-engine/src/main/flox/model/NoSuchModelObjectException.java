package flox.model;

import net.sf.hibernate.Criteria;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 10:43:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class NoSuchModelObjectException
        extends ModelException
{
    private Class dataClass;
    private Criteria criteria;

    public NoSuchModelObjectException(Class dataClass,
                                      Criteria criteria)
    {
        this.dataClass = dataClass;
        this.criteria  = criteria;
    }

    public Class getDataClass()
    {
        return dataClass;
    }

    public Criteria getCriteria()
    {
        return criteria;
    }
}
