package flox;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 15, 2005
 * Time: 11:04:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class FloxException
        extends Exception
{
    private static final long serialVersionUID = 3257846571704791095L;

    public FloxException()
    {
    }

    public FloxException(Throwable cause)
    {
        super( cause );
    }
    
    public FloxException(String message)
    {
        super( message );
    }
}
