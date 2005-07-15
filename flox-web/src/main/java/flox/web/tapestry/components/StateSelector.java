package flox.web.tapestry.components;

import java.util.List;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.form.IPropertySelectionModel;

import flox.NoSuchProcessException;
import flox.WorkflowEngine;
import flox.def.State;
import flox.spi.ProcessSourceException;


public abstract class StateSelector extends BaseComponent
{
    public abstract WorkflowEngine getWorkflowEngine();
    public abstract void setWorkflowEngine(WorkflowEngine workflowEngine);
    
    public abstract Object getContext();
    public abstract void setContext(Object context);
    
    public abstract String getProcess();
    public abstract void setProcess(String processName);
    
    public StateSelector()
    {
        super();
    }

    public IPropertySelectionModel getStatesModel() throws ProcessSourceException, NoSuchProcessException
    {
        return new StatesPropertySelectionModel( getWorkflowEngine(), getContext(), getProcess() );
    }
}
