package flox.def;

import flox.Workflow;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 15, 2005
 * Time: 10:52:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Process
{
    private String name;
    private Map states;
    private State startState;

    public Process(String name)
    {
        this.name = name;
        this.states = new HashMap();
    }

    public String getName()
    {
        return name;
    }

    public void setStartState(String name) throws NoSuchStateException
    {
        State state = getState( name );

        this.startState = state;
    }

    public State getStartState()
    {
        return startState;
    }

    public State newState(String name)
        throws DuplicateStateException
    {
        if ( this.states.containsKey( name ) )
        {
            throw new DuplicateStateException( this,
                                               name );
        }
        
        State state =  new State( this,
                                  name );
        
        this.states.put( name,
                         state );

        if ( this.startState == null )
        {
            this.startState = state;
        }

        return state;
    }

    public State getState(String name)
        throws NoSuchStateException
    {
        State state = (State) this.states.get( name );

        if ( state == null )
        {
            throw new NoSuchStateException( this,
                                            name );
        }

        return state;
    }
}
