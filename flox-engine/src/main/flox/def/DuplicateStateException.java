package flox.def;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 15, 2005
 * Time: 11:06:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class DuplicateStateException
        extends DefinitionException
{
    private Process process;
    private String name;

    public DuplicateStateException(Process process,
                                   String name)
    {
        this.process = process;
        this.name    = name;
    }

    public Process getProcess()
    {
        return process;
    }

    public String getName()
    {
        return name;
    }

    public String getMessage()
    {
        return "Duplicate state '" + getName() + "'";
    }
}
