package flox.def;

import flox.spi.Predicate;
import flox.spi.Action;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 15, 2005
 * Time: 10:52:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class Transition
{
    private String name;
    private State origin;
    private State destination;
    private Predicate predicate;
    private TriggerDefinition triggerDefinition;
    private Action action;

    public Transition(String name,
                      State origin,
                      State destination)
    {
        this.name        = name;
        this.origin      = origin;
        this.destination = destination;
    }

    public String getName()
    {
        return name;
    }

    public State getOrigin()
    {
        return origin;
    }

    public State getDestination()
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

    public TriggerDefinition getTriggerDefinition()
    {
        return triggerDefinition;
    }

    public void setTriggerDefinition(TriggerDefinition triggerDefinition)
    {
        this.triggerDefinition = triggerDefinition;
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
