package flox.web.tapestry;

import flox.NoSuchProcessException;
import flox.WorkflowEngine;
import flox.spi.ProcessSourceException;
import flox.web.tapestry.model.WorkflowTableModel;

public interface WebWorkflowEngine extends WorkflowEngine
{
    public WorkflowTableModel getWorkflowTableModel(Object context, String processName) 
        throws ProcessSourceException, NoSuchProcessException;
    
    public WorkflowTableModel getWorkflowTableModel(Object context, String processName, String stateName) 
        throws ProcessSourceException, NoSuchProcessException;
}
