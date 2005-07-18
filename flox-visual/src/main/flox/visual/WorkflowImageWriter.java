package flox.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import sun.awt.image.ImageFormatException;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgeShape;
import edu.uci.ics.jung.graph.decorators.EdgeShapeFunction;
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
import edu.uci.ics.jung.visualization.VisualizationViewer.Paintable;
import edu.uci.ics.jung.visualization.contrib.CircleLayout;
import flox.def.Process;
import flox.def.State;
import flox.def.Transition;
import flox.visual.jung.ExtendedPluggableRenderer;
import flox.visual.jung.FloxLayout;
import flox.visual.jung.FloxLayoutSolver;
import flox.visual.jung.FloxVisualizationViewer;
import flox.visual.jung.VertexFunctions;
import flox.visual.jung.VertexHighlighter;
import flox.visual.jung.VertexLabelPaintFunction;


public class WorkflowImageWriter 
{
    public static final String JPG = "jpg";
    public static final String PNG = "png";
    public static final String GIF = "gif";
    
    private String state;
    private String imageType;
    
    private Process process;
    
    public WorkflowImageWriter(String imageType)
    {
        super();
        this.imageType = imageType;
    }
    
    public String getImageType()
    {
        return this.imageType;
    }
    
    public String getMimeType()
    {
        return "image/" + getImageType();
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
        BufferedImage image = getImage();
        
        ImageIO.write( image, getImageType(), output );
    }
    
    protected BufferedImage getImage() 
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
        
        // --
        
        VertexFunctions vertexFunctions = new VertexFunctions( process );
        
        vertexFunctions.setStartColors( new Color( 0x33, 0x66, 0x99 ), Color.black, Color.white );
        vertexFunctions.setEndColors( new Color( 0x55, 0x55, 0x55 ), Color.black, Color.white );
        vertexFunctions.setOtherColors( new Color( 0xEE, 0xEE, 0xEE ), Color.black, new Color( 0x33, 0x33, 0x33 ) );
        vertexFunctions.setFont( new Font( "Verdana", Font.PLAIN, 10 ) );
        
        FloxLayoutSolver solver = new FloxLayoutSolver( graph );
        FloxLayout layout = new FloxLayout( graph, vertexFunctions, solver.getRowList() ); 
        
        Dimension prefSize = new Dimension( layout.getPreferredWidth(), layout.getPreferredHeight() );
        
        layout.initialize( prefSize );
        
        ExtendedPluggableRenderer renderer = new ExtendedPluggableRenderer(); 
        
        BufferedImage image = new BufferedImage( prefSize.width, prefSize.height, BufferedImage.TYPE_INT_RGB );
        
        Graphics2D g2 = image.createGraphics();
        
        renderer.setVertexStringer( vertexFunctions );
        renderer.setVertexFontFunction( vertexFunctions );
        renderer.setVertexShapeFunction( vertexFunctions );
        renderer.setVertexPaintFunction( vertexFunctions );
        renderer.setVertexLabelPaintFunction( vertexFunctions );
        renderer.setVertexLabelCentering( true );
        
        renderer.setEdgeShapeFunction( new EdgeShape.Line() );
        
        FloxVisualizationViewer viewer = new FloxVisualizationViewer( layout, renderer );
        
        VertexHighlighter highlighter = new VertexHighlighter( process, layout );
        
        if ( this.state != null )
        {
            highlighter.addHighlight( this.state, new Color( 0xFF, 0xBB, 0x00 ) );
        }
        
        viewer.addPreRenderPaintable( highlighter );
        
        viewer.setBackground( Color.white );
        viewer.setForeground( Color.black );
        
        viewer.resize( prefSize );
        
        Dimension ld = layout.getCurrentSize();
        
        viewer.setScale( 1f, 1f );
        
        g2.setBackground( Color.white );
        g2.setColor( Color.black );
        
        viewer.paintComponent( g2 );
        
        return image;
    }

    public void setState( String state )
    {
        this.state = state;
    }
}
