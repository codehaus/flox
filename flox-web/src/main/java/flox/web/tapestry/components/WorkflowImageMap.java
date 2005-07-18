package flox.web.tapestry.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Set;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IAction;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ActionService;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.link.ActionLink;
import org.apache.tapestry.link.DirectLink;
import org.apache.tapestry.link.ILinkRenderer;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.visualization.Coordinates;
import flox.WorkflowEngine;
import flox.def.NoSuchStateException;
import flox.def.Process;
import flox.def.State;
import flox.visual.GraphBuilder;
import flox.visual.jung.FloxLayout;
import flox.visual.jung.FloxLayoutSolver;
import flox.visual.jung.VertexFunctions;
import flox.web.tapestry.WorkflowEngineProvider;


public abstract class WorkflowImageMap extends WorkflowImage implements IDirect
{
    private String useMap;
    
    public WorkflowImageMap()
    {
        super();
    }
    
    public abstract void setState(String state);
    public abstract String getState();
    
    public boolean isStateful()
    {
        return false;
    }
    
    public void setUseMap(String useMap)
    {
        this.useMap = useMap;
    }
    
    public String getUseMap()
    {
        return useMap;
    }
    
    public void trigger(IRequestCycle cycle)
    {
        Object[] parameters = cycle.getServiceParameters();
        
        String state = (String) parameters[0];
        
        setState( state );
    }

    @Override
    protected void renderComponent( IMarkupWriter writer, IRequestCycle cycle )
    {
        Graph graph = GraphBuilder.buildGraph( getProcess() );
        
        VertexFunctions vertexFunctions = new VertexFunctions( getProcess() );
        vertexFunctions.setFont( new Font( "Verdana", Font.PLAIN, 10 ) );
        
        FloxLayoutSolver solver = new FloxLayoutSolver( graph );
        FloxLayout layout = new FloxLayout( graph, vertexFunctions, solver.getRowList() ); 
        
        Dimension prefSize = new Dimension( layout.getPreferredWidth(), layout.getPreferredHeight() );
        
        layout.initialize( prefSize );        
        
        writer.begin( "map" );
        
        writer.attribute( "name", "workflowmap" );
        
        WorkflowEngine wfEngine = ((WorkflowEngineProvider)getPage()).getWorkflowEngine();
        
        for ( Vertex vertex : (Set<Vertex>) graph.getVertices() )
        {
            writer.begin( "area" );
            Coordinates coords = layout.getCoordinates( vertex );
            Dimension dim = vertexFunctions.getShapeDimension( vertex );
            
            int x1 = ( int ) (coords.getX() - (dim.width/2));
            int y1 = ( int ) (coords.getY() - (dim.height/2));
            int x2 = ( int ) (coords.getX() + (dim.width/2));
            int y2 = ( int ) (coords.getY() + (dim.height/2));
            
            State state = (State) vertex.getUserDatum( FloxLayout.STATE );
            
            IEngineService actionService = cycle.getEngine().getService( Tapestry.DIRECT_SERVICE );
            
            ILink link = actionService.getLink( cycle, this, new Object[] { state.getName() } );
            
            String toolTip = state.getName();
                
            try
            {
                int numWorkflows = wfEngine.getWorkflows( getProcess(), state.getName() ).size();
                
                toolTip += ": " + numWorkflows;
            }
            catch ( NoSuchStateException e )
            {
                // ignore
            }
            
            writer.attribute( "href", link.getAbsoluteURL() );
            writer.attribute( "alt", state.getName() );
            writer.attribute( "coords", x1+","+y1+","+x2+","+y2 );
            writer.attribute( "title", toolTip );
            
            writer.end( "area" );
        }
        
        writer.end( "map" );
        
        setUseMap( "#workflowmap" );
        
        super.renderComponent( writer, cycle );
    }
}
