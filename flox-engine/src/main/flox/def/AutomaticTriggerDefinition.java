package flox.def;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 18, 2005
 * Time: 12:17:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class AutomaticTriggerDefinition
        extends TriggerDefinition
{
    private long checkInterval;

    public AutomaticTriggerDefinition()
    {

    }

    public long getCheckInterval()
    {
        return checkInterval;
    }

    public void setCheckInterval(long checkInterval)
    {
        this.checkInterval = checkInterval;
    }
}
