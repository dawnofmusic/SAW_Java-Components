package de.wsdevel.components;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.SortedSet;

import javax.swing.JPanel;

import de.wsdevel.components.selection.SelectionRectangle;

/**
 * WSRoot created on 30.01.2009. for project: Java__Components
 * 
 * @author <a href="mailto:sweiss@scenejo.org">Sebastian A. Weiss -
 *         scenejo.org</a>
 * @version $Author: sweiss $ -- $Revision: 1.3 $ -- $Date: 2009-02-02 16:23:40
 *          $
 * 
 * <br>
 *          (c) 2008, scenejo.org - All rights reserved. Scenejo - An
 *          Interactive Storytelling Framework
 */
public class WSRoot extends JPanel {

    /**
     * MouseMethodCaller created on 30.01.2009. for project: Java__Components
     * 
     * @author <a href="mailto:sweiss@scenejo.org">Sebastian A. Weiss -
     *         scenejo.org</a>
     * @version $Author: sweiss $ -- $Revision: 1.3 $ -- $Date: 2009-02-02
     *          16:23:40 $
     * 
     * <br>
     *          (c) 2008, scenejo.org - All rights reserved. Scenejo - An
     *          Interactive Storytelling Framework
     */
    public static interface MouseMethodCaller {
	/**
	 * COMMENT.
	 * 
	 * @param comp
	 *            {@link WSComponent}
	 * @param p
	 *            {@link Point}
	 */
	void callIt(WSComponent<?> comp, Point p, Point translated);
    }

    /**
     * {@link long} COMMENT.
     */
    private static final long serialVersionUID = -9162364364214913775L;

    /**
     * {@link SelectionRectangle} COMMENT.
     */
    private SelectionRectangle selectionRectangle;

    /**
     * COMMENT.
     */
    public WSRoot() {
	super(null, true);
	setSelectionRectangle(new SelectionRectangle());
	addMouseMotionListener(new MouseMotionAdapter() {
	    @SuppressWarnings("synthetic-access")
	    @Override
	    public void mouseDragged(final MouseEvent e) {
		informChildren(e, new MouseMethodCaller() {
		    public void callIt(WSComponent<?> comp, Point p,
			    Point translated) {
			comp.mouseDragged(p, translated,
				WSRoot.this.selectionRectangle.isVisible(), e);
		    }
		});
		if (WSRoot.this.selectionRectangle != null
			&& WSRoot.this.selectionRectangle.isVisible()) {
		    synchronized (WSRoot.this.selectionRectangle) {
			Rectangle bounds = WSRoot.this.selectionRectangle
				.getBounds();
			Point startPoint = WSRoot.this.selectionRectangle
				.getStartPoint();
			int width2 = e.getX() - startPoint.x;
			int height2 = e.getY() - startPoint.y;
			if (width2 < 0) {
			    bounds.x = startPoint.x + width2;
			    bounds.width = -width2;
			} else {
			    bounds.x = startPoint.x;
			    bounds.width = width2;
			}
			if (height2 < 0) {
			    bounds.y = startPoint.y + height2;
			    bounds.height = -height2;
			} else {
			    bounds.y = startPoint.y;
			    bounds.height = height2;
			}
			WSRoot.this.selectionRectangle.setBounds(bounds);
		    }
		}
	    }

	    @SuppressWarnings("synthetic-access")
	    @Override
	    public void mouseMoved(MouseEvent e) {
		informChildren(e, new MouseMethodCaller() {
		    public void callIt(WSComponent<?> comp, Point p,
			    Point translated) {
			comp.mouseMoved(p, translated);
		    }
		});
	    }

	});
	addMouseListener(new MouseAdapter() {
	    @SuppressWarnings("synthetic-access")
	    @Override
	    public void mouseClicked(final MouseEvent e) {
		informChildren(e, new MouseMethodCaller() {
		    public void callIt(WSComponent<?> comp, Point p,
			    Point translated) {
			comp.mouseClicked(p, translated, e);
		    }
		});
	    }

	    @SuppressWarnings("synthetic-access")
	    @Override
	    public void mouseExited(final MouseEvent e) {
		informChildren(e, new MouseMethodCaller() {
		    public void callIt(WSComponent<?> comp, Point p,
			    Point translated) {
			comp.mouseExited();
		    }
		});
	    }

	    @SuppressWarnings("synthetic-access")
	    @Override
	    public void mouseEntered(final MouseEvent e) {
		informChildren(e, new MouseMethodCaller() {
		    public void callIt(WSComponent<?> comp, Point p,
			    Point translated) {
			comp.mouseEntered(p, translated);
		    }
		});
	    }

	    @SuppressWarnings("synthetic-access")
	    @Override
	    public void mousePressed(MouseEvent e) {
		informChildren(e, new MouseMethodCaller() {
		    public void callIt(WSComponent<?> comp, Point p,
			    Point translated) {
			comp.mousePressed(p, translated);
		    }
		});
		if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) == InputEvent.BUTTON1_DOWN_MASK
			&& WSRoot.this.selectionRectangle != null
			&& e.isShiftDown()) {
		    // selection rectangle trigger
		    synchronized (WSRoot.this.selectionRectangle) {
			WSRoot.this.selectionRectangle.setStartPoint(e
				.getPoint());
			WSRoot.this.selectionRectangle
				.setLocation(e.getPoint());
			WSRoot.this.selectionRectangle.setSize(0, 0);
			WSRoot.this.selectionRectangle.setVisible(true);
		    }
		}
	    }

	    @SuppressWarnings("synthetic-access")
	    @Override
	    public void mouseReleased(MouseEvent e) {
		informChildren(e, new MouseMethodCaller() {
		    public void callIt(WSComponent<?> comp, Point p,
			    Point translated) {
			comp.mouseReleased(p, translated);
		    }
		});
		if (WSRoot.this.selectionRectangle != null) {
		    synchronized (WSRoot.this.selectionRectangle) {
			WSRoot.this.selectionRectangle.setVisible(false);
		    }
		}
	    }
	});
    }

    /**
     * COMMENT.
     * 
     * @param e
     *            {@link MouseEvent}
     * @return <code>boolean true</code>
     */
    @SuppressWarnings("unused")
    private boolean isMouseOverAnySubComponent(final MouseEvent e) {
	boolean isOverAnySubComponent = false;
	for (Component comp : getComponents()) {
	    if (comp instanceof WSComponent<?>) {
		final Point translated = new Point(e.getPoint());
		translated.translate(-comp.getX(), -comp.getY());
		isOverAnySubComponent |= ((WSComponent<?>) comp)
			.isOverShapeOrChildrensShape(translated);
	    }
	}
	return isOverAnySubComponent;
    }

    /**
     * @return {@link SelectionRectangle} the selectionRectangle.
     */
    public SelectionRectangle getSelectionRectangle() {
	return this.selectionRectangle;
    }

    /**
     * COMMENT.
     * 
     * @param e
     *            {@link MouseEvent}
     * @param mouseMethodCaller
     *            {@link MouseMethodCaller}
     */
    private void informChildren(final MouseEvent e,
	    final MouseMethodCaller mouseMethodCaller) {
	for (Component comp : getComponents()) {
	    if (comp instanceof WSComponent<?>) {
		final Point translated = new Point(e.getPoint());
		translated.translate(-comp.getX(), -comp.getY());
		mouseMethodCaller.callIt(((WSComponent<?>) comp), e.getPoint(),
			translated);
	    }
	}
    }

    /**
     * Created on 11.04.2010.
     * 
     * for project: Java__Components
     * 
     * @author <a href="mailto:sweiss@weissundschmidt.de">Sebastian A. Weiss -
     *         Weiss und Schmidt, Mediale Systeme GbR</a>
     * @version $Author: $ -- $Revision: $ -- $Date: $
     * 
     * <br>
     *          (c) 2007, Weiss und Schmidt, Mediale Systeme GbR - All rights
     *          reserved.
     * 
     */
    public static final class ComponentLevelTuple {

	/**
	 * COMMENT.
	 * 
	 * @param level2
	 * @param wsComponent
	 */
	public ComponentLevelTuple(int level2, int zOrderVal,
		WSComponent<?> wsComponent) {
	    this.level = level2;
	    this.zOrder = zOrderVal;
	    this.component = wsComponent;
	}

	/**
	 * {@link int} COMMENT.
	 */
	public int level = 0;

	/**
	 * {@link int} COMMENT.
	 */
	public int zOrder = 0;

	/**
	 * {@link WSComponent<?>} COMMENT.
	 */
	public WSComponent<?> component;
    }

    /**
     * @param translated
     *            {@link Point}
     * @return <code>boolean</code>
     */
    public void addAllComponentsPointIsOver(final Point translated,
	    final SortedSet<ComponentLevelTuple> componentsOver) {
	for (Component comp : getComponents()) {
	    if (comp instanceof WSComponent<?>) {
		final Point translated2 = new Point(translated);
		translated2.translate(-comp.getX(), -comp.getY());
		((WSComponent<?>) comp).addIfIsOverAnySubComponent(1,
			translated2, componentsOver);
	    }
	}
    }

    /**
     * @param selectionRectangleRef
     *            {@link SelectionRectangle} the selectionRectangle to set.
     */
    public void setSelectionRectangle(
	    final SelectionRectangle selectionRectangleRef) {
	if (this.selectionRectangle != null) {
	    remove(this.selectionRectangle);
	    this.selectionRectangle = null;
	}
	this.selectionRectangle = selectionRectangleRef;
	if (this.selectionRectangle != null) {
	    this.selectionRectangle
		    .addComponentListener(new ComponentAdapter() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void componentResized(ComponentEvent e) {
			    for (Component comp : getComponents()) {
				if (comp instanceof WSComponent<?>) {
				    ((WSComponent<?>) comp)
					    .checkIfContainedInSelectionRectangle(WSRoot.this.selectionRectangle
						    .getBounds());
				}
			    }
			}
		    });
	    this.selectionRectangle.setVisible(false);
	    add(this.selectionRectangle);
	    setComponentZOrder(this.selectionRectangle, 0);
	}
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
	super.addImpl(comp, constraints, index);
	if (this.selectionRectangle != null) {
	    setComponentZOrder(this.selectionRectangle, 0);
	}
    }

}
//
// $Log: WSRoot.java,v $
// Revision 1.3 2009-12-09 15:41:56 sweiss
// just some added stuff concerning highlighting
//
// Revision 1.2 2009-05-02 11:25:44 sweiss
// fixes due to compatibility to 1.5
//
// Revision 1.1 2009-02-09 13:10:29 sweiss
// fixed antialias stuff and added selection rectangle
//
// Revision 1.2 2009-02-02 17:13:36 sweiss
// fixed components
//
// Revision 1.1 2009-02-02 16:23:40 sweiss
// *** empty log message ***
//
//
