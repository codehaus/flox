package flox.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.VertexFontFunction;
import edu.uci.ics.jung.graph.decorators.VertexPaintFunction;
import edu.uci.ics.jung.graph.decorators.VertexShapeFunction;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.SimpleDirectedSparseVertex;
import edu.uci.ics.jung.utils.UserDataContainer;
import edu.uci.ics.jung.visualization.Coordinates;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.Renderer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.contrib.CircleLayout;
import flox.def.Process;
import flox.def.State;
import flox.def.Transition;


public class JpegWriter implements VertexStringer, VertexFontFunction, VertexShapeFunction, VertexPaintFunction
{
    private Dimension size;
    private Process process;
    
    public JpegWriter()
    {
        super();
    }
    
    public Process getProcess()
    {
        return process;
    }
    
    public void setProcess( Process process )
    {
        this.process = process;
    }

    public void write(OutputStream output) throws ImageFormatException, IOException
    {
        List<State> states = process.getStates();

        Graph graph = new DirectedSparseGraph();

        Map<State, Vertex> vertexIndex = new HashMap<State, Vertex>();

        for ( State state : states )
        {
            SimpleDirectedSparseVertex vertex = new SimpleDirectedSparseVertex();
            
            vertex.setUserDatum( FloxLayout.STATE, state, new UserDataContainer.CopyAction.Shared() );
            graph.addVertex( vertex );
            vertexIndex.put( state, vertex );
        }

        for ( State state : states )
        {
            Vertex vertex = vertexIndex.get( state );

            for ( Transition transition : state.getTransitions() )
            {
                State destState = transition.getDestination();
                Vertex destVertex = vertexIndex.get( destState );

                DirectedSparseEdge edge = new DirectedSparseEdge( vertex, destVertex );
                graph.addEdge( edge );
            }
        }
        
        graph.setUserDatum( FloxLayout.PROCESS, process, new UserDataContainer.CopyAction.Shared() );
        
        FloxLayout layout = new FloxLayout( graph );
        
        layout.initialize( size );
        
        PluggableRenderer renderer = new PluggableRenderer(); 
        
        renderer.setVertexStringer( this );
        renderer.setVertexFontFunction( this );
        renderer.setVertexShapeFunction( this );
        renderer.setVertexLabelCentering( true );
        renderer.setVertexPaintFunction( this );
        //renderer.setEdgeStringer( new FloxStringer() );
        
        FloxVisualizationViewer viewer = new FloxVisualizationViewer( layout, renderer );
        
        viewer.setBackground( Color.white );
        viewer.setForeground( Color.black );
        
        viewer.resize( size );
        
        Dimension ld = layout.getCurrentSize();
        viewer.setScale( (float) size.width/ld.width, (float) size.height/ld.height );
        
        BufferedImage image = new BufferedImage( size.width, size.height, BufferedImage.TYPE_INT_RGB );

        Graphics2D g2 = image.createGraphics();
        
        g2.setBackground( Color.white );
        g2.setColor( Color.black );
        
        viewer.paintComponent( g2 );
        
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( output );
        encoder.setJPEGEncodeParam( encoder.getDefaultJPEGEncodeParam( image ) );
        encoder.encode( image );
    }

    public void setSize( int x, int y )
    {
        this.size = new Dimension( x, y );
    }
    
    public Dimension getSize()
    {
        return size;
    }
    
    public String getLabel(Vertex vertex)
    {
        State state = (State) vertex.getUserDatum( FloxLayout.STATE );
        
        return state.getName();
    }
    
    public Font getFont(Vertex vertex)
    {
        return new Font( "SansSerif", Font.BOLD, 9 );
    }
    
    public Shape getShape(Vertex vertex)
    {
        int width = 50;
        int height = 15;
        
        double x = ((Coordinates)vertex.getUserDatum( FloxLayout.COORDS )).getX();
        double y = ((Coordinates)vertex.getUserDatum( FloxLayout.COORDS )).getY();
        
        return new Rectangle( 0-(width/2), 0-(height/2), width, height );
    }
    
    public Paint getDrawPaint(Vertex vertex)
    {
        return Color.black;
    }
    
    public Paint getFillPaint(Vertex vertex)
    {
        State state = (State) vertex.getUserDatum( FloxLayout.STATE );
        Process process = ( Process ) vertex.getGraph().getUserDatum( FloxLayout.PROCESS );
        
        if ( state == process.getStartState() )
        {
            return new Color( 0.0f, 0.7f, 0.0f );
        }
        else if ( vertex.outDegree() == 0 )
        {
            return new Color( 0.7f, 0.0f, 0.0f );
        }
        
        return new Color( 0.9f, 0.9f, 0.9f );
    }
}

class FloxVisualizationViewer extends VisualizationViewer
{
    public FloxVisualizationViewer(Layout layout, Renderer renderer)
    {
        super( layout, renderer );
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent( g );
    }
}
