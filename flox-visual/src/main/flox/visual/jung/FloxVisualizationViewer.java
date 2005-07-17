package flox.visual.jung;

import java.awt.Graphics;

import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.Renderer;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public class FloxVisualizationViewer extends VisualizationViewer
{
    /**
     * 
     */
    private static final long serialVersionUID = 3258412824522143283L;

    public FloxVisualizationViewer(Layout layout, Renderer renderer)
    {
        super( layout, renderer );
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent( g );
    }
    
}
