package flox.web.tapestry;

import flox.DefaultWorkflowEngine;
import flox.NoSuchProcessException;
import flox.def.Process;
import flox.spi.ProcessSourceException;
import flox.web.tapestry.model.WorkflowTableModel;

public class DefaultWebWorkflowEngine extends DefaultWorkflowEngine implements WebWorkflowEngine
{
    public DefaultWebWorkflowEngine()
    {
        super();
    }
    
    public WorkflowTableModel getWorkflowTableModel(Object context, String processName) 
        throws ProcessSourceException, NoSuchProcessException
    {
        Process process = getProcess( context, processName );
        
        return new WorkflowTableModel( this, getWorkflowModelDao(), process );
    }
    
    public WorkflowTableModel getWorkflowTableModel(Object context, String processName, String stateName) 
        throws ProcessSourceException, NoSuchProcessException
    {
        Process process = getProcess( context, processName );
        
        return new WorkflowTableModel( this, getWorkflowModelDao(), process, stateName );
    }
}
