package flox.spi;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import flox.def.TriggerDefinition;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 18, 2005
 * Time: 10:14:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleTriggerDefinitionHandler
        extends TriggerDefinitionHandler
{
    private TriggerDefinition triggerDefinition;

    public SimpleTriggerDefinitionHandler()
    {

    }

    public SimpleTriggerDefinitionHandler(TriggerDefinition triggerDefinition)
    {
        this.triggerDefinition = triggerDefinition;
    }

    public void startTriggerDefinition(Attributes attrs) throws SAXException
    {
    }

    public void endTriggerDefinition() throws SAXException
    {
    }

    public void setTriggerDefinition(TriggerDefinition triggerDefinition)
    {
        this.triggerDefinition = triggerDefinition;
    }

    public TriggerDefinition getTriggerDefinition() throws Exception
    {
        return this.triggerDefinition;
    }
}
