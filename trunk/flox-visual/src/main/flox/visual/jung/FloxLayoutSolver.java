package flox.visual.jung;

import java.awt.Dimension;
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
import flox.visual.RowList;

public class FloxLayoutSolver 
{
    private Graph graph;
    
    private RowList rowList;
    
    public FloxLayoutSolver(Graph graph)
    {
        this.graph = graph;
        solve();
    }
    
    public Graph getGraph()
    {
        return graph;
    }
    
    protected void solve()
    {
        Map<State,Vertex> stateToVertex = new HashMap<State,Vertex>();
        
        Set<Vertex> vertices = getGraph().getVertices();
        
        for ( Vertex vertex : vertices )
        {
            State state = (State) vertex.getUserDatum( FloxLayout.STATE );
            
            stateToVertex.put( state, vertex );
        }
        
        Process process = (Process) getGraph().getUserDatum( FloxLayout.PROCESS );
        
        rowList = new RowList();
        
        State state = process.getStartState();
        
        System.err.println( "start state: " + state.getName() );
        
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
        
        System.err.println( "initial" );
        rowList.dump();
        
        rowList.optimize();
        
        System.err.println( "optimized" );
        rowList.dump();
    }
    
    public RowList getRowList()
    {
        return rowList;
    }
}
