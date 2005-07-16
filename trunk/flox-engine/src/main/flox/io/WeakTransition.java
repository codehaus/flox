package flox.io;

import flox.spi.Predicate;
import flox.spi.Action;
import flox.def.TriggerDefinition;
import org.xml.sax.Locator;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 17, 2005
 * Time: 11:55:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class WeakTransition
        extends WeakEntity
        implements ActionOwner
{
    private String name;
    private String destination;
    private Predicate predicate;
    private Action action;
    private TriggerDefinition triggerDefinition;

    public WeakTransition(Locator locator,
                          String name,
                          String destination)
    {
        super( locator );
        this.name = name;
        this.destination = destination;
    }

    public String getName()
    {
        return name;
    }

    public String getDestination()
    {
        return destination;
    }

    public Predicate getPredicate()
    {
        return predicate;
    }

    public void setPredicate(Predicate predicate)
    {
        this.predicate = predicate;
    }

    public Action getAction()
    {
        return action;
    }

    public void setAction(Action action)
    {
        this.action = action;
    }

    public TriggerDefinition getTriggerDefinition()
    {
        return triggerDefinition;
    }

    public void setTriggerDefinition(TriggerDefinition triggerDefinition)
    {
        this.triggerDefinition = triggerDefinition;
    }
}
