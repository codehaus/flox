package flox.def;

import flox.FloxException;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 15, 2005
 * Time: 11:05:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefinitionException
        extends FloxException
{
    private static final long serialVersionUID = 3760559789121287223L;

    public DefinitionException()
    {

    }

    public DefinitionException(Throwable cause)
    {
        super( cause );
    }
}
