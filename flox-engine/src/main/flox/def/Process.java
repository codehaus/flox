package flox.def;

import flox.spi.ProcessHandle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 15, 2005
 * Time: 10:52:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Process implements Serializable
{
    private static final long serialVersionUID = 3257006549032841264L;
    
    private ProcessHandle processHandle;
    private Map<String,State> states;
    private List<State> orderedStates;

    public Process()
    {
        this.states = new HashMap<String,State>();
        this.orderedStates = new ArrayList<State>();
    }
    
    public ProcessHandle getProcessHandle()
    {
        return processHandle;
    }
    
    public void setProcessHandle(ProcessHandle processHandle)
    {
        this.processHandle = processHandle;
    }

    public State getStartState()
    {
        return this.orderedStates.get( 0 );
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
        
        this.orderedStates.add( state );

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
    
    public List<State> getStates()
    {
        return this.orderedStates;
    }
}
