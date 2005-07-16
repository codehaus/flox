package flox.sources.classpath;

import flox.def.Process;
import flox.sources.AbstractProcessSource;
import flox.spi.InvalidProcessHandleException;
import flox.spi.ProcessHandle;
import flox.spi.ProcessSourceException;

import java.io.InputStream;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Apr 10, 2005
 * Time: 5:19:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClassPathProcessSource extends AbstractProcessSource 
{
    private static Logger LOGGER = Logger.getLogger( ClassPathProcessSource.class );

    public Process getProcess(Object context, String name) throws ProcessSourceException
    {
        ProcessHandle handle = null;
        
        if ( context == null )
        {
            handle = new ProcessHandle( name );
        }
        else if ( context instanceof Class )
        {
            handle = new ProcessHandle( ((Class)context).getName() + "|" + name );
        }
        else
        {
            handle = new ProcessHandle( context.getClass().getName() + "|" + name );
        }
        
        return getProcess( handle );
    }
    
    public Process getProcess(ProcessHandle handle) throws InvalidProcessHandleException, ProcessSourceException
    {
        String str = handle.getHandle();
        
        int pipeLoc = str.indexOf( "|" );
        
        InputStream in = null;
        
        if ( pipeLoc <= 0 )
        {
            in = getClass().getClassLoader().getResourceAsStream( str + ".xml" );
        }
        else
        {
            String contextClassName = str.substring( 0, pipeLoc );
            
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            
            
            if ( cl == null )
            {
                cl = getClass().getClassLoader();
            }
            
            try
            {
                Class contextClass = cl.loadClass( contextClassName );
                
                in = contextClass.getResourceAsStream( str.substring( pipeLoc+1 ) + ".xml" );
            }
            catch ( ClassNotFoundException e )
            {
                return null;
            }
        }
        
        if ( in == null )
        {
            return null;
        }
        
        try
        {
            Process process = getProcessReader().read( in );
        
            process.setProcessHandle( handle );
        
            return process;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ProcessSourceException( this, e );
        }
    }
}
