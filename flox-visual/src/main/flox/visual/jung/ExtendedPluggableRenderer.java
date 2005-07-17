package flox.visual.jung;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.visualization.PluggableRenderer;


public class ExtendedPluggableRenderer extends PluggableRenderer
{
    private VertexLabelPaintFunction vertexLabelPaintFunction;
    
    public ExtendedPluggableRenderer()
    {
        super();
    }
    public void paintVertex(Graphics g, Vertex v, int x, int y) 
    {
        if (!vertexIncludePredicate.evaluate(v))
            return;
        
        Graphics2D g2d = (Graphics2D)g;
        
        Stroke old_stroke = g2d.getStroke();
        Stroke new_stroke = vertexStrokeFunction.getStroke(v);
        if (new_stroke != null)
            g2d.setStroke(new_stroke);
        Paint old_paint = g2d.getPaint();
        
        // get the shape to be rendered
        Shape s = vertexShapeFunction.getShape(v);
        // create a transform that translates to the location of
        // the vertex to be rendered
        AffineTransform xform = AffineTransform.getTranslateInstance(x,y);
        // transform the vertex shape with xtransform
        
        //Stroke stroke = new BasicStroke( 8 );
        //Shape highlight = stroke.createStrokedShape( s );
        //highlight = xform.createTransformedShape( highlight );
        
        //g2d.setPaint( new Color( 0x99, 0xCC, 0x33 ) );
        //g2d.fill( highlight );
        
        //stroke = new BasicStroke( 6 );
        //highlight = stroke.createStrokedShape( s );
        //highlight = xform.createTransformedShape( highlight );
        
        //g2d.setPaint( new Color( 0xFF, 0xFF, 0x00 ) );
        //g2d.fill( highlight );
        
        s = xform.createTransformedShape(s);
        
        // get Paints for filling and drawing
        // (filling is done first so that drawing and label use same Paint)
        Paint fill_paint = vertexPaintFunction.getFillPaint(v); 
        if (fill_paint != null)
        {
            g2d.setPaint(fill_paint);
            g2d.fill(s);
        }
        Paint draw_paint = vertexPaintFunction.getDrawPaint(v);
        if (draw_paint != null)
        {
            g2d.setPaint(draw_paint);
            g2d.draw(s);
        }

        if (new_stroke != null)
            g2d.setStroke(old_stroke);

        // use existing paint for text if no draw paint specified
        VertexLabelPaintFunction vertexLabelPaintFunction = getVertexLabelPaintFunction();
        
        Paint label_paint = null;
        
        if ( vertexLabelPaintFunction != null )
        {
            label_paint = vertexLabelPaintFunction.getLabelDrawPaint( v );
        }
        
        if ( label_paint == null )
        {
            label_paint = draw_paint;
        }
        
        if ( label_paint == null )
        {
            label_paint = old_paint;
        }
        
        g2d.setPaint( label_paint );
        
        String label = vertexStringer.getLabel(v);
        if (label != null) {
            labelVertex(g, v, label, x, y);
        }
        
        g2d.setPaint(old_paint);
    }
    
    public VertexLabelPaintFunction getVertexLabelPaintFunction()
    {
        return vertexLabelPaintFunction;
    }
    
    public void setVertexLabelPaintFunction( VertexLabelPaintFunction vertexLabelPaintFunction )
    {
        this.vertexLabelPaintFunction = vertexLabelPaintFunction;
    }
}
