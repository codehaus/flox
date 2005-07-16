package flox;

import java.util.List;

import org.hibernate.criterion.Criterion;

import flox.def.NoSuchStateException;
import flox.def.Process;
import flox.def.State;
import flox.def.Transition;
import flox.model.NoSuchModelObjectException;
import flox.model.StateModel;
import flox.spi.ProcessHandle;
import flox.spi.ProcessSourceException;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Apr 10, 2005
 * Time: 5:32:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface WorkflowEngine
{
    Process getProcess(Object context, String name) 
        throws ProcessSourceException, NoSuchProcessException;
    
    Process getProcess(ProcessHandle processHandle)
        throws ProcessSourceException, NoSuchProcessException;
    
    Workflow getWorkflow(Object context, String processName, Class flowedObjectClass, Criterion flowedObjectCriterion)
            throws ProcessSourceException, NoSuchProcessException, NoSuchModelObjectException;
    
    Workflow getWorkflow(Object context, String processName, Object flowedObject) 
        throws ProcessSourceException, NoSuchProcessException, NoSuchModelObjectException;

    List<Workflow> getWorkflows(Object context, String processName) 
        throws ProcessSourceException, NoSuchProcessException;

    List<Workflow> getWorkflows(Object context, String processName, String currentState) 
        throws ProcessSourceException, NoSuchProcessException, NoSuchStateException;
    
    List<Transition> getAvailableCurrentTransitions(Workflow workflow);

    List<StateModel> getStateSequence(Workflow workflow);

    boolean attemptManualTransition(Workflow workflow, Transition transition) 
        throws TransitionNotManualException;

    boolean attemptManualTransition(Long workflowId, String transitionName) 
        throws ProcessSourceException, NoSuchModelObjectException, NoSuchProcessException, TransitionNotManualException;
    
    Workflow newWorkflow(Object context, String processName) 
        throws ProcessSourceException, NoSuchProcessException;
    
    Workflow newWorkflow(Object context, String processName, Object flowedObject) 
        throws ProcessSourceException, NoSuchProcessException;

    State getCurrentState(Workflow workflow);
    
    List<Transition> getCurrentTransitions(Workflow workflow);
}
