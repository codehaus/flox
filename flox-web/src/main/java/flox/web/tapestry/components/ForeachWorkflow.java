package flox.web.tapestry.components;

import java.util.List;

import org.apache.tapestry.BaseComponent;

import flox.NoSuchProcessException;
import flox.Workflow;
import flox.WorkflowEngine;
import flox.def.NoSuchStateException;
import flox.spi.ProcessSourceException;


public abstract class ForeachWorkflow extends BaseComponent
{
    public abstract WorkflowEngine getWorkflowEngine();
    public abstract void setWorkflowEngine(WorkflowEngine engine);
    
    public abstract Object getContext();
    public abstract void setContext(Object context);
    
    public abstract String getProcess();
    public abstract void setProcess(String processName);
    
    public abstract String getState();
    public abstract void setState(String stateName);
    
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
