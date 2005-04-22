package flox;

import flox.def.*;
import flox.model.NoSuchModelObjectException;
import flox.model.StateModel;

import java.util.Collection;
import java.util.List;

import net.sf.hibernate.expression.Criterion;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Apr 10, 2005
 * Time: 5:32:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface WorkflowEngine
{
    void addProcess(flox.def.Process process)
        throws DuplicateProcessException;

    Collection getProcessNames();

    Workflow getWorkflow(String processName,
                         Class flowedObjectClass,
                         Criterion flowedObjectCriterion)
            throws NoSuchProcessException, NoSuchModelObjectException;

    List getWorkflows(String processName) throws NoSuchProcessException;

    List<Transition> getAvailableCurrentTransitions(Workflow workflow);

    List<StateModel> getStateSequence(Workflow workflow);

    boolean attemptManualTransition(Workflow workflow,
                                           Transition transition) throws TransitionNotManualException;

    Workflow newWorkflow(String processName)
        throws NoSuchProcessException;
}
