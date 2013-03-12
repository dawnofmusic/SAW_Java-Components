package de.wsdevel.components;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.SortedSet;

import javax.swing.JComponent;

import de.wsdevel.components.WSRoot.MouseMethodCaller;
import de.wsdevel.components.selection.SelectionHolder;

/**
 * WSComponent created on 05.12.2008. for project: Java__Components <br>
 * (c) 2008, scenejo.org - All rights reserved. Scenejo - An Interactive
 * Storytelling Framework
 * 
 * @author <a href="mailto:sweiss@scenejo.org">Sebastian A. Weiss -
 *         scenejo.org</a>
 * @version $Author: sweiss $ -- $Revision: 1.9 $ -- $Date: 2009-02-02 16:23:40
 *          $
 * @param <M>
 */
public class WSComponent<M> extends JComponent {

    /**
     * {@link String} COMMENT.
     */
    public static final String PROPERTY_NAME_RESIZABLE = "resizable";

    /**
     * {@link String} COMMENT.
     */
    public static final String PROPERTY_NAME_FILTERED = "filtered";

    /**
     * {@link String} COMMENT.
     */
    public static final String PROPERTY_NAME_HIGHLIGHTED = "highlighted";

    /**
     * {@link String} COMMENT.
     */
    public static final String PROPERTY_NAME_SELECTED = "selected";

    /**
     * {@link long} COMMENT.
     */
    private static final long serialVersionUID = -3895478917698721603L;

    /**
     * <code>boolean</code> flag determining whether component should be
     * rendered using anti-alias.
     */
    private boolean antiAliasedUsed = true;

    /**
     * <code>boolean</code> flag determining whether component the usage of
     * antialias should be changeable by parent components.
     */
    private boolean antiAliaseUseChangeableByParents = true;

    /**
     * {@link WSComponentSupport} COMMENT.
     */
    private final WSComponentSupport wscs;

    /**
     * <code>boolean</code> filtered flag.
     */
    private boolean filtered = false;

    /**
     * {@link Color} foreground color filtered mode.
     */
    private Color filteredColor;

    /**
     * <code>boolean</code> flag whether component can be highlighted at all.
     */
    private boolean highlightable = true;

    /**
     * <code>boolean</code> highlighted flag.
     */
    private boolean highlighted = false;

    /**
     * <code>boolean</code> determines whether this node is resizable or not.
     */
    private boolean resizable = true;

    /**
     * {@link Color} foreground color in highlighted mode.
     */
    private Color highlightedColor;

    /**
     * {@link Point}
     */
    private Point locationInParentSpaceWhenMousePressed;

    /**
     * <code>M</code> COMMENT.
     */
    private M model;

    /**
     * <code>boolean</code> <code>true</code> if movable.
     */
    private boolean movable = true;

    /**
     * {@link Dimension} COMMENT.
     */
    private Dimension moveAlongRaster = null;

    /**
     * <code>boolean</code>
     */
    private boolean moveMode = false;

    /**
     * {@link Point}
     */
    private Point pointMousePressedInParentSpace;

    /**
     * <code>boolean</code> COMMENT.
     */
    private boolean restrictedToParentsVisibleRect = true;

    /**
     * <code>boolean</code> flag whether component can be selected at all.
     */
    private boolean selectable = true;

    /**
     * <code>boolean</code> selected flag.
     */
    private boolean selected = false;

    /**
     * {@link Color} foreground color in selected and highlighted mode.
     */
    private Color selectedAndHighlightedColor;

    /**
     * {@link Color} foreground color in selected mode.
     */
    private Color selectedColor;

    /**
     * {@link SelectionHolder} COMMENT.
     */
    private transient SelectionHolder selectionHolder = null;

    /**
     * {@link StrokeImpl} COMMENT.
     */
    private StrokeImpl stroke = StrokeImpl.SIMPLE;

    /**
     * <code>boolean</code> COMMENT.
     */
    private boolean xCoorFixed = false;

    /**
     * <code>boolean</code> COMMENT.
     */
    private boolean yCoorFixed = false;

    private final ComponentAdapter evtDelegate;

    /**
     * COMMENT.
     * 
     */
    public WSComponent() {
	super();
	this.wscs = new WSComponentSupport();
	setLocation(0, 0);
	setSize(10, 10);
	setForeground(Color.BLACK);
	setBackground(Color.WHITE);
	setHighlightedColor(Color.ORANGE);
	setSelectedColor(Color.RED);
	setSelectedAndHighlightedColor(Color.MAGENTA);
	setOpaque(false);
	setResizable(false);

	if (!(this instanceof WSResizeHandleComponent || this instanceof WSHandle)) {
	    new WSResizeHandleComponent(this);
	}

	this.evtDelegate = new ComponentAdapter() {
	    @SuppressWarnings("synthetic-access")
	    @Override
	    public void componentMoved(final ComponentEvent e) {
		WSComponent.this.wscs
			.fireWSComponentMoved(new WSComponentEvent(
				WSComponent.this, getLocation(),
				getLocationInRootSpace(), getCenter(),
				getCenterInRootSpace(), getSize()));
	    }
	};
	addComponentListener(this.evtDelegate);
	addComponentListener(new ComponentAdapter() {
	    @SuppressWarnings("synthetic-access")
	    @Override
	    public void componentResized(ComponentEvent e) {
		WSComponent.this.wscs
			.fireWSComponentResized(new WSComponentEvent(
				WSComponent.this, getLocation(),
				getLocationInRootSpace(), getCenter(),
				getCenterInRootSpace(), getSize()));
	    }
	});
    }

    /**
     * COMMENT.
     * 
     * @param wsComponent
     *            {@link WSComponent}
     * @param evtDelegateRef
     *            {@link ComponentListener}
     */
    private void addEVTDelegateToChain(final ComponentListener evtDelegateRef) {
	addComponentListener(evtDelegateRef);
	final Container parent = getParent();
	if (parent == null || !(parent instanceof WSComponent<?>)
		|| parent instanceof WSRoot) {
	    return;
	}
	((WSComponent<?>) parent).addEVTDelegateToChain(evtDelegateRef);
    }

    /**
     * @param translated
     *            {@link Point}
     * @return <code>boolean</code>
     */
    public void addIfIsOverAnySubComponent(final int level,
	    final Point translated,
	    final SortedSet<WSRoot.ComponentLevelTuple> componentsOver) {
	for (final Component comp : getComponents()) {
	    if (comp instanceof WSComponent<?>) {
		final Point translated2 = new Point(translated);
		translated2.translate(-comp.getX(), -comp.getY());
		((WSComponent<?>) comp).addIfIsOverAnySubComponent(level + 1,
			translated2, componentsOver);
	    }
	}
	if (isOverShape(translated)) {
	    componentsOver.add(new WSRoot.ComponentLevelTuple(level,
		    getParent().getComponentZOrder(this), this));
	}
    }

    /**
     * @see javax.swing.JComponent#addNotify()
     */
    @Override
    public void addNotify() {
	super.addNotify();
	removeComponentListener(this.evtDelegate);
	addEVTDelegateToChain(this.evtDelegate);
    }

    /**
     * @param listener
     *            {@link WSComponentListener}
     * @see de.wsdevel.components.WSComponentSupport#addWSComponentListener(de.wsdevel.components.WSComponentListener)
     */
    public final void addWSComponentListener(final WSComponentListener listener) {
	this.wscs.addWSComponentListener(listener);
    }

    /**
     * COMMENT.
     * 
     * @param selectionRectangleBoundsInParentSpace
     *            {@link Rectangle}
     */
    public void checkIfContainedInSelectionRectangle(
	    final Rectangle selectionRectangleBoundsInParentSpace) {
	if (selectionRectangleBoundsInParentSpace.contains(getBounds())) {
	    setSelected(true);
	} else {
	    setSelected(false);
	}
	final Rectangle translated = new Rectangle(
		selectionRectangleBoundsInParentSpace);
	translated.translate(-getX(), -getY());
	for (final Component comp : getComponents()) {
	    if (comp instanceof WSComponent<?>) {
		((WSComponent<?>) comp)
			.checkIfContainedInSelectionRectangle(translated);
	    }
	}
    }

    /**
     * COMMENT.
     * 
     * @return {@link Shape}
     */
    protected Shape createShape() {
	final int d = (int) Math.ceil(getStroke().getLineWidth());
	// return new Rectangle2D.Double(d, d, getWidth() - (2 * d), getHeight()
	// - (2 * d));
	return new Rectangle2D.Double(d, d, getWidth() - d, getHeight() - d);
    }

    /**
     * COMMENT.
     * 
     */
    public final void disableEvents() {
	disableEvents(AWTEvent.COMPONENT_EVENT_MASK);
    }

    /**
     * COMMENT.
     * 
     */
    public final void enableEvents() {
	enableEvents(AWTEvent.COMPONENT_EVENT_MASK);
    }

    /**
     * COMMENT.
     * 
     * @return {@link Rectangle}
     */
    public final Rectangle getBoundsInRootSpace() {
	final Rectangle bounds = getBounds();
	bounds.setLocation(getLocationInRootSpace());
	return bounds;
    }

    /**
     * COMMENT.
     *
     * @return {@link Point2D.Double}
     */
    public final Point2D.Double getCenter() {
	return WSHelper.getCenterOfComponent(getLocation(), this);
    }

    /**
     * COMMENT.
     *
     * @return {@link Point2D.Double}
     */
    public final Point2D.Double getCenterInRootSpace() {
	return WSHelper.getCenterOfComponent(getLocationInRootSpace(), this);
    }

    /**
     * @return {@link Color} the filteredColor.
     */
    public final Color getFilteredColor() {
	return this.filteredColor;
    }

    /**
     * @return {@link Color} the highlightedColor.
     */
    public final Color getHighlightedColor() {
	return this.highlightedColor;
    }

    /**
     * COMMENT.
     * 
     * @return {@link Point}
     */
    public final Point getLocationInRootSpace() {
	final Point point = getLocation();
	final Container parent = getParent();
	if (parent == null || !(parent instanceof WSComponent<?>)
		|| parent instanceof WSRoot) {
	    return point;
	}
	final Point parentsLocationInRootSpace = ((WSComponent<?>) parent)
		.getLocationInRootSpace();
	point.translate(parentsLocationInRootSpace.x,
		parentsLocationInRootSpace.y);
	return point;
    }

    /**
     * @return {@link M} the model.
     */
    public final M getModel() {
	return this.model;
    }

    /**
     * @return {@link Dimension} the moveAlongRaster.
     */
    public final Dimension getMoveAlongRaster() {
	return this.moveAlongRaster;
    }

    /**
     * @return {@link Color} the selectedAndHighlightedColor.
     */
    public final Color getSelectedAndHighlightedColor() {
	return this.selectedAndHighlightedColor;
    }

    /**
     * @return {@link Color} the selectedColor.
     */
    public final Color getSelectedColor() {
	return this.selectedColor;
    }

    /**
     * @return {@link SelectionHolder} the selectionHolder.
     */
    public final SelectionHolder getSelectionHolder() {
	return this.selectionHolder;
    }

    /**
     * @return {@link StrokeImpl} the stroke.
     */
    protected final StrokeImpl getStroke() {
	return this.stroke;
    }

    /**
     * @return <code>boolean</code>. Maybe overwritten by child classes to do
     *         some implementation depending checks.
     */
    protected boolean implementationDependentMoveCheckOnMousePressed() {
	return true;
    }

    /**
     * COMMENT.
     * 
     * @param g2d
     *            {@link Graphics2D}
     */
    protected void implementationDependentPainting(final Graphics2D g2d) {
    }

    /**
     * COMMENT.
     * 
     * @param e
     *            {@link MouseEvent}
     * @param mouseMethodCaller
     *            {@link MouseMethodCaller}
     */
    private void informChildren(final Point p, final Point translated,
	    final MouseMethodCaller mouseMethodCaller) {
	for (final Component comp : getComponents()) {
	    if (comp instanceof WSComponent<?>) {
		final Point translated2 = new Point(translated);
		translated2.translate(-comp.getX(), -comp.getY());
		mouseMethodCaller.callIt(((WSComponent<?>) comp), p,
			translated2);
	    }
	}
    }

    /**
     * @return {@link boolean} the antiAliasedUsed.
     */
    public final boolean isAntiAliasedUsed() {
	return this.antiAliasedUsed;
    }

    /**
     * @return {@link boolean} the antiAliaseUseChangeableByParents.
     */
    public boolean isAntiAliaseUseChangeableByParents() {
	return this.antiAliaseUseChangeableByParents;
    }

    /**
     * @return {@link boolean} the filtered.
     */
    public final boolean isFiltered() {
	return this.filtered;
    }

    /**
     * @return {@link boolean} the highlightable.
     */
    public final boolean isHighlightable() {
	return this.highlightable;
    }

    /**
     * @return {@link boolean} the highlighted.
     */
    public final boolean isHighlighted() {
	return this.highlighted;
    }

    /**
     * @return {@link boolean} the moveable.
     */
    public final boolean isMovable() {
	return this.movable;
    }

    /**
     * @param translated
     * @return <code>boolean</code>
     */
    private boolean isOverAnySubComponent(final Point translated) {
	boolean isOverAnySubComponent = false;
	for (final Component comp : getComponents()) {
	    if (comp instanceof WSComponent<?>) {
		final Point translated2 = new Point(translated);
		translated2.translate(-comp.getX(), -comp.getY());
		isOverAnySubComponent |= ((WSComponent<?>) comp)
			.isOverShapeOrChildrensShape(translated2);
	    }
	}
	return isOverAnySubComponent;
    }

    /**
     * @param translated
     *            {@link Point} in component's space
     * @return <code>true</code> if point is contained in component's shape.
     */
    public boolean isOverShape(final Point translated) {
	return createShape().contains(translated);
    }

    /**
     * @param translated
     *            {@link Point} in component's space
     * @return <code>true</code> if point is contained in component's shape.
     */
    public boolean isOverShapeOrChildrensShape(final Point translated) {
	if (isOverShape(translated)) {
	    return true;
	}
	return isOverAnySubComponent(translated);
    }

    /**
     * @return <code>boolean</code> <code>true</code> if this component is
     *         resizable.
     */
    public final boolean isResizable() {
	return this.resizable;
    }

    /**
     * @return {@link boolean} the restrictedToParentsVisibleRect.
     */
    public final boolean isRestrictedToParentsVisibleRect() {
	return this.restrictedToParentsVisibleRect;
    }

    /**
     * @return {@link boolean} the selectable.
     */
    public final boolean isSelectable() {
	return this.selectable;
    }

    /**
     * @return {@link boolean} the selected.
     */
    public final boolean isSelected() {
	if (isSelectable()) {
	    return this.selected;
	}
	return false;
    }

    /**
     * @return {@link boolean} the xCoorFixed.
     */
    public final boolean isXCoorFixed() {
	return this.xCoorFixed;
    }

    /**
     * @return {@link boolean} the yCoorFixed.
     */
    public final boolean isYCoorFixed() {
	return this.yCoorFixed;
    }

    /**
     * COMMENT.
     * 
     * @param p
     *            {@link Point} in parent's space
     * @param translated
     *            {@link Point} in component's space
     */
    public void mouseClicked(final Point p, final Point translated,
	    final MouseEvent e) {
	informChildren(p, translated, new MouseMethodCaller() {
	    public void callIt(final WSComponent<?> comp, final Point p2,
		    final Point translated2) {
		comp.mouseClicked(p2, translated2, e);
	    }
	});
	if (isSelectable() && isOverShape(translated)
		&& !isOverAnySubComponent(translated)) {
	    setSelected(!isSelected());
	} else if (isSelectable()) {
	    if (e.isControlDown()) {
		// do nothing (sw)
	    } else if (e.isShiftDown()) {
		// SEBASTIAN select range!
	    } else {
		setSelected(false);
	    }
	}
    }

    /**
     * COMMENT.
     * 
     * @param p
     *            {@link Point} in parent's space
     * @param translated
     *            {@link Point} in component's space
     */
    @SuppressWarnings("rawtypes")
    public void mouseDragged(final Point p, final Point translated,
	    final boolean selectionMode, final MouseEvent evt) {
	informChildren(p, translated, new MouseMethodCaller() {
	    public void callIt(final WSComponent<?> comp, final Point p2,
		    final Point translated2) {
		comp.mouseDragged(p2, translated2, selectionMode, evt);
	    }
	});
	setHighlighted(isOverShape(translated));
	if (!selectionMode) {
	    if (isSelected()) {
		moveMe(p);
	    } else if (this.moveMode) {
		int lowestZ = Integer.MAX_VALUE;
		for (final Component comp : getParent().getComponents()) {
		    if (comp instanceof WSComponent) {
			final WSComponent sibling = (WSComponent) comp;
			final int componentZOrder = getParent()
				.getComponentZOrder(sibling);
			if (sibling.moveMode && componentZOrder < lowestZ) {
			    lowestZ = componentZOrder;
			}
		    }
		}
		if (getParent().getComponentZOrder(this) <= lowestZ) {
		    moveMe(p);
		}
	    }
	}
    }

    /**
     * COMMENT.
     * 
     * @param p
     *            {@link Point} in parent's space
     * @param translated
     *            {@link Point} in component's space
     */
    public void mouseEntered(final Point p, final Point translated) {
	informChildren(p, translated, new MouseMethodCaller() {
	    public void callIt(final WSComponent<?> comp, final Point p2,
		    final Point translated2) {
		comp.mouseEntered(p2, translated2);
	    }
	});
	setHighlighted(isOverShape(translated));
    }

    /**
     * COMMENT.
     */
    public void mouseExited() {
	for (final Component comp : getComponents()) {
	    if (comp instanceof WSComponent<?>) {
		((WSComponent<?>) comp).mouseExited();
	    }
	}
	setHighlighted(false);
    }

    /**
     * COMMENT.
     * 
     * @param p
     *            {@link Point} in parent's space
     * @param translated
     *            {@link Point} in component's space
     */
    public void mouseMoved(final Point p, final Point translated) {
	informChildren(p, translated, new MouseMethodCaller() {
	    public void callIt(final WSComponent<?> comp, final Point p2,
		    final Point translated2) {
		comp.mouseMoved(p2, translated2);
	    }
	});
	setHighlighted(isOverShape(translated));
    }

    /**
     * COMMENT.
     * 
     * @param p
     *            {@link Point}
     * @param translated
     *            {@link Point}
     */
    public void mousePressed(final Point p, final Point translated) {
	// SEBASTIAN take care of move mode!!!
	informChildren(p, translated, new MouseMethodCaller() {
	    public void callIt(final WSComponent<?> comp, final Point p2,
		    final Point translated2) {
		comp.mousePressed(p2, translated2);
	    }
	});
	this.pointMousePressedInParentSpace = p;
	this.locationInParentSpaceWhenMousePressed = getLocation();
	if (isMovable() && isOverShape(translated)
		&& !isOverAnySubComponent(translated)
		&& implementationDependentMoveCheckOnMousePressed()) {
	    this.moveMode = true;
	}
    }

    /**
     * @param p
     *            {@link Point} in parent's space
     * @param translated
     *            {@link Point} in component's space
     */
    public void mouseReleased(final Point p, final Point translated) {
	informChildren(p, translated, new MouseMethodCaller() {
	    public void callIt(final WSComponent<?> comp, final Point p2,
		    final Point translated2) {
		comp.mouseReleased(p2, translated2);
	    }
	});
	this.moveMode = false;
	placeIntoRaster();
    }

    /**
     * Moves component by delta between current mouse position and the last
     * location of this component when a mouse button has been pressed.
     * 
     * @param currentMouseLocation
     *            {@link Point}
     */
    private void moveMe(final Point currentMouseLocation) {
	final Point delta = new Point(0, 0);
	if (!isXCoorFixed()) {
	    delta.x += currentMouseLocation.x
		    - this.pointMousePressedInParentSpace.x;
	}
	if (!isYCoorFixed()) {
	    delta.y += currentMouseLocation.y
		    - this.pointMousePressedInParentSpace.y;
	}
	if (isRestrictedToParentsVisibleRect()) {
	    final Dimension size = getParent().getSize();
	    if (delta.x + this.locationInParentSpaceWhenMousePressed.x < 0) {
		delta.x = -this.locationInParentSpaceWhenMousePressed.x;
	    }
	    final int maxX = size.width - getWidth();
	    if (delta.x + this.locationInParentSpaceWhenMousePressed.x > maxX) {
		delta.x = maxX - this.locationInParentSpaceWhenMousePressed.x;
	    }
	    if (delta.y + this.locationInParentSpaceWhenMousePressed.y < 0) {
		delta.y = -this.locationInParentSpaceWhenMousePressed.y;
	    }
	    final int maxY = size.height - getHeight();
	    if (delta.y + this.locationInParentSpaceWhenMousePressed.y > maxY) {
		delta.y = maxY - this.locationInParentSpaceWhenMousePressed.y;
	    }
	}
	setLocation(this.locationInParentSpaceWhenMousePressed.x + delta.x,
		this.locationInParentSpaceWhenMousePressed.y + delta.y);
    }

    /**
     * @param g
     *            {@link Graphics}
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
	final Graphics2D g2d = (Graphics2D) g;
	if (isAntiAliasedUsed()) {
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		    RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		    RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
	} else {
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		    RenderingHints.VALUE_ANTIALIAS_OFF);
	    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		    RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
	}
	g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
		RenderingHints.VALUE_STROKE_NORMALIZE);
	g2d.setStroke(getStroke());

	if (isSelected() && isHighlighted()) {
	    g2d.setColor(getSelectedAndHighlightedColor());
	} else if (isHighlighted()) {
	    g2d.setColor(getHighlightedColor());
	} else if (isSelected()) {
	    g2d.setColor(getSelectedColor());
	} else if (isFiltered()) {
	    g2d.setColor(getFilteredColor());
	} else {
	    g2d.setColor(getBackground());
	}

	final Shape shape = createShape();
	g2d.fill(shape);

	implementationDependentPainting(g2d);

	g2d.setColor(getForeground());
	g2d.draw(shape);
	// if (isAntiAliasedUsed()) {
	// g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	// RenderingHints.VALUE_ANTIALIAS_OFF);
	// }
    }

    /**
     * COMMENT.
     */
    private void placeIntoRaster() {
	final Dimension moveAlongRaster2 = getMoveAlongRaster();
	if (moveAlongRaster2 != null) {
	    final Point l = getLocation();

	    int dX = l.x % moveAlongRaster2.width;
	    if (dX > moveAlongRaster2.width / 2.0f) {
		dX = dX - moveAlongRaster2.width;
	    }
	    int dY = l.y % moveAlongRaster2.height;
	    if (dY > moveAlongRaster2.height / 2.0f) {
		dY = dY - moveAlongRaster2.height;
	    }

	    setLocation(l.x - dX, l.y - dY);
	}
    }

    /**
     * @param modelRef
     *            M
     */
    protected void registerToModel(final M modelRef) {
    }

    /**
     * @param listener
     *            {@link WSComponentListener}
     * @see de.wsdevel.components.WSComponentSupport#removeWSComponentListener(de.wsdevel.components.WSComponentListener)
     */
    public final void removeWSComponentListener(
	    final WSComponentListener listener) {
	this.wscs.removeWSComponentListener(listener);
    }

    /**
     * @param antiAliasedUsedVal
     *            {@link boolean} the antiAliasedUsed to set.
     */
    public final void setAntiAliasedUsed(final boolean antiAliasedUsedVal) {
	if (this.antiAliasedUsed != antiAliasedUsedVal) {
	    this.antiAliasedUsed = antiAliasedUsedVal;
	    repaint();
	    for (final Component comp : getComponents()) {
		if (comp instanceof WSComponent<?>) {
		    final WSComponent<?> child = (WSComponent<?>) comp;
		    if (child.isAntiAliaseUseChangeableByParents()) {
			child.setAntiAliasedUsed(antiAliasedUsedVal);
		    }
		}
	    }
	}
    }

    /**
     * @param antiAliaseUseChangeableByParentsVal
     *            {@link boolean} the antiAliaseUseChangeableByParents to set.
     */
    public void setAntiAliaseUseChangeableByParents(
	    final boolean antiAliaseUseChangeableByParentsVal) {
	this.antiAliaseUseChangeableByParents = antiAliaseUseChangeableByParentsVal;
    }

    /**
     * @param filteredVal
     *            {@link boolean} the filtered to set.
     */
    public final void setFiltered(final boolean filteredVal) {
	if (this.filtered != filteredVal) {
	    final boolean oldVal = this.filtered;
	    this.filtered = filteredVal;
	    firePropertyChange(WSComponent.PROPERTY_NAME_FILTERED, oldVal,
		    this.filtered);
	    repaint();
	}
    }

    /**
     * @param filteredColorVal
     *            {@link Color} the filteredColor to set.
     */
    public final void setFilteredColor(final Color filteredColorVal) {
	this.filteredColor = filteredColorVal;
    }

    /**
     * @param highlightableVal
     *            {@link boolean} the highlightable to set.
     */
    public final void setHighlightable(final boolean highlightableVal) {
	this.highlightable = highlightableVal;
	if (!this.highlightable && this.highlighted) {
	    this.highlighted = false;
	    firePropertyChange(WSComponent.PROPERTY_NAME_HIGHLIGHTED, true,
		    false);
	    repaint();
	}
    }

    /**
     * @param highlightedVal
     *            {@link boolean} the highlighted to set.
     */
    public final void setHighlighted(final boolean highlightedVal) {
	if (isHighlightable()) {
	    if (this.highlighted != highlightedVal) {
		final boolean oldValue = this.highlighted;
		this.highlighted = highlightedVal;
		firePropertyChange(WSComponent.PROPERTY_NAME_HIGHLIGHTED,
			oldValue, this.highlighted);
		repaint();
	    }
	}
    }

    /**
     * @param highlightedColorRef
     *            {@link Color} the highlightedColor to set.
     */
    public final void setHighlightedColor(final Color highlightedColorRef) {
	this.highlightedColor = highlightedColorRef;
    }

    /**
     * @param modelRef
     *            {@link M} the model to set.
     */
    public final void setModel(final M modelRef) {
	if (this.model != null) {
	    unregisterFromModel(this.model);
	}
	this.model = null;
	this.model = modelRef;
	if (this.model != null) {
	    registerToModel(this.model);
	}
	repaint();
    }

    /**
     * @param moveableVal
     *            {@link boolean} the moveable to set.
     */
    public final void setMovable(final boolean moveableVal) {
	this.movable = moveableVal;
    }

    /**
     * @param moveAlongRasterRef
     *            {@link Dimension} the moveAlongRaster to set.
     */
    public final void setMoveAlongRaster(final Dimension moveAlongRasterRef) {
	this.moveAlongRaster = moveAlongRasterRef;
    }

    /**
     * @param resizableVal
     *            <code>boolean</code>
     */
    public final void setResizable(final boolean resizableVal) {
	final boolean oldValue = this.resizable;
	this.resizable = resizableVal;
	firePropertyChange(WSComponent.PROPERTY_NAME_RESIZABLE, oldValue,
		this.resizable);
    }

    /**
     * @param restrictedToParentsVisibleRectVal
     *            {@link boolean} the restrictedToParentsVisibleRect to set.
     */
    public final void setRestrictedToParentsVisibleRect(
	    final boolean restrictedToParentsVisibleRectVal) {
	this.restrictedToParentsVisibleRect = restrictedToParentsVisibleRectVal;
    }

    /**
     * @param selectableVal
     *            {@link boolean} the selectable to set.
     */
    public final void setSelectable(final boolean selectableVal) {
	this.selectable = selectableVal;
	if (!this.selectable && this.selected) {
	    this.selected = false;
	    firePropertyChange(WSComponent.PROPERTY_NAME_SELECTED, true, false);
	}
    }

    /**
     * @param selectedVal
     *            {@link boolean} the selected to set.
     */
    public final void setSelected(final boolean selectedVal) {
	if (isSelectable()) {
	    if (this.selected != selectedVal) {
		final boolean oldValue = this.selected;
		this.selected = selectedVal;
		firePropertyChange(WSComponent.PROPERTY_NAME_SELECTED,
			oldValue, this.selected);
		repaint();
	    }
	}
    }

    /**
     * @param selectedAndHighlightedColorRef
     *            {@link Color} the selectedAndHighlightedColor to set.
     */
    public final void setSelectedAndHighlightedColor(
	    final Color selectedAndHighlightedColorRef) {
	this.selectedAndHighlightedColor = selectedAndHighlightedColorRef;
    }

    /**
     * @param selectedColorRef
     *            {@link Color} the selectedColor to set.
     */
    public final void setSelectedColor(final Color selectedColorRef) {
	this.selectedColor = selectedColorRef;
    }

    /**
     * @param selectionHolderRef
     *            {@link SelectionHolder} the selectionHolder to set.
     */
    public final void setSelectionHolder(
	    final SelectionHolder selectionHolderRef) {
	if (this.selectionHolder != null) {
	    removePropertyChangeListener(this.selectionHolder);
	}
	this.selectionHolder = selectionHolderRef;
	addPropertyChangeListener(this.selectionHolder);
    }

    /**
     * @param strokeRef
     *            {@link Stroke} the stroke to set.
     */
    public final void setStroke(final StrokeImpl strokeRef) {
	this.stroke = strokeRef;
    }

    /**
     * @param coorFixed
     *            {@link boolean} the xCoorFixed to set.
     */
    public final void setXCoorFixed(final boolean coorFixed) {
	this.xCoorFixed = coorFixed;
    }

    /**
     * @param coorFixed
     *            {@link boolean} the yCoorFixed to set.
     */
    public final void setYCoorFixed(final boolean coorFixed) {
	this.yCoorFixed = coorFixed;
    }

    /**
     * @param modelRef
     *            M
     */
    protected void unregisterFromModel(final M modelRef) {
    }
}
//
// $Log: WSComponent.java,v $
// Revision 1.9 2009-12-09 15:41:57 sweiss
// just some added stuff concerning highlighting
//
// Revision 1.8 2009-11-23 13:50:44 sweiss
// *** empty log message ***
//
// Revision 1.7 2009-06-14 15:05:19 sweiss
// try to fix move bug but still not finished
//
// Revision 1.6 2009-05-27 18:46:51 sweiss
// fixed dragging of parent comps
//
// Revision 1.5 2009-05-02 11:25:24 sweiss
// is a component highlightable at all?
//
// Revision 1.4 2009-02-09 16:14:42 sweiss
// bug fixing
//
// Revision 1.3 2009-02-09 13:10:29 sweiss
// fixed antialias stuff and added selection rectangle
//
// Revision 1.2 2009-02-02 17:13:39 sweiss
// fixed components
//
// Revision 1.1 2009-02-02 16:23:40 sweiss
// *** empty log message ***
//
//
