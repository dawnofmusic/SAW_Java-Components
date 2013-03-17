package de.wsdevel.components.piemenue;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JMenuItem;

import de.wsdevel.components.WSComponent;

/**
 * JPieMenuSlice created on 29.01.2009. for project: Java__Components
 * 
 * @author <a href="mailto:sweiss@scenejo.org">Sebastian A. Weiss -
 *         scenejo.org</a>
 * @version $Author: sweiss $ -- $Revision: 1.4 $ -- $Date: 2009-02-02 16:23:38
 *          $
 * 
 * <br>
 *          (c) 2008, scenejo.org - All rights reserved. Scenejo - An
 *          Interactive Storytelling Framework
 */
public class JPieMenuSlice extends WSComponent<Object> {

    /**
     * {@link long} COMMENT.
     */
    private static final long serialVersionUID = -2662495520170279155L;

    private double start = 0;
    private double extend = 0;

    /**
     * {@link JMenuItem} COMMENT.
     */
    private JMenuItem innerItem;

    /**
     * COMMENT.
     * 
     * @param pieMenu
     * @param startVal
     * @param extendVal
     */
    public JPieMenuSlice(JPieMenue pieMenu, final JMenuItem innerItemRef,
	    double startVal, double extendVal) {
	this.start = startVal;
	this.extend = extendVal;
	this.innerItem = innerItemRef;

	updateSize(pieMenu.getSize());
	pieMenu.addComponentListener(new ComponentAdapter() {
	    @SuppressWarnings("synthetic-access")
	    @Override
	    public void componentResized(ComponentEvent e) {
		final Dimension newSize = e.getComponent().getSize();
		updateSize(newSize);
	    }
	});
	setMovable(false);
	setSelectable(false);
    }

    @Override
    public void mousePressed(Point p, Point translated) {
	if (createShape().contains(translated)) {
	    System.out.println("clicked");// TODO remove sysout
	    this.innerItem.doClick();
	    // final Action action = this.innerItem.getAction();
	    // if (action != null) {
	    // action.actionPerformed(new ActionEvent(this, 0, ""));
	    // }else{
	    // }
	    //
	}
    }

    /**
     * COMMENT.
     * 
     * @param newSize
     *            {@link Dimension}
     */
    private void updateSize(final Dimension newSize) {
	setSize(newSize);
    }

    /**
     * @return {@link Shape}
     * @see de.wsdevel.components.WSComponent#createShape()
     */
    @Override
    protected Shape createShape() {
	final Arc2D.Double shape = new Arc2D.Double(0, 0, getWidth() - 1,
		getHeight() - 1, this.start, this.extend, Arc2D.PIE);
	return shape;
    }

    @Override
    protected void implementationDependentPainting(final Graphics2D g2d) {
	final double theta = Math.PI * ((this.start + (this.extend / 2)) / 180);
	g2d.setColor(getForeground());
	g2d.rotate(-theta, getWidth() / 2.0d, getHeight() / 2.0d);
	final Rectangle2D stringBounds = g2d.getFontMetrics().getStringBounds(
		this.innerItem.getText(), g2d);
	g2d.drawString(
		this.innerItem.getText(),
		((getWidth() / 2.0f) + ((getWidth() / 2.0f) - (float) stringBounds
			.getWidth()) / 2.0f), getHeight() / 2.0f
			+ g2d.getFontMetrics().getAscent() / 2.0f);
	g2d.rotate(theta, getWidth() / 2.0d, getHeight() / 2.0d);
    }
}
//
// $Log: JPieMenuSlice.java,v $
// Revision 1.4 2009-02-09 16:14:42 sweiss
// bug fixing
//
// Revision 1.3 2009-02-09 13:10:30 sweiss
// fixed antialias stuff and added selection rectangle
//
// Revision 1.2 2009-02-02 17:13:30 sweiss
// fixed components
//
// Revision 1.1 2009-02-02 16:23:38 sweiss
// *** empty log message ***
//
//
