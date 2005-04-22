package flox.spi;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 18, 2005
 * Time: 8:55:06 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ActionHandler
        extends DefaultHandler
{
    private Locator locator;
    
    public ActionHandler()
    {

    }

    public void setDocumentLocator(Locator locator)
    {
        this.locator = locator;
    }

    public Locator getDocumentLocator()
    {
        return this.locator;
    }

    public abstract void startAction(Attributes attrs) throws SAXException;
    public abstract void endAction() throws SAXException;

    public abstract Action getAction() throws Exception;

}
