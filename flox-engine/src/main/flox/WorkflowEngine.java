package flox;

import flox.def.NoSuchStateException;
import flox.def.Process;
import flox.def.State;
import flox.def.Transition;
import flox.model.NoSuchModelObjectException;
import flox.model.StateModel;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.Criterion;

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

    Process getProcess(String name) throws NoSuchProcessException;
    
    Workflow getWorkflow(String processName,
                         Class flowedObjectClass,
                         Criterion flowedObjectCriterion)
            throws NoSuchProcessException, NoSuchModelObjectException;
    
    Workflow getWorkflow(String processName, Object flowedObject) 
        throws NoSuchProcessException, NoSuchModelObjectException;

    List getWorkflows(String processName) throws NoSuchProcessException;

    List getWorkflows(String processName, String currentState) throws NoSuchProcessException, NoSuchStateException;
    
    List<Transition> getAvailableCurrentTransitions(Workflow workflow);

    List<StateModel> getStateSequence(Workflow workflow);

    boolean attemptManualTransition(Workflow workflow,
                                    Transition transition) throws TransitionNotManualException;

    boolean attemptManualTransition(Long workflowId,
                                    String transitionName) throws NoSuchModelObjectException, NoSuchProcessException, TransitionNotManualException;
    
    Workflow newWorkflow(String processName)
        throws NoSuchProcessException;

    State getCurrentState(Workflow workflow);
    List<Transition> getCurrentTransitions(Workflow workflow);
}
