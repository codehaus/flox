package flox;

import flox.def.Transition;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 18, 2005
 * Time: 10:27:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransitionNotManualException
        extends FloxException
{
    private static final long serialVersionUID = 3617851963111717173L;
    
    private Workflow workflow;
    private Transition transition;

    public TransitionNotManualException(Workflow workflow,
                                        Transition transition)
    {
        this.workflow = workflow;
        this.transition = transition;
    }

    public Workflow getWorkflow()
    {
        return workflow;
    }

    public Transition getTransition()
    {
        return transition;
    }

    public String getMessage()
    {
        return "Transition '" + getTransition().getName() + "' in state '" + getTransition().getOrigin().getName() + "' is not manually triggered";
    }
}
