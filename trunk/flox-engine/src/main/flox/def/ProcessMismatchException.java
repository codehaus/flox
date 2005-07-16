package flox.def;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 15, 2005
 * Time: 11:12:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessMismatchException
        extends DefinitionException
{
    private static final long serialVersionUID = 3905240126224610872L;
    
    private State origin;
    private State destination;

    public ProcessMismatchException(State origin,
                                    State destination)
    {
        this.origin      = origin;
        this.destination = destination;
    }

    public State getOrigin()
    {
        return origin;
    }

    public State getDestination()
    {
        return destination;
    }

    public String getMessage()
    {
        return "Process mismatch between state '" + getOrigin().getName() + "' and '" + getDestination().getName() + "'";
    }
}
