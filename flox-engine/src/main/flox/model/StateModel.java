package flox.model;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 10:00:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class StateModel
{
    private Long id;
    private WorkflowModel workflow;
    private String name;
    private Date enteredDate;
    private Date exitedDate;

    public StateModel()
    {
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public WorkflowModel getWorkflow()
    {
        return workflow;
    }

    public void setWorkflow(WorkflowModel workflow)
    {
        this.workflow = workflow;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getEnteredDate()
    {
        return enteredDate;
    }

    public void setEnteredDate(Date enteredDate)
    {
        this.enteredDate = enteredDate;
    }

    public Date getExitedDate()
    {
        return exitedDate;
    }
    
    public void setExitedDate(Date exitedDate)
    {
        this.exitedDate = exitedDate;
    }

    public String toString()
    {
        return "[State: id=" + this.id + 
            "; name=" + this.name +
            "; enteredDate=" + this.enteredDate +
            "; exitedDate=" + this.exitedDate + "]";
    }

}
