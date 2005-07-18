package flox.visual.jung;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.visualization.Coordinates;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import flox.def.Process;
import flox.def.State;

public class VertexHighlighter extends AbstractFunctions implements VisualizationViewer.Paintable
{
    private Map<String,Color> colors;
    
    private FloxLayout layout;

    public VertexHighlighter(Process process, FloxLayout layout)
    {
        super( process );
        this.layout = layout;
        
        this.colors = new HashMap<String,Color>();
    }
    
    public void addHighlight(String stateName, Color color)
    {
        colors.put( stateName, color );
    }

    public void paint( Graphics g )
    {
        System.err.println( "running highlighter" ) ;
        
        Graphics2D graphics = (Graphics2D) g;
        
        for ( Vertex vertex : (Set<Vertex>) layout.getGraph().getVertices() )
        {
            State state = getState( vertex );
            
            Color color = colors.get( state.getName() );
            
            if ( color != null )
            {
                Coordinates coords = layout.getCoordinates( vertex );
                Dimension size = layout.getVertexFunctions().getShapeDimension( vertex );
                
                double hWidth = size.width + 6;
                double hHeight = size.height + 6;
                
                Shape highlight = new RoundRectangle2D.Double( coords.getX() - ( hWidth/2 ) , coords.getY() - ( hHeight/2 ), hWidth, hHeight, 10, 10 );
                
                Paint oldPaint = graphics.getPaint();
                
                graphics.setPaint( color );
                
                graphics.fill( highlight );
                
                graphics.setPaint( oldPaint );
            }
        }
    }

    public boolean useTransform()
    {
        return false;
    }
}