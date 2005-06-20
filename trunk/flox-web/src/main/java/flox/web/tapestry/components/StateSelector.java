package flox.web.tapestry.components;

import java.util.List;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.form.IPropertySelectionModel;

import flox.NoSuchProcessException;
import flox.WorkflowEngine;
import flox.def.State;


public abstract class StateSelector extends BaseComponent
{
    public abstract WorkflowEngine getWorkflowEngine();
    public abstract String getProcess();
    
    public StateSelector()
    {
        super();
    }

    public IPropertySelectionModel getStatesModel() throws NoSuchProcessException
    {
        return new StatesPropertySelectionModel( getWorkflowEngine(), getProcess() );
    }
}
