package flox;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 10:17:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class RuntimeFloxException
        extends RuntimeException
{
    private static final long serialVersionUID = 3978143249032886324L;

    public RuntimeFloxException()
    {

    }

    public RuntimeFloxException(Throwable cause)
    {
        super( cause );
    }
}
