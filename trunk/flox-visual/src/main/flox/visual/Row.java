package flox.visual;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.jung.graph.Vertex;


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
}
