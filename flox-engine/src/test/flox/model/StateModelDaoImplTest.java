package flox.model;

import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;

public class StateModelDaoImplTest 
        extends DaoTestCase
{
    public StateModelDaoImplTest(String name)
    {
        super(name);
    }

    public void setUp() throws Exception
    {
        super.setUp();
    }

    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    public static Test suite()
    {
        return new TestSuite(StateModelDaoImplTest.class);
    }

    public void testDao() throws Exception
    {
        assertNotNull( "dao is available",
                       getBean( "stateModelDao" ) );
    }

    public void testSave() throws Exception
    {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setProcessHandle( "test process" );

        StateModel state = new StateModel();

        state.setWorkflow( workflow );
        state.setName( "test state");

        WorkflowModelDao workflowDao = (WorkflowModelDao) getBean( "workflowModelDao" );

        workflowDao.save( workflow );

        StateModelDao stateDao = (StateModelDao) getBean( "stateModelDao" );

        stateDao.save( state );

        Long id = state.getId();

        StateModel fetchedState = stateDao.get( id );

        assertEquals( "same name",
                      state.getName(),
                      fetchedState.getName() );

        assertEquals( "same workflow",
                      state.getWorkflow().getId(),
                      fetchedState.getWorkflow().getId() );

        assertEquals( "same process",
                      state.getWorkflow().getProcessHandle(),
                      fetchedState.getWorkflow().getProcessHandle() );
    }

    public void testGetCurrentState() throws Exception
    {

        WorkflowModel workflow = new WorkflowModel();
        workflow.setProcessHandle( "test process" );

        StateModel state = new StateModel();

        state.setWorkflow( workflow );
        state.setName( "test state");

        WorkflowModelDao workflowDao = (WorkflowModelDao) getBean( "workflowModelDao" );

        workflowDao.save( workflow );

        StateModelDao stateDao = (StateModelDao) getBean( "stateModelDao" );

        stateDao.save( state );

        try
        {
            stateDao.getCurrentState( workflow );
            fail( "should have throw NoSuchModelObjectException" );
        }
        catch (NoSuchModelObjectException e)
        {
            // expected and correct
        }

        state.setEnteredDate( new Date() );

        stateDao.save( state );

        assertEquals( state.getId() + " is current state",
                      state.getId(),
                      stateDao.getCurrentState( workflow ).getId() );

        state.setExitedDate( new Date() );

        stateDao.save( state );

        try
        {
            stateDao.getCurrentState( workflow );
            fail( "should have throw NoSuchModelObjectException" );
        }
        catch (NoSuchModelObjectException e)
        {
            // expected and correct
        }
    }
}
