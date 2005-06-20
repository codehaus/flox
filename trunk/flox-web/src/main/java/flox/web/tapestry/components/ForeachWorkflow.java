package flox.web.tapestry.components;

import java.util.List;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.components.Foreach;
import org.apache.tapestry.form.IPropertySelectionModel;

import flox.NoSuchProcessException;
import flox.Workflow;
import flox.WorkflowEngine;
import flox.def.NoSuchStateException;
import flox.def.State;


public abstract class ForeachWorkflow extends BaseComponent
{
    public abstract WorkflowEngine getWorkflowEngine();
    public abstract String getProcess();
    public abstract String getState();
    
    public ForeachWorkflow()
    {
        super();
    }
    
    public List<Workflow> getWorkflows() throws NoSuchStateException, NoSuchProcessException
    {
        if ( getState() == null )
        {
            return getWorkflowEngine().getWorkflows( getProcess() );
        }
        else
        {
            return getWorkflowEngine().getWorkflows( getProcess(), getState() );
        }
    }
}
