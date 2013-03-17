package de.wsdevel.components.piemenue;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.wsdevel.components.WSRoot;

/**
 * JPieMenue created on 29.01.2009. for project: Java__Components
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
public class JPieMenue extends WSRoot {

    /**
     * {@link long} COMMENT.
     */
    private static final long serialVersionUID = 496947761415851104L;
    private JPieMenuCenter center;

    /**
     * COMMENT.
     */
    public JPieMenue() {
	super();
	setSize(200, 200);
	this.center = new JPieMenuCenter(this);
	add(this.center);
    }

    /**
     * @param comp
     * @param constraints
     * @param index
     * @see java.awt.Container#addImpl(java.awt.Component, java.lang.Object,
     *      int)
     */
    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
	if (comp instanceof JPopupMenu) {
	    remove(this.center);
	    final Component[] components = ((Container) comp).getComponents();
	    double step = 360.0d / components.length;
	    for (int i = 0; i < components.length; i++) {
		if (components[i] instanceof JMenuItem) {
		    add(new JPieMenuSlice(this, (JMenuItem) components[i], i
			    * step, step));
		}
	    }
	    add(this.center);
	} else {
	    super.addImpl(comp, constraints, index);
	}
    }

    /**
     * @param g
     *            {@link Graphics}
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
	// Graphics2D g2d = (Graphics2D) g;
	// g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	// RenderingHints.VALUE_ANTIALIAS_ON);
	//
	// final Ellipse2D.Float circle = new Ellipse2D.Float(0, 0,
	// getWidth() - 1, getHeight() - 1);
	// g2d.setColor(Color.WHITE);
	// g2d.fill(circle);
	// g2d.setColor(Color.LIGHT_GRAY);
	// g2d.draw(circle);

    }
}
//
// $Log: JPieMenue.java,v $
// Revision 1.2 2009-02-09 13:10:30 sweiss
// fixed antialias stuff and added selection rectangle
//
// Revision 1.1 2009-02-02 16:23:38 sweiss
// *** empty log message ***
//
//
