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
import flox.spi.ProcessSourceException;


public abstract class ForeachWorkflow extends BaseComponent
{
    public abstract WorkflowEngine getWorkflowEngine();
    public abstract Object getContext();
    public abstract String getProcess();
    public abstract String getState();
    
    public ForeachWorkflow()
    {
        super();
    }
    
    public List<Workflow> getWorkflows() throws ProcessSourceException, NoSuchStateException, NoSuchProcessException
    {
        if ( getState() == null )
        {
            return getWorkflowEngine().getWorkflows( getContext(), getProcess() );
        }
        else
        {
            return getWorkflowEngine().getWorkflows( getContext(), getProcess(), getState() );
        }
    }
}
