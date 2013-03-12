package de.wsdevel.components.selection;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import de.wsdevel.components.StrokeImpl;

/**
 * Created on 09.02.2009 for project Java__Components. <br/>
 * (c) 2009, wei&szlig;&amp;schmidt - All rights reserved.
 * 
 * @author <a href="mailto:sweiss@weissundschmidt.de">Sebastian A. Wei&szlig; -
 *         wei&szlig;&amp;schmidt</a>
 * @version $Author: sweiss $ -- $Revision: 1.2 $ -- $Date: 2009-02-09 13:10:30
 *          $
 */
public class SelectionRectangle extends JComponent {

    /**
     * {@link Color} COMMENT.
     */
    private static final Color DEFAULT_BACKGROUND_COLOR = new Color(0f, 1f, 0f,
	    0.1f);

    /**
     * {@link long} COMMENT.
     */
    private static final long serialVersionUID = -2578397446249114962L;

    /**
     * {@link Point} COMMENT.
     */
    private Point startPoint;

    /**
     * {@link StrokeImpl} COMMENT.
     */
    private StrokeImpl stroke;

    /**
     * COMMENT.
     */
    public SelectionRectangle() {
	setBackground(DEFAULT_BACKGROUND_COLOR);
	setForeground(Color.DARK_GRAY);
	setStroke(StrokeImpl.DOTTED);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.awt.Container#addImpl(java.awt.Component, java.lang.Object,
     *      int)
     */
    @Override
    protected void addImpl(final Component comp, final Object constraints,
	    final int index) {
	// nothing can be added to a selection rectangle
    }

    /**
     * @return {@link Point} the startPoint.
     */
    public Point getStartPoint() {
	return this.startPoint;
    }

    /**
     * @return {@link StrokeImpl} the stroke.
     */
    public StrokeImpl getStroke() {
	return this.stroke;
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
	Graphics2D g2d = (Graphics2D) g;
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_OFF);
	g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
	g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
		RenderingHints.VALUE_STROKE_NORMALIZE);
	g2d.setStroke(getStroke());

	int d = (int) Math.ceil(getStroke().getLineWidth());
	Shape s = new Rectangle2D.Double(d, d, getWidth() - (2 * d),
		getHeight() - (2 * d));
	g2d.setColor(getBackground());
	g2d.fill(s);
	g2d.setColor(getForeground());
	g2d.setStroke(getStroke());
	g2d.draw(s);
    }

    /**
     * @param startPointRef
     *            {@link Point} the startPoint to set.
     */
    public void setStartPoint(final Point startPointRef) {
	this.startPoint = startPointRef;
    }

    /**
     * @param strokeRef
     *            {@link StrokeImpl} the stroke to set.
     */
    public void setStroke(final StrokeImpl strokeRef) {
	this.stroke = strokeRef;
    }
}
//
// $Log: SelectionRectangle.java,v $
// Revision 1.2  2009-05-02 11:26:34  sweiss
// added JTwoDSpace
//
// Revision 1.1 2009-02-09 13:10:30 sweiss
// fixed antialias stuff and added selection rectangle
//
//

