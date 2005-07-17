package flox.io;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.net.URL;

import flox.def.*;
import flox.def.Process;
import flox.spi.*;

public class ProcessReaderTest
        extends IoTestCase
{
    public ProcessReaderTest(String name)
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
        return new TestSuite(ProcessReaderTest.class);
    }

    public void testSimpleParse() throws Exception
    {
        ProcessReader reader = new ProcessReader();

        Process process = reader.read( getUrl( "simple.xml") );

        assertNotNull( process );

        State first  = process.getState( "first" );
        State second = process.getState( "second" );
        State third  = process.getState( "third" );

        assertNotNull( "has state 'first'",
                       first );

        assertNotNull( "has state 'second'",
                       second );

        assertNotNull( "has state 'third'",
                       third );

        Transition trans0 = null;
        Transition trans1 = null;

        trans0 = first.getTransitions().get( 0 );
        trans1 = first.getTransitions().get( 1 );

        assertEquals( "go to second",
                      trans0.getName() );

        assertEquals( second,
                      trans0.getDestination() );

        assertEquals( "go to third",
                      trans1.getName() );

        assertEquals( third,
                      trans1.getDestination() );

        trans0 = second.getTransitions().get( 0 );
        trans1 = second.getTransitions().get( 1 );

        assertEquals( "go to first",
                      trans0.getName() );

        assertEquals( first,
                      trans0.getDestination() );

        assertEquals( "go to third",
                      trans1.getName() );

        assertEquals( third,
                      trans1.getDestination() );


        trans0 = third.getTransitions().get( 0 );
        trans1 = third.getTransitions().get( 1 );

        assertEquals( "go to first",
                      trans0.getName() );

        assertEquals( first,
                      trans0.getDestination() );

        assertEquals( "go to second",
                      trans1.getName() );

        assertEquals( second,
                      trans1.getDestination() );

        assertSame( "'first' is start state",
                    first,
                    process.getStartState() );
    }

    public void testNoDelegationAction() throws Exception
    {
        ProcessReader reader = new ProcessReader();

        SimpleActionHandlerFactory actionHandlerFactory = new SimpleActionHandlerFactory( "http://flox.codehaus.org/v1/test/" );

        Action action1 = new NoOpAction();
        Action action2 = new NoOpAction();
        Action action3 = new NoOpAction();
        Action action4 = new NoOpAction();

        actionHandlerFactory.register( "action1", new SimpleActionHandler( action1 ) );
        actionHandlerFactory.register( "action2", new SimpleActionHandler( action2 ) );
        actionHandlerFactory.register( "action3", new SimpleActionHandler( action3 ) );
        actionHandlerFactory.register( "action4", new SimpleActionHandler( action4 ) );

        reader.addActionHandlerFactory( actionHandlerFactory );

        SimpleTriggerDefinitionHandlerFactory triggerDefHandlerFactory = new SimpleTriggerDefinitionHandlerFactory( "http://flox.codehaus.org/v1/test/" );

        TriggerDefinition trigDef1 = new AutomaticTriggerDefinition();
        TriggerDefinition trigDef2 = new AutomaticTriggerDefinition();

        triggerDefHandlerFactory.register( "trigger1", new SimpleTriggerDefinitionHandler( trigDef1 ) );
        triggerDefHandlerFactory.register( "trigger2", new SimpleTriggerDefinitionHandler( trigDef2 ) );

        reader.addTriggerDefinitionHandlerFactory( triggerDefHandlerFactory );

        Process process = reader.read( getUrl( "no-delegation-action.xml") );

        State first = process.getState( "first" );

        assertSame( "state 'first' has action1",
                    action1,
                    first.getAction() );

        assertSame( "transition has action3",
                    first.getTransitions().get( 0 ).getAction(),
                    action3 );

        assertSame( "transition has trigDef1",
                    first.getTransitions().get( 0 ).getTriggerDefinition(),
                    trigDef1 );

        State second = process.getState( "second" );

        assertSame( "state 'second' has action2",
                    action2,
                    second.getAction() );

        assertSame( "transition has action4",
                    second.getTransitions().get( 0 ).getAction(),
                    action4 );

        assertSame( "transition has trigDef2",
                    second.getTransitions().get( 0 ).getTriggerDefinition(),
                    trigDef2 );

    }

    protected URL getUrl(String name)
    {
        return getClass().getResource( name );
    }
}
