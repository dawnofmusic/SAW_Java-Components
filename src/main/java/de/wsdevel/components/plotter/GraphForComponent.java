package de.wsdevel.components.plotter;

import java.awt.Color;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.wsdevel.tools.math.Graph;

/**
 * Created on 27.03.2009.
 * 
 * (c) 2008, Sebastian A. Weiss - All rights reserved.
 * 
 * @author <a href="mailto:post@sebastian-weiss.de">Sebastian A. Weiss</a>
 * @version $Author$ -- $Revision$ -- $Date$
 */
public class GraphForComponent extends Graph {

    /**
     * {@link Log} the logger.
     */
    @SuppressWarnings("unused")
    private static final Log LOG = LogFactory.getLog(GraphForComponent.class);

    /**
     * {@link Color} COMMENT.
     */
    private Color color = Color.BLACK;

    /** {@link Color} The fillColor. */
    private Color fillColor = new Color(200, 200, 200, 50);

    /**
     * {@link float} COMMENT.
     */
    private float strokeWidth = 1.0f;

    /**
     * @return {@link Color} the color.
     */
    public final Color getColor() {
	return this.color;
    }

    /**
     * Returns the fillColor.
     * 
     * @return {@link Color}
     */
    public Color getFillColor() {
	return this.fillColor;
    }

    /**
     * @return {@link float} the strokeWidth.
     */
    public final float getStrokeWidth() {
	return this.strokeWidth;
    }

    /**
     * @param colorRef
     *            {@link Color} the color to set.
     */
    public final void setColor(final Color colorRef) {
	this.color = colorRef;
    }

    /**
     * Sets the fillColor.
     * 
     * @param fillColor
     *            {@link Color}
     */
    public void setFillColor(final Color fillColor) {
	this.fillColor = fillColor;
    }

    /**
     * @param strokeWidthVal
     *            {@link float} the strokeWidth to set.
     */
    public final void setStrokeWidth(final float strokeWidthVal) {
	this.strokeWidth = strokeWidthVal;
    }

}
//
// $Log$
// Revision 1.3 2009-04-09 09:57:31 sweiss
// real time logging of probes
//
// Revision 1.2 2009-04-01 11:57:25 sweiss
// added new uistuff
//
// Revision 1.1 2009-03-28 16:09:24 sweiss
// added a lot of graph stuff
//
//
