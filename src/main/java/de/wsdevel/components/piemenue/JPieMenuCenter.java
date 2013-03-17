package de.wsdevel.components.piemenue;

import java.awt.Dimension;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;

import de.wsdevel.components.WSComponent;

/**
 * JPieMenuCenter created on 29.01.2009. for project: Java__Components
 * 
 * @author <a href="mailto:sweiss@scenejo.org">Sebastian A. Weiss -
 *         scenejo.org</a>
 * @version $Author: sweiss $ -- $Revision: 1.2 $ -- $Date: 2009-02-09 13:10:30
 *          $
 * 
 * <br>
 *          (c) 2008, scenejo.org - All rights reserved. Scenejo - An
 *          Interactive Storytelling Framework
 */
public class JPieMenuCenter extends WSComponent<Object> {

    /**
     * {@link long} COMMENT.
     */
    private static final long serialVersionUID = 14087561952050963L;

    /**
     * COMMENT.
     * 
     * @param pieMenu
     *            {@link JPieMenue}
     */
    public JPieMenuCenter(final JPieMenue pieMenu) {
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

    /**
     * COMMENT.
     * 
     * @param newSize
     *            {@link Dimension}
     */
    private void updateSize(final Dimension newSize) {
	final int offset = (int) Math.round(newSize.width * 0.40);
	final int diameter = (int) Math.round(newSize.width * 0.2);
	setBounds(offset, offset, diameter, diameter);
    }

    /**
     * @return {@link Shape}
     * @see de.wsdevel.components.WSComponent#createShape()
     */
    @Override
    protected Shape createShape() {
	return new Ellipse2D.Double(0, 0, getWidth() - 1, getHeight() - 1);
    }
}
//
// $Log: JPieMenuCenter.java,v $
// Revision 1.2 2009-02-09 13:10:30 sweiss
// fixed antialias stuff and added selection rectangle
//
// Revision 1.1 2009-02-02 16:23:38 sweiss
// *** empty log message ***
//
//
