package flox.visual;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.SimpleDirectedSparseVertex;
import edu.uci.ics.jung.utils.UserDataContainer;
import flox.def.Process;
import flox.def.State;
import flox.def.Transition;
import flox.visual.jung.FloxLayout;


public class GraphBuilder
{

    public GraphBuilder()
    {
        super();
    }
    
    public static Graph buildGraph(Process process)
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
                
                System.err.println( "connect: " + state.getName() + " to " + destState.getName() );
            }
        }
        
        graph.setUserDatum( FloxLayout.PROCESS, process, new UserDataContainer.CopyAction.Shared() );
        
        return graph;
    }
}
