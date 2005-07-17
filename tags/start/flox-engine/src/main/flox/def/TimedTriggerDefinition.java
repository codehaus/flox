package flox.def;

import flox.Workflow;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 18, 2005
 * Time: 4:33:12 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TimedTriggerDefinition
        extends TriggerDefinition
{
    private Date triggerTime;

    public TimedTriggerDefinition()
    {

    }
    
    public abstract Date getTriggerTime(Workflow workflow,
                                        Object context);
}
