package de.wsdevel.components.plotter;

import java.awt.Color;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.wsdevel.tools.math.Graph;
import de.wsdevel.tools.math.ValueTuple;

/**
 * Created on 27.03.2009.
 * 
 * (c) 2008, Sebastian A. Weiss - All rights reserved.
 * 
 * @author <a href="mailto:post@sebastian-weiss.de">Sebastian A. Weiss</a>
 * @version $Author$ -- $Revision$ -- $Date$
 */
public class GraphForComponent {

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
     * {@link GraphListenerSupport}
     */
    private GraphListenerSupport gls = null;

    private Graph model;

    /**
     * {@link float} COMMENT.
     */
    private float strokeWidth = 1.0f;

    /**
     * @param modelRef
     */
    public GraphForComponent(final Graph modelRef) {
	setModel(modelRef);
    }

    /**
     * @param listener
     * @see de.wsdevel.components.plotter.GraphListenerSupport#addListener(de.wsdevel.components.plotter.GraphListener)
     */
    public final void addListener(final GraphListener listener) {
	if (this.gls == null) {
	    this.gls = new GraphListenerSupport();
	}
	this.gls.addListener(listener);
    }

    /**
     * @param tuple
     * @see de.wsdevel.tools.math.Graph#addTuple(de.wsdevel.tools.math.ValueTuple)
     */
    public final void addTuple(final ValueTuple tuple) {
	this.model.addTuple(tuple);
	if (this.gls != null) {
	    this.gls.fireGraphChanged();
	}
    }

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
     * @return the model
     */
    protected Graph getModel() {
	return this.model;
    }

    /**
     * @return {@link float} the strokeWidth.
     */
    public final float getStrokeWidth() {
	return this.strokeWidth;
    }

    /**
     * @return
     * @see de.wsdevel.tools.math.Graph#getTuples()
     */
    public final ArrayBlockingQueue<ValueTuple> getTuples() {
	return this.model.getTuples();
    }

    /**
     * @param listener
     * @see de.wsdevel.components.plotter.GraphListenerSupport#removeListener(de.wsdevel.components.plotter.GraphListener)
     */
    public final void removeListener(final GraphListener listener) {
	if (this.gls != null) {
	    this.gls.removeListener(listener);
	}
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
     * @param model
     *            the model to set
     */
    protected void setModel(final Graph model) {
	this.model = model;
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
