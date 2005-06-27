package flox;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import flox.io.ProcessReader;
import flox.def.*;
import flox.def.Process;
import flox.model.StateModel;

import java.net.URL;
import java.util.List;

public class WorkflowEngineTest 
        extends FloxTestCase
{
    private WorkflowEngine engine;

    public WorkflowEngineTest(String name)
    {
        super(name);
    }

    public void setUp() throws Exception
    {
        super.setUp();
        this.engine = (WorkflowEngine) getBean( "test.workflowEngine" );
    }

    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    public static Test suite()
    {
        return new TestSuite(WorkflowEngineTest.class);
    }

    public void testNewWorkflowFullyAutomatic() throws Exception
    {
        Workflow workflow = newWorkflow( "fully-auto" );

        List<StateModel> stateSequence = this.engine.getStateSequence( workflow );

        assertEquals( "3 states in sequence",
                      3,
                      stateSequence.size() );

        assertEquals( "state 'first'",
                      "first",
                      stateSequence.get( 0 ).getName() );

        assertEquals( "state 'second'",
                      "second",
                      stateSequence.get( 1 ).getName() );

        assertEquals( "state 'third'",
                      "third",
                      stateSequence.get( 2 ).getName() );
    }

    public void testNewWorkflowFullyManual() throws Exception
    {
        Workflow workflow = newWorkflow( "fully-manual" );

        assertNotNull( "not complete",
                       workflow.getCurrentState() );

        assertEquals( "state 'first'",
                      "first",
                      workflow.getCurrentState().getName() );

        this.engine.attemptManualTransition( workflow,
                                             workflow.getCurrentState().getTransitions().get( 0 ) );

        assertEquals( "state 'second'",
                      "second",
                      workflow.getCurrentState().getName() );

        assertTrue( "transition is followed",
                    this.engine.attemptManualTransition( workflow,
                                                         workflow.getCurrentState().getTransitions().get( 0 ) ) );

        assertNull( "is complete",
                    workflow.getCurrentState() );

        List<StateModel> stateSequence = this.engine.getStateSequence( workflow );

        assertEquals( "3 states in sequence",
                      3,
                      stateSequence.size() );

        assertEquals( "state 'first'",
                      "first",
                      stateSequence.get( 0 ).getName() );

        assertEquals( "state 'second'",
                      "second",
                      stateSequence.get( 1 ).getName() );

        assertEquals( "state 'third'",
                      "third",
                      stateSequence.get( 2 ).getName() );

    }

    protected Workflow newWorkflow(String name) throws Exception
    {
        addProcess( name );

        return this.engine.newWorkflow( name );
    }

    protected void addProcess(String name) throws Exception
    {
        URL url = getClass().getResource( name + ".xml" );

        ProcessReader reader = (ProcessReader) getBean( "test.processReader" );

        Process process = reader.read( url );

        this.engine.addProcess( name, process );
    }
}


