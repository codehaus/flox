package flox.web.tapestry.components;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

import flox.def.Process;
import flox.web.tapestry.WorkflowEngineProvider;
import flox.web.tapestry.services.WorkflowImageService;

public abstract class WorkflowImage extends AbstractComponent implements WorkflowEngineProvider
{
    private Process process;
    
    public abstract String getState();
    
    public void setProcess(Process process)
    {
        this.process = process;
    }
    
    public Process getProcess()
    {
        return process;
    }
    
    public void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        System.err.println( "render: " ) ;
        IEngineService service = getPage().getEngine().getService( WorkflowImageService.SERVICE_NAME);
        
        ILink link = service.getLink( cycle, this, new Object[] { getProcess().getProcessHandle().getHandle(), getState() } );
        
        System.err.println( "prc: " + getProcess().getProcessHandle().getHandle() );
        System.err.println( "state: " + getState() );
        System.err.println( "link: " + link.getAbsoluteURL() );
        
        writer.begin( "img" );
        
        writer.attribute( "src", link.getAbsoluteURL() );
        
        writer.end( "img" );
    }
} 
