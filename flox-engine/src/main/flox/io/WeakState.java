package flox.io;

import org.xml.sax.Locator;

import java.util.ArrayList;
import java.util.List;

import flox.spi.Action;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 18, 2005
 * Time: 12:02:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class WeakState
        extends WeakEntity
        implements ActionOwner
{
    private String name;
    private List<WeakTransition> transitions;
    private Action action;

    public WeakState(Locator locator,
                     String name)
    {
        super( locator );
        this.name = name;
        this.transitions = new ArrayList<WeakTransition>();
    }

    public String getName()
    {
        return name;
    }

    public void addTransition(WeakTransition transition)
    {
        this.transitions.add( transition );
    }

    public List<WeakTransition> getTransitions()
    {
        return transitions;
    }

    public Action getAction()
    {
        return action;
    }

    public void setAction(Action action)
    {
        this.action = action;
    }
}
