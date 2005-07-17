package flox.def;

import flox.spi.Action;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 15, 2005
 * Time: 10:52:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class State
{
    private Process process;
    private String name;
    private List<Transition> transitions;
    private Action action;

    protected State(Process process,
                    String name)
    {
        this.process     = process;
        this.name        = name;
        this.transitions = new ArrayList<Transition>();
    }

    public Process getProcess()
    {
        return process;
    }

    public String getName()
    {
        return name;
    }
    
    public Transition addTransition(String name,
                                    State destination)
        throws DuplicateTransitionException, ProcessMismatchException
    {
        for ( Iterator<Transition> transitionIter = transitions.iterator(); transitionIter.hasNext(); )
        {
            Transition transition = transitionIter.next();

            if ( transition.getName().equals( name ) )
            {
                throw new DuplicateTransitionException( this,
                                                        name,
                                                        destination );
            }
        }


        if ( destination.getProcess() != getProcess() )
        {
            throw new ProcessMismatchException( this,
                                                destination );
        }

        Transition transition = new Transition( name,
                                                this,
                                                destination );

        this.transitions.add( transition );

        return transition;
    }

    public List<Transition> getTransitions()
    {
        return Collections.unmodifiableList( this.transitions );
    }

    public Action getAction()
    {
        return action;
    }

    public void setAction(Action action)
    {
        this.action = action;
    }

}
