package flox.io;

import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import flox.def.*;
import flox.def.Process;
import flox.spi.ActionHandlerFactory;
import flox.spi.ActionHandler;
import flox.spi.TriggerDefinitionHandler;
import flox.spi.TriggerDefinitionHandlerFactory;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 17, 2005
 * Time: 11:41:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessReaderHandler
        extends DefaultHandler
{
    public final static String FLOX_URI = "http://flox.codehaus.org/v1/";

    private final static int ROOT = 1;
    private final static int PROCESS = 2;
    private final static int STATE = 4;
    private final static int TRANSITION = 8;
    private final static int ACTION = 16;
    private final static int TRIGGER = 32;

    private List<ActionHandlerFactory> actionHandlerFactories;
    private ActionHandler actionHandler;
    private List<TriggerDefinitionHandlerFactory> triggerDefinitionHandlerFactories;
    private TriggerDefinitionHandler triggerDefinitionHandler;

    private int delegationDepth;

    private int parseState;
    private Process process;
    private StringBuffer characters;
    private Locator locator;
    private Map states;
    private List orderedStates;
    private WeakState currentState;
    private WeakTransition currentTransition;
    private ActionOwner currentActionOwner;

    public ProcessReaderHandler(List<ActionHandlerFactory> actionHandlerFactories,
                                List<TriggerDefinitionHandlerFactory> triggerDefinitionHandlerFactories)
    {
        this.actionHandlerFactories            = actionHandlerFactories;
        this.triggerDefinitionHandlerFactories = triggerDefinitionHandlerFactories;
    }

    public Process getProcess()
    {
        return this.process;
    }

    public void setDocumentLocator(Locator locator)
    {
        this.locator = locator;
    }

    public void startDocument() throws SAXException
    {
        this.parseState = ROOT;
        this.process = null;
    }

    public void endDocument() throws SAXException
    {
        this.parseState = ROOT;
    }

    public void startElement(String uri,
                             String localName,
                             String qName,
                             Attributes attrs) throws SAXException
    {
        this.characters = new StringBuffer();

        if ( ( this.parseState & ROOT ) != 0 )
        {
            if ( uri.equals( FLOX_URI )  )
            {
                if ( localName.equals( "process" ) )
                {
                    startProcess( attrs );
                }
                else
                {
                    throw new SAXParseException( "<flox:process> must be the root element (xmlns:flox=\"" + FLOX_URI + "\"",
                                                 this.locator );
                }
            }
            else
            {
                throw new SAXParseException( "<flox:process> must be the root element (xmlns:flox=\"" + FLOX_URI + "\"",
                                             this.locator );
            }
        }
        else if ( ( this.parseState & PROCESS ) != 0 )
        {
            if ( uri.equals( FLOX_URI ) )
            {
                if ( localName.equals( "state" ) )
                {
                    startState( attrs );
                }
                else
                {
                    throw new SAXParseException( "<flox:process> may have <flox:state> children (xmlns:flox=\"" + FLOX_URI + "\"",
                                                 this.locator );
                }
            }
            else
            {
                throw new SAXParseException( "<flox:process> may have <flox:state> children (xmlns:flox=\"" + FLOX_URI + "\"",
                                             this.locator );
            }
        }
        else if ( (this.parseState & STATE) != 0 )
        {
            if ( uri.equals( FLOX_URI ) )
            {
                if ( localName.equals( "transition" ) )
                {
                    startTransition( attrs );
                }
                else
                {
                    throw new SAXParseException( "only <flox:transition> is allowed within <flox:state> (xmlns:flox=\"" + FLOX_URI + "\"",
                                                 this.locator );
                }
            }
            else
            {
                ActionHandlerFactory factory = getActionHandlerFactory( uri, localName );
                
                if ( factory != null )
                {
                    if ( this.currentState.getAction() != null )
                    {
                        throw new SAXParseException( "State '" + this.currentState.getName() + "' already has an action defined",
                                                     this.locator );
                    }

                    ActionHandler handler = null;

                    try
                    {
                        handler = factory.newHandler( uri,
                                                      localName );
                    }
                    catch (Exception e)
                    {
                        throw new SAXParseException( e.getMessage(),
                                                     this.locator,
                                                     e );
                    }

                    startAction( handler,
                                 attrs );
                }
                else
                {
                    throw new SAXParseException( "URI '" + uri + "' not supported by current extensions",
                                                 this.locator );
                }
            }
        }
        else if ( ( this.parseState & ACTION ) != 0 )
        {
            delegateActionStartElement( uri,
                                        localName,
                                        qName,
                                        attrs );
        }
        else if ( ( this.parseState & TRIGGER ) != 0 )
        {
            delegateTriggerDefinitionStartElement( uri,
                                                   localName,
                                                   qName,
                                                   attrs );
        }
        else if ( (this.parseState & TRANSITION ) != 0 )
        {
            ActionHandlerFactory factory = getActionHandlerFactory( uri, localName );

            if ( factory != null )
            {
                if ( this.currentTransition.getAction() != null )
                {
                    throw new SAXParseException( "Transition '" + this.currentTransition.getName() + "' in state '" + this.currentState.getName() + "' already has an action defined",
                                                 this.locator );
                }

                ActionHandler handler = null;

                try
                {
                    handler = factory.newHandler( uri,
                                                  localName );
                }
                catch (Exception e)
                {
                    throw new SAXParseException( e.getMessage(),
                                                 this.locator,
                                                 e );
                }

                startAction( handler,
                             attrs );
            }
            else
            {
                TriggerDefinitionHandlerFactory triggerHandlerFactory = getTriggerDefinitionHandlerFactory( uri, localName );
                
                if ( triggerHandlerFactory != null )
                {
                    if ( this.currentTransition.getTriggerDefinition() != null )
                    {
                        throw new SAXParseException( "Transition '" + this.currentTransition.getName() + "' in state '" + this.currentState.getName() + "' already has a trigger defined",
                                                     this.locator );
                    }

                    TriggerDefinitionHandler triggerHandler = null;

                    try
                    {
                        triggerHandler = triggerHandlerFactory.newHandler( uri,
                                                                           localName );
                    }
                    catch (Exception e)
                    {
                        throw new SAXParseException( e.getMessage(),
                                                     this.locator,
                                                     e );
                    }

                    startTriggerDefinition( triggerHandler,
                                            attrs );
                }
                else
                {
                    throw new SAXParseException( "URI '" + uri + "' not supported by current extensions",
                                                this.locator );
                }
            }
        }
    }

    public void endElement(String uri,
                           String localName,
                           String qName) throws SAXException
    {
        if ( ( this.parseState  & ACTION ) != 0 )
        {
            if ( this.delegationDepth > 0 )
            {
                delegateActionEndElement( uri,
                                          localName,
                                          qName );
            }

            if ( this.delegationDepth == 0 )
            {
                endAction();
            }
        }

        if ( ( this.parseState & TRIGGER ) != 0 )
        {
            if ( this.delegationDepth > 0 )
            {
                delegateTriggerDefinitionEndElement( uri,
                                                     localName,
                                                     qName );
            }

            if ( this.delegationDepth == 0 )
            {
                endTriggerDefintion();
            }
        }

        if ( uri.equals( FLOX_URI ) )
        {
            if ( localName.equals( "process" ) )
            {
                endProcess();
            }
            else if ( localName.equals( "state" ) )
            {
                endState();
            }
            else if ( localName.equals( "transition" ) )
            {
                endTransition();
            }
        }
    }

    public void startProcess(Attributes attrs) throws SAXException
    {
        /*
        System.err.println( "startProcess" );
        String name = attrs.getValue( "", "name" );

        if ( name == null )
        {
            throw new SAXParseException( "attribute 'name' required on <process>",
                                         this.locator );
        }

        name = name.trim();

        this.process = new Process( name );
        */
        this.process = new Process();
        this.states = new HashMap();
        this.orderedStates = new ArrayList();

        this.parseState = PROCESS;

    }

    public void endProcess() throws SAXException
    {
        //System.err.println( "endProcess" );

        for ( Iterator stateIter = this.orderedStates.iterator(); stateIter.hasNext(); )
        {
            WeakState weakState = (WeakState) stateIter.next();

            try
            {
                State state = this.process.newState( weakState.getName() );

                state.setAction( weakState.getAction() );
            }
            catch (DuplicateStateException e)
            {
                throw new SAXParseException( e.getMessage(),
                                             weakState.getDocumentLocator(),
                                             e );
            }
        }

        for ( Iterator stateIter = orderedStates.iterator(); stateIter.hasNext(); )
        {
            WeakState weakState = (WeakState) stateIter.next();

            List transitions = weakState.getTransitions();

            for ( Iterator transIter = transitions.iterator(); transIter.hasNext(); )
            {
                WeakTransition weakTransition = (WeakTransition) transIter.next();

                try
                {
                    State origin = this.process.getState( weakState.getName() );
                    State destination = this.process.getState( weakTransition.getDestination() );

                    Transition transition = origin.addTransition( weakTransition.getName(),
                                                                  destination );

                    transition.setAction( weakTransition.getAction() );
                    transition.setTriggerDefinition( weakTransition.getTriggerDefinition() );
                }
                catch (NoSuchStateException e)
                {
                    throw new SAXParseException( e.getMessage(),
                                                 weakState.getDocumentLocator(),
                                                 e );
                }
                catch (DuplicateTransitionException e)
                {
                    throw new SAXParseException( e.getMessage(),
                                                 weakState.getDocumentLocator(),
                                                 e );
                }
                catch (ProcessMismatchException e)
                {
                    throw new SAXParseException( e.getMessage(),
                                                 weakState.getDocumentLocator(),
                                                 e );
                }
            }
        }

        this.parseState = ROOT;
    }

    public void startState(Attributes attrs) throws SAXException
    {
        //System.err.println( "startState" );
        String name = attrs.getValue( "", "name" );

        if ( name == null )
        {
            throw new SAXParseException( "attribute 'name' required",
                                         this.locator );
        }

        name = name.trim();

        if ( this.states.containsKey( name ) )
        {
            throw new SAXParseException( "duplicate state '" + name + "'",
                                         this.locator );
        }

        WeakState state = new WeakState( this.locator,
                                         name );

        this.states.put( state.getName(),
                         state );
        
        this.orderedStates.add( state );

        this.currentState = state;
        this.currentActionOwner = state;

        this.parseState = STATE;
    }

    public void endState() throws SAXException
    {
        //System.err.println( "endState" );
        this.currentState = null;
        this.currentActionOwner = null;
        this.parseState = PROCESS;
    }
    
    public void startAction(ActionHandler handler,
                            Attributes attrs) throws SAXException
    {
        //System.err.println( "startAction" );
        this.actionHandler = handler;
        this.actionHandler.setDocumentLocator( this.locator );
        this.actionHandler.startAction( attrs );
        this.delegationDepth = 0;
        this.parseState |= ACTION;
    }

    public void endAction() throws SAXException
    {
        //System.err.println( "endAction" );
        this.actionHandler.endAction();
        this.delegationDepth = 0;

        try
        {
            this.currentActionOwner.setAction( this.actionHandler.getAction() );
        }
        catch (Exception e)
        {
            throw new SAXParseException( e.getMessage(),
                                         this.locator,
                                         e );
        }

        this.parseState ^= ACTION;
    }

    public void startTriggerDefinition(TriggerDefinitionHandler handler,
                                       Attributes attrs) throws SAXException
    {
        this.triggerDefinitionHandler = handler;
        this.triggerDefinitionHandler.setDocumentLocator( this.locator );
        this.triggerDefinitionHandler.startTriggerDefinition( attrs );
        this.delegationDepth = 0;
        this.parseState = TRIGGER;
    }

    public void endTriggerDefintion() throws SAXException
    {
        this.triggerDefinitionHandler.endTriggerDefinition();
        this.delegationDepth = 0;

        try
        {
            this.currentTransition.setTriggerDefinition( this.triggerDefinitionHandler.getTriggerDefinition() );
        }
        catch (Exception e)
        {
            throw new SAXParseException( e.getMessage(),
                                         this.locator,
                                         e );
        }

        this.parseState = TRANSITION;
    }

    public void delegateActionStartElement(String uri,
                                           String localName,
                                           String qName,
                                           Attributes attrs) throws SAXException
    {
        ++this.delegationDepth;
        this.actionHandler.startElement( uri,
                                         localName,
                                         qName,
                                         attrs );
    }

    public void delegateActionEndElement(String uri,
                                         String localName,
                                         String qName) throws SAXException
    {
        this.actionHandler.endElement( uri,
                                       localName,
                                       qName );
        --this.delegationDepth;
    }   
    
    public void delegateTriggerDefinitionStartElement(String uri,
                                                      String localName,
                                                      String qName,
                                                      Attributes attrs) throws SAXException
    {
        ++this.delegationDepth;
        this.triggerDefinitionHandler.startElement( uri,
                                                    localName,
                                                    qName,
                                                    attrs );
    }

    public void delegateTriggerDefinitionEndElement(String uri,
                                                    String localName,
                                                    String qName) throws SAXException
    {
        this.triggerDefinitionHandler.endElement( uri,
                                                  localName,
                                                  qName );
        --this.delegationDepth;
    }
    
    public ActionHandlerFactory getActionHandlerFactory(String uri,
                                                        String localName)
    {
        for ( Iterator<ActionHandlerFactory> factoryIter = actionHandlerFactories.iterator();
              factoryIter.hasNext(); )
        {
            ActionHandlerFactory factory = factoryIter.next();

            if ( factory.supports( uri, localName ) )
            {
                return factory;
            }
        }

        return null;
    }
    
    public TriggerDefinitionHandlerFactory getTriggerDefinitionHandlerFactory(String uri,
                                                                              String localName)
    {
        for ( Iterator<TriggerDefinitionHandlerFactory> factoryIter = triggerDefinitionHandlerFactories.iterator();
              factoryIter.hasNext(); )
        {
            TriggerDefinitionHandlerFactory factory = factoryIter.next();

            if ( factory.supports( uri, localName ) )
            {
                return factory;
            }
        }

        return null;
    }


    public void startTransition(Attributes attrs) throws SAXException
    {
        //System.err.println( "startTransition" );
        String name = attrs.getValue( "", "name" );
        String to   = attrs.getValue( "", "to" );

        if ( name == null )
        {
            throw new SAXParseException( "attribute 'name' required",
                                         this.locator );
        }

        if ( to == null )
        {
            throw new SAXParseException( "attribute 'to' required",
                                         this.locator );
        }

        for ( Iterator transIter = this.currentState.getTransitions().iterator(); transIter.hasNext(); )
        {
            WeakTransition weakTransition = (WeakTransition) transIter.next();

            if ( weakTransition.getName().equals( name ) )
            {
                throw new SAXParseException( "duplicate transition '" + name + "' for state '" + this.currentState.getName() + "'",
                                             this.locator );
            }
        }

        WeakTransition transition = new WeakTransition( this.locator,
                                                        name,
                                                        to );

        this.currentState.addTransition( transition );


        this.currentTransition = transition;
        this.currentActionOwner = transition;

        this.parseState = TRANSITION;
    }

    public void endTransition() throws SAXException
    {
        //System.err.println( "endTransition" );
        this.currentTransition = null;
        this.currentActionOwner = null;
        this.parseState = STATE;
    }

    public void characters(char ch[],
                           int start,
                           int length) throws SAXException
    {
        this.characters.append( ch,
                                start,
                                length );
    }

    public String getLocation()
    {
        return this.locator.getLineNumber() + ":" + this.locator.getColumnNumber();
    }

    public void warning(SAXParseException e) throws SAXException
    {
        super.warning( e );
    }

    public void error(SAXParseException e) throws SAXException
    {
        super.error( e );
    }

    public void fatalError(SAXParseException e) throws SAXException
    {
        super.fatalError( e );
    }
}
