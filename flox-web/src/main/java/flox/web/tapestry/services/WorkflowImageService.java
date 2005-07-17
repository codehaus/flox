/*
 * Copyright (c) 2004-2005, by OpenXource, LLC. All rights reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF OPENXOURCE
 *  
 * The copyright notice above does not evidence any          
 * actual or intended publication of such source code. 
 */
package flox.web.tapestry.services;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.AbstractService;
import org.apache.tapestry.engine.IEngineServiceView;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.request.ResponseOutputStream;

import samples.graph.BasicRenderer;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgeStringer;
import edu.uci.ics.jung.graph.decorators.StringLabeller;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.graph.decorators.StringLabeller.UniqueLabelException;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.SimpleDirectedSparseVertex;
import edu.uci.ics.jung.utils.UserDataContainer;
import edu.uci.ics.jung.visualization.FRLayout;
import edu.uci.ics.jung.visualization.GraphDraw;
import edu.uci.ics.jung.visualization.ISOMLayout;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.Renderer;
import edu.uci.ics.jung.visualization.SpringLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.contrib.CircleLayout;
import edu.uci.ics.jung.visualization.contrib.KKLayout;
import flox.NoSuchProcessException;
import flox.WorkflowEngine;
import flox.def.Process;
import flox.def.State;
import flox.def.Transition;
import flox.spi.ProcessHandle;
import flox.spi.ProcessSourceException;
import flox.visual.FloxLayout;
import flox.web.tapestry.ProcessProvider;

public class WorkflowImageService extends AbstractService
{

    public static final String SERVICE_NAME = "workflowImage";

    public void service( IEngineServiceView engine, IRequestCycle cycle, ResponseOutputStream output )
            throws ServletException, IOException
    {
        String context[] = getServiceContext( cycle.getRequestContext() );

        String pageName = context[0];
        String componentPath = context[1];

        IPage page = cycle.getPage( pageName );

        IComponent component = page;

        ProcessProvider provider = ( ProcessProvider ) component;

        try
        {
            Process process = provider.getProcess();

            List<State> states = process.getStates();

            Graph graph = new DirectedSparseGraph();

            Map<State, Vertex> vertexIndex = new HashMap<State, Vertex>();

            for ( State state : states )
            {
                SimpleDirectedSparseVertex vertex = new SimpleDirectedSparseVertex();
                
                vertex.setUserDatum( "name", state.getName(), new UserDataContainer.CopyAction.Shared() );
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
                    edge.setUserDatum( "name", transition.getName(), new UserDataContainer.CopyAction.Shared() );
                    graph.addEdge( edge );
                }
            }
            
            graph.setUserDatum( FloxLayout.PROCESS, process, new UserDataContainer.CopyAction.Shared() );
       
            Dimension vd = new Dimension( 400, 400 );
            
            //CircleLayout layout = new CircleLayout( graph );
            FloxLayout layout = new FloxLayout( graph );
            
            layout.initialize( vd );
            
            PluggableRenderer renderer = new PluggableRenderer(); 
            
            renderer.setVertexStringer( new FloxStringer() );
            //renderer.setEdgeStringer( new FloxStringer() );
            
            MyVV viewer = new MyVV( layout, renderer );
            
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
            
            output.setContentType( "image/jpeg" );

            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( output );
            encoder.setJPEGEncodeParam( encoder.getDefaultJPEGEncodeParam( image ) );
            encoder.encode( image );
            
            output.close();
        }
        catch ( ProcessSourceException e )
        {
            throw new ServletException( e );
        }
        catch ( NoSuchProcessException e )
        {
            throw new ServletException( e );
        }
    }

    public String getName()
    {
        return SERVICE_NAME;
    }

    public ILink getLink( IRequestCycle cycle, IComponent component, Object[] parameters )
    {
        String[] context = new String[]
        {
                component.getPage().getPageName(), component.getIdPath()
        };

        return constructLink( cycle, SERVICE_NAME, context, null, true );
    }
}

class MyVV extends VisualizationViewer {
    public MyVV(Layout layout, Renderer renderer)
    {
        super( layout, renderer);
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent( g );
    }
}

class FloxStringer implements VertexStringer, EdgeStringer 
{
    public String getLabel( Vertex v )
    {
        return (String) v.getUserDatum( "name" );
    }

    public String getLabel( Edge e )
    {
        return (String) e.getUserDatum( "name" );
    }
}