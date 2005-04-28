package flox.spi;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 18, 2005
 * Time: 10:08:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleTriggerDefinitionHandlerFactory
        implements TriggerDefinitionHandlerFactory
{
    private String uri;
    private Map<String,SimpleTriggerDefinitionHandler> handlers;

    public SimpleTriggerDefinitionHandlerFactory()
    {
        this.handlers = new HashMap<String,SimpleTriggerDefinitionHandler>();
    }

    public SimpleTriggerDefinitionHandlerFactory(String uri)
    {
        this();
        this.uri = uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getUri()
    {
        return uri;
    }

    public Map<String, SimpleTriggerDefinitionHandler> getHandlers()
    {
        return handlers;
    }

    public void setHandlers(Map<String, SimpleTriggerDefinitionHandler> handlers)
    {
        this.handlers = handlers;
    }

    public void register(String name,
                         SimpleTriggerDefinitionHandler handler)
    {
        this.handlers.put( name,
                           handler );
    }

    public boolean supports(String uri, String name)
    {
        if ( uri.equals( this.uri ) )
        {
            return ( this.handlers.containsKey( name ) );
        }
        
        return false;
    }

    public TriggerDefinitionHandler newHandler(String uri, String name) throws IllegalAccessException, InstantiationException
    {
        if ( uri.equals( this.uri ) )
        {
            SimpleTriggerDefinitionHandler handler = this.handlers.get( name );

            if ( handler == null )
            {
                return null;
            }

            return handler;
        }

        return null;
    }
    
    public String toString()
    {
        return "[SimpleTriggerDefinitionHandlerFactory: uri=" + this.uri +
            "; handlers=" + this.handlers + "]";
    }
}
