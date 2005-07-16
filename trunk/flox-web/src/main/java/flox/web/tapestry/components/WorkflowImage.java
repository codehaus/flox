package flox.web.tapestry.components;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

import flox.def.Process;
import flox.web.tapestry.ProcessProvider;
import flox.web.tapestry.services.WorkflowImageService;

public abstract class WorkflowImage extends AbstractComponent implements ProcessProvider
{
    private Process process;
    
    public void setProcess(Process process)
    {
        System.err.println( "setProcess: " + process );
        this.process = process;
    }
    
    public Process getProcess()
    {
        System.err.println( "getProcess: " + process );
        return process;
    }
    
    public void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IEngineService service = getPage().getEngine().getService( WorkflowImageService.SERVICE_NAME);
        
        ILink link = service.getLink( cycle, this, new Object[] { getProcess().getProcessHandle().getHandle() } );
        
        writer.begin( "img" );
        
        writer.attribute( "src", link.getAbsoluteURL() );
        
        writer.end( "img" );
    }
} 
