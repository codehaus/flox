package flox;

import flox.def.Process;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 15, 2005
 * Time: 11:53:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class DuplicateProcessException
        extends FloxException
{
    private static final long serialVersionUID = 3545794369106817329L;
    
    private WorkflowEngine engine;
    private String name;
    private Process process;

    public DuplicateProcessException(WorkflowEngine engine, String name, Process process)
    {
        this.engine  = engine;
        this.name    = name;
        this.process = process;
    }

    public WorkflowEngine getEngine()
    {
        return engine;
    }
    
    public String getName()
    {
        return name;
    }

    public Process getProcess()
    {
        return process;
    }

    public String getMessage()
    {
        return "Duplicate process '" + getName() + "'";
    }
}
