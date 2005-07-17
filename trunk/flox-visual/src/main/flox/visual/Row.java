package flox.visual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import edu.uci.ics.jung.graph.Vertex;
import flox.def.State;


public class Row
{
    private int depth;
    
    private List<Vertex> vertices;
    
    public Row(int depth)
    {
        super();
        this.vertices = new ArrayList<Vertex>();
    }
    
    public int getDepth()
    {
        return depth;
    }

    public void add( Vertex vertex )
    {
        this.vertices.add( vertex );
    }

    public List<Vertex> getVertices()
    {
        return vertices;
    }

    public boolean contains( Vertex vertex )
    {
        return vertices.contains( vertex );
    }

    public int getWidth()
    {
        return vertices.size();
    }

    public void optimize()
    {
        Object comparator;
        List<Vertex> sorted = new ArrayList<Vertex>( this.vertices );
        
        Collections.sort( sorted, new Comparator<Vertex>() {
            public int compare( Vertex v1, Vertex v2 )
            {
                if ( v1.outDegree() < v2.outDegree() )
                {
                    return 1;
                }
                
                if ( v1.outDegree() > v2.outDegree() )
                {
                    return -1;
                }
                
                return 0;
            }
        });
        
        LinkedList<Vertex> optimized = new LinkedList<Vertex>();
        
        boolean front = false;
        
        for ( Vertex vertex : sorted )
        {
            State state = (State) vertex.getUserDatum( FloxLayout.STATE );
            
            if ( front )
            {
                System.err.println( state.getName() + " to first" );
                optimized.addFirst( vertex );
            }
            else
            {
                System.err.println( state.getName() + " to last" );
                optimized.addLast( vertex );
            }
             
            front = !front;
        }
        
        this.vertices = optimized;
    }
}
