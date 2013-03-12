package de.wsdevel.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created on 10.08.2009.
 * 
 * for project: Java__Graph
 * 
 * @author <a href="mailto:sweiss@weissundschmidt.de">Sebastian A. Weiss - Weiss
 *         und Schmidt, Mediale Systeme GbR</a>
 * @version $Author: sweiss $ -- $Revision: 1.3 $ -- $Date: 2009-08-16 17:47:47
 *          $
 * 
 * <br>
 *          (c) 2007, Weiss und Schmidt, Mediale Systeme GbR - All rights
 *          reserved.
 * 
 */
public class WSResizeHandleComponent extends WSComponent<Object> {

    /**
     * {@link int} COMMENT.
     */
    static int count = 0;

    /**
     * {@link Dimension} default size.
     */
    public static final Dimension DEFAULT_SIZE = new Dimension(8, 8);

    /**
     * <code>long</code> COMMENT.
     */
    private static final long serialVersionUID = -4362058977979988326L;

    /**
     * {@link WSComponent} COMMENT.
     */
    private WSComponent<?> parent = null;

    /**
     * {@link Point} COMMENT.
     */
    private Point pressPoint = null;

    /**
     * {@link Cursor} COMMENT.
     */
    private Cursor oldCursor;

    /**
     * Default constructor.
     */
    public WSResizeHandleComponent(final WSComponent<?> nodeComp) {
	setSize(WSResizeHandleComponent.DEFAULT_SIZE);
	setLocation(0, 0);
	setForeground(Color.DARK_GRAY);
	setBackground(Color.LIGHT_GRAY);
	setHighlightedColor(Color.DARK_GRAY);
	setSelectable(false);
	setMovable(false);
	setAntiAliaseUseChangeableByParents(false);
	setAntiAliasedUsed(false);
	setParent(nodeComp);
	setModel("" + ++WSResizeHandleComponent.count);
    }

    /**
     * @return {@link Shape}
     * @see de.wsdevel.components.WSComponent#createShape()
     */
    @Override
    protected Shape createShape() {
	final int d = (int) Math.ceil(getStroke().getLineWidth());
	return new Rectangle2D.Double(d, d, getWidth() - 2 * d, getHeight() - 2
		* d);
    }

    /**
     * @param p
     *            {@link Point}
     * @param translated
     *            {@link Point}
     * @param selectionMode
     *            <code>boolean</code>
     * @see de.wsdevel.components.WSComponent#mouseDragged(java.awt.Point,
     *      java.awt.Point, boolean)
     */
    @Override
    public void mouseDragged(final Point p, final Point translated,
	    final boolean selectionMode, final MouseEvent evt) {
	if (this.pressPoint != null && this.parent.isResizable()) {
	    final int dX = translated.x - this.pressPoint.x;
	    final int dY = translated.y - this.pressPoint.y;
	    this.parent.setSize(this.parent.getWidth() + dX,
		    this.parent.getHeight() + dY);
	}
    }

    /**
     * @param p
     *            {@link Point}
     * @param translated
     *            {@link Point}
     * @see de.wsdevel.components.WSComponent#mouseEntered(java.awt.Point,
     *      java.awt.Point)
     */
    @Override
    public void mouseEntered(final Point p, final Point translated) {
	super.mouseEntered(p, translated);
	this.oldCursor = getCursor();
	setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
    }

    /**
     * @see de.wsdevel.components.WSComponent#mouseExited()
     */
    @Override
    public void mouseExited() {
	super.mouseExited();
	setCursor(this.oldCursor);
    }

    /**
     * @param p
     *            {@link Point}
     * @param translated
     *            {@link Point}
     * @see de.wsdevel.components.WSComponent#mousePressed(java.awt.Point,
     *      java.awt.Point)
     */
    @Override
    public final void mousePressed(final Point p, final Point translated) {
	if (isOverShape(translated)) {
	    this.pressPoint = translated;
	    setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
	}
    }

    /**
     * @param p
     *            {@link Point}
     * @param translated
     *            {@link Point}
     * @see de.wsdevel.components.WSComponent#mouseReleased(java.awt.Point,
     *      java.awt.Point)
     */
    @Override
    public final void mouseReleased(final Point p, final Point translated) {
	this.pressPoint = null;
	setCursor(this.oldCursor);
    }

    /**
     * COMMENT.
     * 
     * @param parentRef
     *            {@link DefaultNodeComponent}
     */
    public void setParent(final WSComponent<?> parentRef) {
	this.parent = parentRef;
	this.parent.add(this);
	updateLocation(this.parent);
	this.parent.addComponentListener(new ComponentAdapter() {
	    @SuppressWarnings("synthetic-access")
	    @Override
	    public void componentResized(final ComponentEvent arg0) {
		// System.out.println("hc: " + HandleComponent.this.getModel() +
		// " pc: "
		// + arg0.getComponent());// TODO remove sysout
		updateLocation(WSResizeHandleComponent.this.parent);
	    }
	});
	setVisible(this.parent.isResizable());
	this.parent.addPropertyChangeListener(
		WSComponent.PROPERTY_NAME_RESIZABLE,
		new PropertyChangeListener() {
		    @SuppressWarnings("synthetic-access")
		    public void propertyChange(final PropertyChangeEvent evt) {
			if (WSResizeHandleComponent.this.parent.isResizable()) {
			    setVisible(true);
			} else {
			    setVisible(false);
			}
		    }
		});
    }

    /**
     * COMMENT.
     * 
     * @param nodeCompRef
     *            {@link Component}
     */
    private void updateLocation(final Component nodeCompRef) {
	setLocation(nodeCompRef.getWidth() - getWidth(),
		nodeCompRef.getHeight() - getHeight());
    }

}
//
// $Log: HandleComponent.java,v $
// Revision 1.3 2009-12-09 15:56:53 sweiss
// cleanup and moved a lot of layout stuff to src
//
// Revision 1.2 2009-11-23 13:49:44 sweiss
// *** empty log message ***
//
// Revision 1.1 2009-08-16 17:47:47 sweiss
// added resize handle
//
//
