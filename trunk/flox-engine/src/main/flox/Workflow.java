package flox;

import flox.model.WorkflowModel;
import flox.def.Process;
import flox.def.State;
import flox.def.Transition;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 15, 2005
 * Time: 11:32:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class Workflow implements Serializable
{
    private transient WorkflowEngine engine;
    private Process process;
    private WorkflowModel model;

    public Workflow(WorkflowEngine engine,
                    Process process,
                    WorkflowModel model)
    {
        this.engine  = engine;
        this.process = process;
        this.model   = model;
    }

    public WorkflowEngine getEngine()
    {
        return engine;
    }

    Process getProcess()
    {
        return process;
    }

    WorkflowModel getModel()
    {
        return model;
    }
    
    public Long getId()
    {
        return getModel().getId();
    }

    public State getCurrentState()
    {
        return getEngine().getCurrentState( this );
    }

    public List<Transition> getCurrentTransitions()
    {
        return getEngine().getCurrentTransitions( this );
    }

    public List<Transition> getAvailableCurrentTransitions()
    {
        return getEngine().getAvailableCurrentTransitions( this );
    }

    public Object getFlowedObject()
    {
        return getModel().getFlowedObject();
    }
}
