package de.wsdevel.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import de.wsdevel.components.selection.SelectionHolder;

/**
 * Created on 22.11.2008.
 * 
 * for project: Scenejo__Model <br>
 * (c) 2007, Weiss und Schmidt, Mediale Systeme GbR - All rights reserved.
 * 
 * @author <a href="mailto:sweiss@weissundschmidt.de">Sebastian A. Weiss - Weiss
 *         und Schmidt, Mediale Systeme GbR</a>
 * @version $Author: sweiss $ -- $Revision: 1.2 $ -- $Date: 2009-02-02 16:23:40
 *          $
 */
public class WSHandle<M> extends WSComponent<M> {

    /**
     * {@link Dimension} COMMENT.
     */
    private static final Dimension DEFAULT_HANDLE_DIMENSION = new Dimension(6,
	    6);

    /**
     * {@link long} COMMENT.
     */
    private static final long serialVersionUID = 1L;

    /**
     * <code>boolean</code> COMMENT.
     */
    private boolean oval = false;

    /**
     * COMMENT.
     * 
     * @param userObjectRef
     *            <code>M</code>
     */
    public WSHandle(final M userObjectRef,
	    final SelectionHolder selectionHolderRef) {
	setModel(userObjectRef);
	setSize(DEFAULT_HANDLE_DIMENSION);
	setLocation(0, 0);
	setSelectionHolder(selectionHolderRef);
	setForeground(Color.DARK_GRAY);
	setOval(false);
	setAntiAliaseUseChangeableByParents(false);
	setAntiAliasedUsed(false);
    }

    /**
     * COMMENT.
     */
    public WSHandle(final SelectionHolder selectionHolderRef) {
	this(null, selectionHolderRef);
    }

    @Override
    protected Shape createShape() {
	int d = (int) Math.ceil(getStroke().getLineWidth());
	Shape s = null;
	if (isOval()) {
	    s = new Ellipse2D.Double(d, d, getWidth() - (2 * d), getHeight()
		    - (2 * d));
	} else {
	    s = new Rectangle2D.Double(d, d, getWidth() - (2 * d), getHeight()
		    - (2 * d));
	}
	return s;
    }

    /**
     * @return {@link boolean} the oval.
     */
    public final boolean isOval() {
	return this.oval;
    }

    /**
     * COMMENT.
     * 
     * @param xCoor
     *            <code>int</code>
     * @param yCoor
     *            <code>int</code>
     */
    public void setCenter(final int xCoor, final int yCoor) {
	setLocation(xCoor - getWidth() / 2, yCoor - getHeight() / 2);
    }

    /**
     * @param ovalVal
     *            {@link boolean} the oval to set.
     */
    public final void setOval(final boolean ovalVal) {
	this.oval = ovalVal;
    }

}
//
// $Log: WSHandle.java,v $
// Revision 1.2 2009-02-09 13:10:29 sweiss
// fixed antialias stuff and added selection rectangle
//
// Revision 1.1 2009-02-02 16:23:40 sweiss
// *** empty log message ***
//
//
