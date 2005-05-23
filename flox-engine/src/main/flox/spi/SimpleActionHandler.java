package flox.spi;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 18, 2005
 * Time: 10:14:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleActionHandler
        extends ActionHandler
{
    private Action action;

    public SimpleActionHandler()
    {
        
    }
    
    public SimpleActionHandler(Action action)
    {
        this.action = action;
    }
    
    public void setAction(Action action)
    {
        this.action = action;
    }
    
    public Action getAction()
    {
        return this.action;
    }

    public void startAction(Attributes attrs) throws SAXException
    {
    }

    public void endAction() throws SAXException
    {
    }
}
