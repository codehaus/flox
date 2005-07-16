package flox;


/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 10:16:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorkflowIntegrityException
        extends RuntimeFloxException
{
    private static final long serialVersionUID = 3832623971605557559L;
    
    private Workflow workflow;

    public WorkflowIntegrityException(Workflow workflow,
                                      Throwable cause)
    {
        super( cause );
        this.workflow = workflow;
    }

    public Workflow getWorkflow()
    {
        return workflow;
    }

    public String getMessage()
    {
        return "Workflow Integrity Exception: " + this.getCause().getMessage();
    }
}
