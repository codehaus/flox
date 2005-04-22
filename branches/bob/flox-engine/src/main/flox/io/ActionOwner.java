package flox.io;

import flox.spi.Action;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 18, 2005
 * Time: 12:30:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ActionOwner
{
    void setAction(Action action);
    Action getAction();
}
