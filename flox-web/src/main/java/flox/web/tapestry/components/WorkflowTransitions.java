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
import flox.spi.ProcessSourceException;


public abstract class WorkflowTransitions extends BaseComponent
{
    private Workflow workflow;
    
    public abstract WorkflowEngine getWorkflowEngine();
    
    public abstract Object getContextParameter();
    public abstract String getProcessParameter();
    public abstract Object getObjectParameter();
    public abstract Workflow getWorkflowParameter();
    
    public WorkflowTransitions()
    {
        super();
    }
    
    public Workflow getWorkflow() throws ProcessSourceException, NoSuchProcessException, NoSuchModelObjectException
    {
        Workflow workflow = getWorkflowParameter();
        
        if ( workflow == null )
        {
            return getWorkflowEngine().getWorkflow( getContextParameter(), getProcessParameter(), getObjectParameter() );
        }
        
        return workflow;
    }
    
    public List<Transition> getTransitions() throws ProcessSourceException, NoSuchProcessException, NoSuchModelObjectException
    {
        Workflow workflow = getWorkflow();
        
        return getWorkflowEngine().getAvailableCurrentTransitions( workflow );
    }
    
    public void followTransitionAction(IRequestCycle cycle) throws ProcessSourceException, NoSuchModelObjectException, NoSuchProcessException, TransitionNotManualException
    {        
        Object[] parameters = cycle.getServiceParameters();
     
        Long   workflowId     = (Long) parameters[0];
        String transitionName = (String) parameters[1];
        
        getWorkflowEngine().attemptManualTransition( workflowId, transitionName );
    }
}
