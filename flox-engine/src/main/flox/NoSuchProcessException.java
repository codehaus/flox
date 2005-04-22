package flox;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 9:51:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class NoSuchProcessException
        extends FloxException
{
    private WorkflowEngine engine;
    private String name;

    public NoSuchProcessException(WorkflowEngine engine,
                                  String name)
    {
        this.engine = engine;
        this.name   = name;
    }

    public WorkflowEngine getEngine()
    {
        return engine;
    }

    public String getName()
    {
        return name;
    }

    public String getMessage()
    {
        return "No such process '" + getName() + "'";
    }
}
