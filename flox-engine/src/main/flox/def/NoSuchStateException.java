package flox.def;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 10:07:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class NoSuchStateException
        extends DefinitionException
{
    private Process process;
    private String name;

    public NoSuchStateException(Process process,
                                String name)
    {
        this.process = process;
        this.name    = name;
    }

    public Process getProcess()
    {
        return process;
    }

    public void setProcess(Process process)
    {
        this.process = process;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMessage()
    {
        return "No such state '" + getName() + "'";
    }
}
