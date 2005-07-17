package flox.def;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 15, 2005
 * Time: 11:09:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class DuplicateTransitionException
        extends DefinitionException
{
    private State state;
    private String name;
    private State destination;

    public DuplicateTransitionException(State state,
                                        String name,
                                        State destination)
    {
        this.state       = state;
        this.name        = name;
        this.destination = destination;
    }

    public State getState()
    {
        return state;
    }

    public String getName()
    {
        return name;
    }

    public State getDestination()
    {
        return destination;
    }

    public String getMessage()
    {
        return "Duplicate transition '" + getName() + "' for state '" + getState().getName() + "'";
    }
}
