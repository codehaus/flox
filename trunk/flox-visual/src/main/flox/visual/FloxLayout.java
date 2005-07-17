package flox.visual;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.utils.UserDataContainer;
import edu.uci.ics.jung.visualization.AbstractLayout;
import edu.uci.ics.jung.visualization.Coordinates;
import flox.def.Process;
import flox.def.State;

public class FloxLayout extends AbstractLayout
{
    public final static String PROCESS = "flox.visual.FloxLayout.process";
    public final static String STATE = "flox.visual.FloxLayout.state";
    
    public final static String COORDS = "flox.visual.FloxLayout.coords";
    
    private RowList rowList;
        
    public FloxLayout(Graph g)
    {
        super( g );
    }

    @Override
    protected void initialize_local()
    {
        Map<State,Vertex> stateToVertex = new HashMap<State,Vertex>();
        
        Set<Vertex> vertices = getGraph().getVertices();
        
        for ( Vertex vertex : vertices )
        {
            State state = (State) vertex.getUserDatum( STATE );
            
            System.err.println( state.getName() + "/" + vertex );
            stateToVertex.put( state, vertex );
        }
        
        Process process = (Process) getGraph().getUserDatum( PROCESS );
        
        rowList = new RowList();
        
        State state = process.getStartState();
        
        Vertex vertex = stateToVertex.get( state );
            
        rowList.add( 0, vertex );
        
        int curRow = 0;
        
        Set<Vertex> seenVertices = new HashSet<Vertex>();
        seenVertices.add( vertex );
        
        while ( curRow < rowList.getDepth() )
        {
            List<Vertex> rowVertices = rowList.get( curRow ).getVertices();
            
            for ( Vertex rowVertex : rowVertices )
            {
                Set<Edge> edges = rowVertex.getOutEdges();
                
                for ( Edge edge : edges )
                {
                    Vertex destVertex = edge.getOpposite( rowVertex );
                    
                    System.err.println( "destination: " + destVertex );
                    
                    if ( ! seenVertices.contains( destVertex ) )
                    {
                        rowList.add( curRow+1, destVertex );
                        seenVertices.add( destVertex );
                    }
                }
                
                seenVertices.add( rowVertex );
            }
            
            ++curRow;
        }
        
        //rowList.dump();
        rowList.optimize();
    }

    @Override
    protected void initialize_local_vertex( Vertex vertex )
    {
        int row = rowList.getRow( vertex );
        int col = rowList.getColumn( vertex );
        
        int widthPx  = this.getCurrentSize().width;
        int heightPx = this.getCurrentSize().height;
        
        int width = rowList.getWidth();
        int depth = rowList.getDepth();
        
        int rowWidth = rowList.getWidth( row );
        
        int columnWidthPx = widthPx / width;
        int rowHeightPx = heightPx / depth;
        
        Coordinates coords = new Coordinates();
        
        double x = ( col * columnWidthPx );
        double y = ( row * rowHeightPx ) + ( rowHeightPx / 2 );
        
        x = x + ( widthPx / 2 ) - ( ( rowWidth - 1 ) * ( columnWidthPx / 2 ) );
        
        State state = (State) vertex.getUserDatum( STATE );
        
        System.err.println( "row: " + row + " / state " + state.getName() + " / row width " + rowWidth + " / x " + x );
        
        coords.setX( x );
        coords.setY( y );
        
        vertex.setUserDatum( COORDS, coords, new UserDataContainer.CopyAction.Shared() );
    }
    
    public double getX(Vertex vertex)
    {
        return getCoordinates( vertex ).getX();
    }
    
    public double getY(Vertex vertex)
    {
        return getCoordinates( vertex ).getY();
    }
    
    public Coordinates getCoordinates(Vertex vertex)
    {
        return (Coordinates) vertex.getUserDatum( COORDS );
    }

    @Override
    public void advancePositions()
    {
    }

    public boolean isIncremental()
    {
        return false;
    }

    public boolean incrementsAreDone()
    {
        return false;
    }

}
