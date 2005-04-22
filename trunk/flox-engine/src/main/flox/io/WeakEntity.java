package flox.io;

import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 18, 2005
 * Time: 12:03:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class WeakEntity
{
    private Locator locator;

    public WeakEntity(Locator locator)
    {
        this.locator = new LocatorImpl(locator);
    }

    public Locator getDocumentLocator()
    {
        return locator;
    }
}
