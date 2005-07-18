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

public abstract class WorkflowImage extends AbstractComponent 
{
    private Process process;
    
    public abstract String getState();
    public abstract String getUseMap();
    
    public void setProcess(Process process)
    {
        this.process = process;
    }
    
    public Process getProcess()
    {
        return process;
    }
    
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IEngineService service = getPage().getEngine().getService( WorkflowImageService.SERVICE_NAME);
        
        ILink link = service.getLink( cycle, this, new Object[] { getProcess().getProcessHandle().getHandle(), getState() } );
        
        writer.begin( "img" );
        
        writer.attribute( "src", link.getAbsoluteURL() );
        writer.attribute( "border", "0" );
        
        if ( getUseMap() != null )
        {
            writer.attribute( "usemap", getUseMap() );
        }
        
        writer.end( "img" );
    }
} 