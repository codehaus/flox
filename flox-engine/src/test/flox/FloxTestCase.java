package flox;

import junit.framework.TestCase;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 4:46:14 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class FloxTestCase
        extends TestCase
{
    private ApplicationContext appContext;

    public FloxTestCase()
    {

    }

    public FloxTestCase(String name)
    {
        super( name );
    }

    public void setUp()
        throws Exception
    {
        super.setUp();

        this.appContext = new ClassPathXmlApplicationContext( new String[] { "flox/spring.xml",
                                                                             "flox/test-spring.xml" } );
    }

    public void tearDown()
        throws Exception
    {
        super.tearDown();
    }

    public ApplicationContext getAppContext()
    {
        return appContext;
    }

    public Object getBean(String id)
    {
        return getAppContext().getBean( id );
    }
}
