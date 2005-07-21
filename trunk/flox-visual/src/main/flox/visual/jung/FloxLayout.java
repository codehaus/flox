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

public class FloxLayout extends AbstractLayout
{
    public final static String PROCESS = "flox.visual.FloxLayout.process";
    public final static String STATE = "flox.visual.FloxLayout.state";
    
    public final static String COORDS = "flox.visual.FloxLayout.coords";
    
    private static final int COLUMN_SPACE = 20;
    private static final int ROW_HEIGHT_MULTIPLIER = 3;
    
    private RowList rowList;
    
    private VertexFunctions vertexFunctions;
    private int columnWidth;
    private int rowHeight;
        
    public FloxLayout(Graph g, VertexFunctions vertexFunctions, RowList rowList)
    {
        super( g );
        this.vertexFunctions = vertexFunctions;
        this.rowList         = rowList;
        computeSize();
    }
    
    public VertexFunctions getVertexFunctions()
    {
        return this.vertexFunctions;
    }
    
    public int getColumnWidth()
    {
        return columnWidth;
    }
    
    public int getRowHeight()
    {
        return rowHeight;
    }
    
    public int getPreferredWidth()
    {
        return rowList.getWidth() * columnWidth;
    }
    
    public int getPreferredHeight()
    {
        return rowList.getDepth() * getRowHeight() * ROW_HEIGHT_MULTIPLIER;
    }

    protected void computeSize()
    {
        Set<Vertex> vertices = getGraph().getVertices();
        
        for ( Vertex vertex : vertices )
        {
            int width = vertexFunctions.getShapeDimension( vertex ).width;
            int height = vertexFunctions.getShapeDimension( vertex ).height;
            
            if ( width > columnWidth )
            {
                columnWidth = width;
            }
            if ( height > rowHeight )
            {
                rowHeight =  height;
            }
        }
        
        columnWidth = columnWidth + COLUMN_SPACE;
    }

    @Override
    protected void initialize_local_vertex( Vertex vertex )
    {
        int row = rowList.getRow( vertex );
        int col = rowList.getColumn( vertex );
        
        int widthPx  = this.getCurrentSize().width;
        int heightPx = this.getCurrentSize().height;
        
        int rowWidth = rowList.getWidth( row );
        
        int columnWidthPx = getColumnWidth();
        int rowHeightPx = getRowHeight();
        
        Coordinates coords = new Coordinates();
        
        double x = ( col * columnWidthPx );
        double y = ( row * ( rowHeightPx * ROW_HEIGHT_MULTIPLIER ) );
        
        x = x + ( widthPx / 2 ) - ( ( rowWidth - 1 ) * ( columnWidthPx / 2 ) );
        y = y + ( rowHeightPx/2 ) + 3;
        
        State state = (State) vertex.getUserDatum( STATE );
        
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

    @Override
    protected void initialize_local()
    {
        // nothing
    }

}
