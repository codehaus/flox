package flox.spi;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 18, 2005
 * Time: 8:57:54 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ActionHandlerFactory
{
    boolean supports(String uri, String name);

    ActionHandler newHandler(String uri, String name) throws Exception;
}
