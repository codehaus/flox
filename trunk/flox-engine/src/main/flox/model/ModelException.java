package flox.model;

import flox.FloxException;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 10:44:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class ModelException
        extends FloxException
{
    private static final long serialVersionUID = 3257291344103027250L;

    public ModelException()
    {

    }

    public ModelException(Throwable cause)
    {
        super( cause );
    }
}
