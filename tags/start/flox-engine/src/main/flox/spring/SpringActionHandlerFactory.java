package flox.spring;

import flox.spi.ActionHandlerFactory;
import flox.spi.ActionHandler;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.BeansException;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 18, 2005
 * Time: 9:48:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class SpringActionHandlerFactory
        implements ActionHandlerFactory, BeanFactoryAware
{
    public static final String NS_URI = "http://flox.codehaus.org/v1/spring";

    private BeanFactory beanFactory;

    public SpringActionHandlerFactory()
    {

    }

    public boolean supports(String uri, String name)
    {
        if ( uri.equals( NS_URI ) )
        {
           if ( name.equals( "action" ) )
           {
                return true;
           }
        }

        return false;
    }

    public ActionHandler newHandler(String uri, String name) throws Exception
    {
        return new SpringActionHandler( getBeanFactory() );
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.beanFactory = beanFactory;
    }

    public BeanFactory getBeanFactory()
    {
        return beanFactory;
    }
}
