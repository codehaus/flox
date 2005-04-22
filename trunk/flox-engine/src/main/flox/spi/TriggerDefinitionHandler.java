package flox.spi;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import flox.def.TriggerDefinition;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 18, 2005
 * Time: 8:55:06 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TriggerDefinitionHandler
        extends DefaultHandler
{
    private Locator locator;
    
    public TriggerDefinitionHandler()
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

    public abstract void startTriggerDefinition(Attributes attrs) throws SAXException;
    public abstract void endTriggerDefinition() throws SAXException;

    public abstract TriggerDefinition getTriggerDefinition() throws Exception;

}
