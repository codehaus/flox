/*
 * Copyright (c) 2004-2005, by OpenXource, LLC. All rights reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF OPENXOURCE
 *  
 * The copyright notice above does not evidence any          
 * actual or intended publication of such source code. 
 */
package flox.web.tapestry.services;

import java.awt.Graphics;
import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.AbstractService;
import org.apache.tapestry.engine.IEngineServiceView;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.request.ResponseOutputStream;

import sun.awt.image.ImageFormatException;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgeStringer;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.Renderer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import flox.NoSuchProcessException;
import flox.def.Process;
import flox.spi.ProcessSourceException;
import flox.visual.WorkflowImageWriter;
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
            
            WorkflowImageWriter imageWriter = new WorkflowImageWriter( WorkflowImageWriter.PNG );
            
            imageWriter.setProcess( process );
            
            output.setContentType( imageWriter.getMimeType() );

            imageWriter.write( output );
            
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
        catch ( ImageFormatException e )
        {
            throw new ServletException( e );
        }
        catch ( IOException e )
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