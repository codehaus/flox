package flox.spi;

import flox.Workflow;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 18, 2005
 * Time: 10:20:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class NoOpAction
        implements Action
{
    public void execute(Workflow workflow,
                        Object context) throws Exception
    {
        // intentionally left blank
    }
}
