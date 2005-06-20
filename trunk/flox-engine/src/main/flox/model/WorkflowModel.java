package flox.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 12:24:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorkflowModel implements Serializable
{
    private Long id;
    private String processName;
    private Object flowedObject;
    private StateModel currentState;

    public WorkflowModel()
    {
    }
    
    public WorkflowModel(Object flowedObject)
    {
        setFlowedObject( flowedObject );
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getProcessName()
    {
        return processName;
    }

    public void setProcessName(String processName)
    {
        this.processName = processName;
    }

    public Object getFlowedObject()
    {
        return flowedObject;
    }

    public void setFlowedObject(Object flowedObject)
    {
        this.flowedObject = flowedObject;
    }
    
    public StateModel getCurrentState()
    {
        return this.currentState;
    }
    
    public void setCurrentState(StateModel currentState)
    {
        this.currentState = currentState;
    }
    
    public String toString()
    {
        return "[WorkflowModel: id=" + this.id +
            "; processName=" + this.processName +
            "; flowedObject=" + this.flowedObject + "]";
    }
}