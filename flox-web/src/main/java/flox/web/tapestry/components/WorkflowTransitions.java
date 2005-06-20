package flox.web.tapestry.components;

import java.util.List;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.Foreach;
import org.apache.tapestry.form.IPropertySelectionModel;

import flox.NoSuchProcessException;
import flox.TransitionNotManualException;
import flox.Workflow;
import flox.WorkflowEngine;
import flox.def.NoSuchStateException;
import flox.def.State;
import flox.def.Transition;
import flox.model.NoSuchModelObjectException;


public abstract class WorkflowTransitions extends BaseComponent
{
    public abstract WorkflowEngine getWorkflowEngine();
    public abstract Workflow getWorkflow();
    
    public WorkflowTransitions()
    {
        super();
    }
    
    public List<Transition> getTransitions()
    {
        return getWorkflowEngine().getAvailableCurrentTransitions( getWorkflow() );
    }
    
    public void followTransitionAction(IRequestCycle cycle) throws NoSuchModelObjectException, NoSuchProcessException, TransitionNotManualException
    {        
        Object[] parameters = cycle.getServiceParameters();
     
        Long   workflowId     = (Long) parameters[0];
        String transitionName = (String) parameters[1];
        
        getWorkflowEngine().attemptManualTransition( workflowId, transitionName );
    }
}
