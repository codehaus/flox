package flox.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.SimpleDirectedSparseVertex;
import edu.uci.ics.jung.utils.UserDataContainer;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.Renderer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.contrib.CircleLayout;
import flox.def.Process;
import flox.def.State;
import flox.def.Transition;


public class JpegWriter
{
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
            
            vertex.setUserDatum( "name", state.getName(), new UserDataContainer.CopyAction.Shared() );
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
                edge.setUserDatum( "name", transition.getName(), new UserDataContainer.CopyAction.Shared() );
                graph.addEdge( edge );
            }
        }
        
        graph.setUserDatum( FloxLayout.PROCESS, process, new UserDataContainer.CopyAction.Shared() );
        
        Dimension vd = new Dimension( 400, 400 );
        
        CircleLayout layout = new CircleLayout( graph );
        
        layout.initialize( vd );
        
        PluggableRenderer renderer = new PluggableRenderer(); 
        
        //renderer.setVertexStringer( new FloxStringer() );
        //renderer.setEdgeStringer( new FloxStringer() );
        
        FloxVisualizationViewer viewer = new FloxVisualizationViewer( layout, renderer );
        
        viewer.setBackground( Color.white );
        viewer.setForeground( Color.black );
        
        //viewer.setPreferredSize( vd );
        //viewer.resize( vd );
        viewer.resize( vd );
        
        //layout.update();
        
        for ( int i = 0 ; i < 100 ; ++i )
        {
            layout.advancePositions();
        }
        
        
        Dimension ld = layout.getCurrentSize();
        viewer.setScale( (float) vd.width/ld.width, (float) vd.height/ld.height );
        
        BufferedImage image = new BufferedImage( vd.width, vd.height, BufferedImage.TYPE_INT_RGB );

        Graphics2D g2 = image.createGraphics();
        g2.setBackground( Color.white );
        g2.setColor( Color.black );
        
        viewer.paintComponent( g2 );
        
        //output.setContentType( "image/jpeg" );

        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( output );
        encoder.setJPEGEncodeParam( encoder.getDefaultJPEGEncodeParam( image ) );
        encoder.encode( image );
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
