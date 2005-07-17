package flox.spi;

import flox.Workflow;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 9:58:11 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Predicate
{
    boolean evaluate(Workflow workflow,
                     Object context);
}
