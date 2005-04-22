package flox.spring;

import flox.spi.ActionHandler;
import flox.spi.Action;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.springframework.beans.factory.BeanFactory;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 18, 2005
 * Time: 9:43:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class SpringActionHandler
        extends ActionHandler
{
    private Action action;
    private BeanFactory beanFactory;

    public SpringActionHandler(BeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }

    public BeanFactory getBeanFactory()
    {
        return this.beanFactory;
    }

    public void startAction(Attributes attrs) throws SAXException
    {
        String id = attrs.getValue( "", "id" );

        if ( id == null )
        {
            throw new SAXParseException( "attribute 'id' required",
                                         getDocumentLocator() );
        }

        id = id.trim();

        this.action = (Action) getBeanFactory().getBean( id );
    }

    public void endAction() throws SAXException
    {
    }

    public Action getAction() throws Exception
    {
        return this.action;
    }


}
