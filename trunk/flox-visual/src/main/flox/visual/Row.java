package flox.visual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import edu.uci.ics.jung.graph.Vertex;
import flox.def.State;
import flox.visual.jung.FloxLayout;


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
                
                State s1 = (State) v1.getUserDatum( FloxLayout.STATE );
                State s2 = (State) v2.getUserDatum( FloxLayout.STATE );
                
                return s1.getName().compareTo( s2.getName() );
            }
        });
        
        LinkedList<Vertex> optimized = new LinkedList<Vertex>();
        
        boolean front = false;
        
        for ( Vertex vertex : sorted )
        {
            State state = (State) vertex.getUserDatum( FloxLayout.STATE );
            
            if ( front )
            {
                optimized.addFirst( vertex );
            }
            else
            {
                optimized.addLast( vertex );
            }
             
            front = !front;
        }
        
        this.vertices = optimized;
    }
}
