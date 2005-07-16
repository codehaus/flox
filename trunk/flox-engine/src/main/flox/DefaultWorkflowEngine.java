package flox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.criterion.Criterion;

import flox.def.AutomaticTriggerDefinition;
import flox.def.ManualTriggerDefinition;
import flox.def.NoSuchStateException;
import flox.def.Process;
import flox.def.State;
import flox.def.Transition;
import flox.def.TriggerDefinition;
import flox.model.NoSuchModelObjectException;
import flox.model.StateModel;
import flox.model.StateModelDao;
import flox.model.WorkflowModel;
import flox.model.WorkflowModelDao;
import flox.spi.Action;
import flox.spi.ManualTriggerEvaluator;
import flox.spi.Predicate;
import flox.spi.ProcessHandle;
import flox.spi.ProcessSource;
import flox.spi.ProcessSourceException;

public class DefaultWorkflowEngine implements WorkflowEngine
{
    private WorkflowModelDao workflowModelDao;
    private StateModelDao stateModelDao;
    
    private ProcessSource processSource;
    
    private ManualTriggerEvaluator manualTriggerEvaluator;

    public DefaultWorkflowEngine()
    {
    }

    public WorkflowModelDao getWorkflowModelDao()
    {
        return workflowModelDao;
    }

    public void setWorkflowModelDao(WorkflowModelDao workflowModelDao)
    {
        this.workflowModelDao = workflowModelDao;
    }

    public StateModelDao getStateModelDao()
    {
        return stateModelDao;
    }

    public void setStateModelDao(StateModelDao stateModelDao)
    {
        this.stateModelDao = stateModelDao;
    }

    public ManualTriggerEvaluator getManualTriggerEvaluator()
    {
        return manualTriggerEvaluator;
    }

    public void setManualTriggerEvaluator(ManualTriggerEvaluator manualTriggerEvaluator)
    {
        this.manualTriggerEvaluator = manualTriggerEvaluator;
    }

    public ProcessSource getProcessSource()
    {
        return processSource;
    }

    public void setProcessSource(ProcessSource processSource)
    {
        this.processSource = processSource;
    }
    
    public Process getProcess(ProcessHandle handle)
        throws ProcessSourceException, NoSuchProcessException
    {
        Process process = getProcessSource().getProcess( handle );
        
        if ( process == null )
        {
            throw new NoSuchProcessException( this, handle.getHandle() );
        }
        
        return process;
    }

    public Process getProcess(Object context, String name)
        throws ProcessSourceException, NoSuchProcessException
    {
        Process process = getProcessSource().getProcess( context, name );

        if ( process == null )
        {
            throw new NoSuchProcessException( this, name );
        }
        
        return process;
    }

    public Workflow newWorkflow(Object context, String processName)
        throws ProcessSourceException, NoSuchProcessException
    {
        return newWorkflow( context, processName, null );
    }
    
    public Workflow newWorkflow(Object context, String processName, Object flowedObject)
        throws ProcessSourceException, NoSuchProcessException
    {
        Process process = getProcess( context, processName );

        WorkflowModel workflowModel = new WorkflowModel( flowedObject );
        
        workflowModel.setProcessHandle( process.getProcessHandle().getHandle() );
        getWorkflowModelDao().save( workflowModel );
    
        Workflow workflow = new Workflow( this,
                                          process,
                                          workflowModel );
        
        enterStartState( workflow );

        while( attemptTransition( workflow ) )
        {
            // do it again
        }

        return workflow;
    }
    
    public boolean attemptManualTransition(Long workflowId, String transitionName) 
        throws ProcessSourceException, NoSuchModelObjectException, NoSuchProcessException, TransitionNotManualException
    {
        Workflow workflow = getWorkflow( workflowId );
        
        Process process = workflow.getProcess();
        
        State state = workflow.getCurrentState();
        
        Transition transition = state.getTransition( transitionName );
        
        return attemptManualTransition( workflow,
                                        transition );
    }
    
    public boolean attemptManualTransition(Workflow workflow, Transition transition) 
        throws TransitionNotManualException
    {
        if ( ! ( transition.getTriggerDefinition() instanceof ManualTriggerDefinition )  )
        {
            throw new TransitionNotManualException( workflow,
                                                    transition );
        }

        if ( attemptTransition( workflow,
                               transition ) )
        {
            while ( attemptTransition( workflow ) )
            {
                // do it again
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    private void enterStartState(Workflow workflow)
    {
        Process process = workflow.getProcess();

        State startState = process.getStartState();

        Date now = new Date();

        enterState( now,
                    workflow,
                    startState );
    }

    private void enterState(Date now, 
                            Workflow workflow, 
                            State state)
    {
        StateModel stateModel = new StateModel();

        stateModel.setName( state.getName() );
        stateModel.setEnteredDate( now );
        stateModel.setWorkflow( workflow.getModel() );

        getStateModelDao().save( stateModel );
        
        workflow.getModel().setCurrentState( stateModel );
        
        getWorkflowModelDao().save( workflow.getModel() );
        Action action = state.getAction();

        if ( action != null )
        {
            try
            {
                action.execute( workflow,
                                null );
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if ( state.getTransitions().isEmpty() )
        {
            exitState( now,
                       workflow );
        }
    }
    
    private void exitState(Date now, Workflow workflow)
    {
        try
        {
            StateModel stateModel = getStateModelDao().getCurrentState( workflow.getModel() );
            stateModel.setExitedDate( now );
            getStateModelDao().save( stateModel );
        }
        catch (NoSuchModelObjectException e)
        {
            throw new WorkflowIntegrityException( workflow,
                                                  e );
        }
    }
    
    private boolean attemptTransition(Workflow workflow)
    {
        State state = workflow.getCurrentState();

        if ( state == null )
        {
            return false;
        }

        List<Transition> transitions = state.getTransitions();
        
        for ( Iterator<Transition> transIter = transitions.iterator(); transIter.hasNext(); )
        {
            Transition transition = transIter.next();

            TriggerDefinition triggerDef = transition.getTriggerDefinition();
            
            if ( triggerDef == null || triggerDef instanceof AutomaticTriggerDefinition )
            {
                boolean result = attemptTransition( workflow,
                                                    transition );

                if ( result )
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean attemptTransition(Workflow workflow, Transition transition)
    {
        State state = workflow.getCurrentState();
        List<Transition> transitions = state.getTransitions();
        
        if ( ! transitions.contains( transition ) )
        {
            throw new WorkflowIntegrityException( workflow, null );
        }
        
        TriggerDefinition triggerDef = transition.getTriggerDefinition();
        
        Predicate predicate = transition.getPredicate();

        if ( ( predicate == null )
                    ||
        ( predicate.evaluate( workflow,
                              null ) ) )
        {
            followTransition( workflow,
                              transition );
                
            return true;
        }
        
        return false;
    }
    
    private void followTransition(Workflow workflow, Transition transition)
    {
        Date now = new Date();

        exitState( now,
                   workflow );

        Action action = transition.getAction();
        
        if ( action != null )
        {
            try
            {
                action.execute( workflow,
                                null );
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        enterState( now,
                    workflow,
                    transition.getDestination() );
    }

    public Workflow getWorkflow(Object context, String processName, Class flowedObjectClass, Criterion flowedObjectCriterion)
            throws ProcessSourceException, NoSuchProcessException, NoSuchModelObjectException
    {
        Process process = getProcess( context, processName );

        WorkflowModel workflowModel = getWorkflowModelDao().get( processName,
                                                                 flowedObjectClass,
                                                                 flowedObjectCriterion );

        Workflow workflow = new Workflow( this,
                                          process,
                                          workflowModel );

        return workflow;
    }
    
    public Workflow getWorkflow(Object context, String processName, Object flowedObject) 
        throws ProcessSourceException, NoSuchProcessException, NoSuchModelObjectException
    {
        Process process = getProcess( context, processName );
        
        WorkflowModel workflowModel = getWorkflowModelDao().get( process, flowedObject );
        
        Workflow workflow = new Workflow( this, process, workflowModel );
        
        return workflow;
    }
    
    
    public Workflow getWorkflow(Long id) 
        throws ProcessSourceException, NoSuchModelObjectException, NoSuchProcessException
    {
        WorkflowModel wfModel = getWorkflowModelDao().get( id );
        
        ProcessHandle handle = new ProcessHandle( wfModel.getProcessHandle() );
        
        Process process = getProcess( handle );
        
        return new Workflow( this, process, wfModel );
    }

    public List<Workflow> getWorkflows(Object context, String processName) 
        throws ProcessSourceException, NoSuchProcessException
    {
        Process process = getProcess( context, processName );

        List<WorkflowModel> models = getWorkflowModelDao().getAll( process );
        
        List<Workflow> flows = new ArrayList<Workflow>( models.size() );
        
        for ( WorkflowModel model : models )
        {
            Workflow flow = new Workflow( this, process, model );
            flows.add( flow );
        }
        
        return flows;
    }
    
    public List<Workflow> getWorkflows(Object context, String processName, String currentStateName) 
        throws ProcessSourceException, NoSuchProcessException, NoSuchStateException
    {
        Process process = getProcess( context, processName );
        
        State currentState = process.getState( currentStateName );
        
        List<WorkflowModel> models = getWorkflowModelDao().getAll( process, currentState );
        
        List<Workflow> flows = new ArrayList<Workflow>( models.size() );
        
        for ( WorkflowModel model : models )
        {
            Workflow flow = new Workflow( this, process, model );
            
            flows.add( flow );
        }
        
        return flows;
    }

    public State getCurrentState(Workflow workflow)
    {
        try
        {
            StateModel model = getStateModelDao().getCurrentState( workflow.getModel() );

            State state = workflow.getProcess().getState( model.getName() );

            return state;
        }
        catch (NoSuchStateException e)
        {
            throw new WorkflowIntegrityException( workflow,
                                                  e );
        }
        catch (NoSuchModelObjectException e)
        {
            return null;
        }
    }

    public List<Transition> getCurrentTransitions(Workflow workflow)
    {
        State state = getCurrentState( workflow );

        if ( state != null )
        {
            return state.getTransitions();
        }
        
        return new ArrayList<Transition>();
    }

    public List<Transition> getAvailableCurrentTransitions(Workflow workflow)
    {
        List<Transition> available = new LinkedList<Transition>( getCurrentTransitions( workflow ) );

        for ( Iterator<Transition> transIter = available.iterator(); transIter.hasNext(); )
        {
            Transition transition = transIter.next();

            if ( transition.getPredicate() != null ) 
            {
                if ( ! transition.getPredicate().evaluate( workflow,
                                                           null ) )
                {
                    transIter.remove();
                }
            }
        }

        return Collections.unmodifiableList( available );
    }

    public List<StateModel> getStateSequence(Workflow workflow)
    {
        return getStateModelDao().getStateSequence( workflow.getModel() );
    }
}

