package flox;

import flox.def.Process;
import flox.io.ProcessReader;

import javax.xml.parsers.ParserConfigurationException;
import java.util.List;
import java.util.Iterator;
import java.net.URL;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Apr 10, 2005
 * Time: 5:19:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClassPathProcessLoader
        implements ProcessLoader
{
    private static Logger LOGGER = Logger.getLogger( ClassPathProcessLoader.class );
    
    private ProcessReader processReader;
    private String prefix;
    private List processNames;
    
    public void setProcessReader(ProcessReader processReader)
    {
        this.processReader = processReader;
    }

    public ProcessReader getProcessReader()
    {
        if ( processReader == null )
        {
            processReader = new ProcessReader();
        }
        
        return processReader;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public List getProcessNames()
    {
        return processNames;
    }

    public void setProcessNames(List processNames)
    {
        this.processNames = processNames;
    }

    public void loadProcesses(WorkflowEngine workflowEngine)
    {
        for ( Iterator nameIter = processNames.iterator(); nameIter.hasNext(); )
        {
            String name = (String) nameIter.next();

            try
            {
                loadProcess( workflowEngine,
                             name );
            }
            catch (IOException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            catch (ParserConfigurationException e)
            {
                LOGGER.error( e.getMessage() );
            }
            catch (SAXException e)
            {
                LOGGER.error( e.getMessage() );
            }
            catch (DuplicateProcessException e)
            {
                LOGGER.error( e.getMessage() );
            }
        }
    }
    
    protected void loadProcess(WorkflowEngine workflowEngine,
                               String name) throws IOException, ParserConfigurationException, SAXException, DuplicateProcessException
    {
        if ( this.prefix != null )
        {
            name = this.prefix + "/" + name + ".xml";
        }

        URL url = getClass().getClassLoader().getResource( name );

        if ( url != null )
        {
            ProcessReader reader = getProcessReader();
            
            Process process = reader.read( url );

            workflowEngine.addProcess( name, process );
        }
    }
}
