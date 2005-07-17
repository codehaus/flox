package flox.visual.jung;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.VertexFontFunction;
import edu.uci.ics.jung.graph.decorators.VertexPaintFunction;
import edu.uci.ics.jung.graph.decorators.VertexShapeFunction;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.visualization.Coordinates;
import flox.def.Process;


public class VertexFunctions extends AbstractFunctions
    implements VertexStringer, VertexFontFunction, VertexShapeFunction, VertexPaintFunction, VertexLabelPaintFunction
{
    private VertexColorSet startState;
    private VertexColorSet endState;
    private VertexColorSet otherState;
    
    private Graphics graphics;
    
    private Font font;
    
    public VertexFunctions(Process process)
    {
        super( process );
        
        BufferedImage image = new BufferedImage( 100, 100, BufferedImage.TYPE_INT_RGB );
        this.graphics   = image.createGraphics();
        
        this.startState = new VertexColorSet();
        this.endState   = new VertexColorSet();
        this.otherState = new VertexColorSet();
        
        this.font = new Font( "Verdana", Font.BOLD, 10 );
    }
    
    public void setStartColors(Color fill, Color stroke, Color text)
    {
        this.startState = new VertexColorSet( fill, stroke, text );
    }
    
    public void setEndColors(Color fill, Color stroke, Color text)
    {
        this.endState = new VertexColorSet( fill, stroke, text );
    }
    
    public void setOtherColors(Color fill, Color stroke, Color text)
    {
        this.otherState = new VertexColorSet( fill, stroke, text );
    }
    
    public void setFont(Font font)
    {
        this.font = font;
    }

    public Paint getLabelDrawPaint( Vertex vertex )
    {
        if ( isStartState( vertex ) )
        {
            return startState.getText();
        }
        else if ( isEndState( vertex ) )
        {
            return endState.getText();
        }
        
        return otherState.getText();
    }

    public String getLabel( Vertex vertex )
    {
        return getState( vertex ).getName();
    }

    public Font getFont( Vertex vertex )
    {
        return this.font;
    }

    public Shape getShape( Vertex vertex )
    {
        Dimension dim = getShapeDimension( vertex );
        
        return new RoundRectangle2D.Double( 0-(dim.width/2), 0-(dim.height/2), dim.width, dim.height, 10, 10 );
    }
    
    protected Dimension getShapeDimension(Vertex vertex)
    {
        String label = getLabel( vertex );
        
        Font font = getFont( vertex );
        
        FontMetrics fm = graphics.getFontMetrics( getFont( vertex ) );
        
        int width = fm.stringWidth( label );
        int height = fm.getHeight();
        
        width = ( int ) (width + font.getSize());
        height = ( int ) (height + font.getSize());
        
        return new Dimension( width, height );
    }
    
    public Paint getFillPaint( Vertex vertex )
    {
        if ( isStartState( vertex ) )
        {
            return startState.getFill();
        }
        else if ( isEndState( vertex ) )
        {
            return endState.getFill();
        }
        
        return otherState.getFill();
    }

    public Paint getDrawPaint( Vertex vertex )
    {
        if ( isStartState( vertex ) )
        {
            return startState.getStroke();
        }
        else if ( isEndState( vertex ) )
        {
            return endState.getStroke();
        }
        
        return otherState.getStroke();
    }

}
