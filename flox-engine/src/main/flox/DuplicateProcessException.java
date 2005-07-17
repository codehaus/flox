package flox;

import flox.def.*;
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
    private WorkflowEngine engine;
    private Process process;

    public DuplicateProcessException(WorkflowEngine engine,
                                     Process process)
    {
        this.engine  = engine;
        this.process = process;
    }

    public WorkflowEngine getEngine()
    {
        return engine;
    }

    public Process getProcess()
    {
        return process;
    }

    public String getMessage()
    {
        return "Duplicate process '" + getProcess().getName() + "'";
    }
}
