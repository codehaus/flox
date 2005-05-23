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
public class SimpleActionHandlerFactory
        implements ActionHandlerFactory
{
    private String uri;
    private Map<String,SimpleActionHandler> handlers;

    public SimpleActionHandlerFactory()
    {
        this.handlers = new HashMap<String,SimpleActionHandler>();
    }

    public SimpleActionHandlerFactory(String uri)
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
    
    public void setHandlers(Map handlers)
    {
        this.handlers = handlers;
    }
    
    public Map getHandlers()
    {
        return this.handlers;
    }
    
    public void register(String name,
                         SimpleActionHandler handler)
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

    public ActionHandler newHandler(String uri, String name) throws IllegalAccessException, InstantiationException
    {
        if ( uri.equals( this.uri ) )
        {
            SimpleActionHandler handler = this.handlers.get( name );

            if ( handler == null )
            {
                return null;
            }

            return handler;
        }

        return null;
    }
}
