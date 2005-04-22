package flox.spi;

import flox.Workflow;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 18, 2005
 * Time: 8:51:55 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Action
{
    void execute(Workflow workflow, Object context) throws Exception;
}
