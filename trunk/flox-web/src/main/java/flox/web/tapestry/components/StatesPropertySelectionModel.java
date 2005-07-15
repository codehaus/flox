package flox.web.tapestry.components;

import java.util.List;

import org.apache.tapestry.form.IPropertySelectionModel;

import flox.NoSuchProcessException;
import flox.WorkflowEngine;
import flox.def.NoSuchStateException;
import flox.def.State;
import flox.spi.ProcessSourceException;


public class StatesPropertySelectionModel
    implements IPropertySelectionModel
{
    private WorkflowEngine workflowEngine;
    private Object context;
    private String process;
    private List<State> states;
    
    public StatesPropertySelectionModel(WorkflowEngine workflowEngine, Object context, String process) throws ProcessSourceException, NoSuchProcessException
    {
        this.workflowEngine = workflowEngine;
        this.context = context;
        this.process = process;
        this.states = workflowEngine.getProcess( context, process ).getStates();
    }

    public int getOptionCount()
    {
        return this.states.size();
    }

    public Object getOption(int index)
    {
        return ((State)this.states.get( index )).getName();
    }

    public String getLabel(int index)
    {
        String state = (String) getOption( index );
        
        int num;
        
        try
        {
            num = workflowEngine.getWorkflows( context, process, state ).size();
        }
        catch ( ProcessSourceException e )
        {
            num = 0;
        }
        catch ( NoSuchStateException e )
        {
            num = 0;
        }
        catch ( NoSuchProcessException e )
        {
            num = 0;
        }
        
        return state + " (" + num + ")";
    }

    public String getValue(int index)
    {
        return (String) getOption( index );
    }

    public Object translateValue(String value)
    {
        return value;
    }
}
