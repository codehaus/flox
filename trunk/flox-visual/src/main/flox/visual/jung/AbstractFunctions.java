package flox.visual.jung;

import edu.uci.ics.jung.graph.Vertex;
import flox.def.Process;
import flox.def.State;


public class AbstractFunctions
{
    private Process process;
    
    public AbstractFunctions(Process process)
    {
        this.process = process;
    }
    
    public Process getProcess()
    {
        return process;
    }
    
    protected State getState(Vertex vertex)
    {
        return (State) vertex.getUserDatum( FloxLayout.STATE );
    }
    
    protected boolean isStartState(Vertex vertex)
    {
        return isStartState( getState( vertex ) );
    }
    
    protected boolean isStartState(State state)
    {
        return getProcess().getStartState().equals( state );
    }
    
    protected boolean isEndState(Vertex vertex)
    {
        return isEndState( getState( vertex ) );
    }
    
    protected boolean isEndState(State state)
    {
        return state.getTransitions().isEmpty();
    }
}
