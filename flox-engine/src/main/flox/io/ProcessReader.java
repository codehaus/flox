package flox.io;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import flox.def.Process;
import flox.spi.ActionHandlerFactory;
import flox.spi.TriggerDefinitionHandlerFactory;


/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 17, 2005
 * Time: 11:41:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessReader
{
    private SAXParser saxParser;
    
    private List<ActionHandlerFactory> actionHandlerFactories;
    private List<TriggerDefinitionHandlerFactory> triggerDefinitionHandlerFactories;

    public ProcessReader()
    {
        this.actionHandlerFactories            = new ArrayList<ActionHandlerFactory>();
        this.triggerDefinitionHandlerFactories = new ArrayList<TriggerDefinitionHandlerFactory>();
    }

    public SAXParser getSaxParser()
    {
        return saxParser;
    }

    public void setSaxParser(SAXParser saxParser)
    {
        this.saxParser = saxParser;
    }

    public List<ActionHandlerFactory> getActionHandlerFactories()
    {
        return actionHandlerFactories;
    }

    public void setActionHandlerFactories(List<ActionHandlerFactory> actionHandlerFactories)
    {
        this.actionHandlerFactories = actionHandlerFactories;
    }
    
    public void addActionHandlerFactory(ActionHandlerFactory factory)
    {
        this.actionHandlerFactories.add( factory );
    }

    public List<TriggerDefinitionHandlerFactory> getTriggerDefinitionHandlerFactories()
    {
        return triggerDefinitionHandlerFactories;
    }

    public void setTriggerDefinitionHandlerFactories(List<TriggerDefinitionHandlerFactory> triggerDefinitionHandlerFactories)
    {
        this.triggerDefinitionHandlerFactories = triggerDefinitionHandlerFactories;
    }

    public void addTriggerDefinitionHandlerFactory(TriggerDefinitionHandlerFactory factory)
    {
        this.triggerDefinitionHandlerFactories.add( factory );
    }
    
    public Process read(String proc) throws ParserConfigurationException, SAXException, IOException
    {
        Reader in = new StringReader( proc );
        try
        {
            return read( in );
        }
        finally
        {
            in.close();
        }
    }
    
    public Process read(Reader in) throws ParserConfigurationException, SAXException, IOException
    {
        SAXParser parser = getSaxParser();

        if ( parser == null )
        {
            SAXParserFactory factory = SAXParserFactory.newInstance( );

            factory.setNamespaceAware( true );

            parser = factory.newSAXParser();
        }

        ProcessReaderHandler handler = new ProcessReaderHandler( this.actionHandlerFactories,
                                                                 this.triggerDefinitionHandlerFactories );

        parser.parse( new InputSource( in ),
                      handler );

        return handler.getProcess();
    }
    
    public Process read(InputStream in) throws ParserConfigurationException, SAXException, IOException
    {
        SAXParser parser = getSaxParser();

        if ( parser == null )
        {
            SAXParserFactory factory = SAXParserFactory.newInstance( );

            factory.setNamespaceAware( true );

            parser = factory.newSAXParser();
        }

        ProcessReaderHandler handler = new ProcessReaderHandler( this.actionHandlerFactories,
                                                                 this.triggerDefinitionHandlerFactories );

        parser.parse( in,
                      handler );

        return handler.getProcess();
    }

    public Process read(URL url) throws ParserConfigurationException, SAXException, IOException
    {
        SAXParser parser = getSaxParser();

        if ( parser == null )
        {
            SAXParserFactory factory = SAXParserFactory.newInstance( );

            factory.setNamespaceAware( true );

            parser = factory.newSAXParser();
        }

        ProcessReaderHandler handler = new ProcessReaderHandler( this.actionHandlerFactories,
                                                                 this.triggerDefinitionHandlerFactories );

        parser.parse( url.toExternalForm(),
                      handler );

        return handler.getProcess();
    }


}
