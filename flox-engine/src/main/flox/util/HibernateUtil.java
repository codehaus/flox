package flox.util;

import java.util.List;
import java.util.Enumeration;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 16, 2005
 * Time: 4:28:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class HibernateUtil
{
    public static List getMappings() throws IOException
    {
        ClassLoader cl = HibernateUtil.class.getClassLoader();

        Enumeration urls = cl.getResources( "hibernate.conf" );

        List mappings = new ArrayList();

        while ( urls.hasMoreElements() )
        {
            URL url = (URL) urls.nextElement();

            InputStream in = url.openStream();

            try
            {
                BufferedReader reader = new BufferedReader( new InputStreamReader( in ) );

                String line = null;

                while ( ( line = reader.readLine() ) != null )
                {
                    line = line.trim();

                    if ( !line.equals( "" ) )
                    {
                        mappings.add( line );
                    }
                }
            }
            finally
            {
                in.close();
            }
        }

        return mappings;
    }
}
