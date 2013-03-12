package de.wsdevel.components.timeline;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.wsdevel.components.StrokeImpl;
import de.wsdevel.components.WSHandle;
import de.wsdevel.components.WSRoot;
import de.wsdevel.components.selection.SelectionHolder;
import de.wsdevel.components.timeline.Timeline.ValueCat;

/**
 * Created on 22.11.2008.
 * 
 * for project: Scenejo__Model
 * 
 * @author <a href="mailto:sweiss@weissundschmidt.de">Sebastian A. Weiss - Weiss
 *         und Schmidt, Mediale Systeme GbR</a>
 * @version $Author: sweiss $ -- $Revision: 1.3 $ -- $Date: 2009-02-02 16:23:39
 *          $
 * 
 * <br>
 *          (c) 2007, Weiss und Schmidt, Mediale Systeme GbR - All rights
 *          reserved.
 * 
 */
public class JTimeline<T> extends WSRoot {

    private static final Dimension DEFAULT_HANDLE_DIMENSION = new Dimension(6,
	    6);

    /**
     * {@link long} COMMENT.
     */
    private static final long serialVersionUID = 1L;

    /**
     * <code>boolean</code> COMMENT.
     */
    private boolean canAddHandles = true;

    /**
     * {@link ComponentAdapter} COMMENT.
     */
    private transient ComponentAdapter handleMoveListener;

    /**
     * {@link HashMap<TimelineValueTuple<T>,WSHandle<TimelineValueTuple<T>>>}
     * COMMENT.
     */
    private HashMap<TimelineValueTuple<T>, WSHandle<TimelineValueTuple<T>>> handlesForTVT = new HashMap<TimelineValueTuple<T>, WSHandle<TimelineValueTuple<T>>>();

    /**
     * <code>int</code> COMMENT.
     */
    private int maximumMillisPerPixel = 2000;

    /**
     * <code>int</code> COMMENT.
     */
    private int millisPerPixel = 10;

    /**
     * <code>int</code> COMMENT.
     */
    private int minimumMillisPerPixel = 2;

    /**
     * {@link Timeline<T>} COMMENT.
     */
    private Timeline<T> model = null;

    /**
     * {@link TimelineListener<T>} COMMENT.
     */
    private transient TimelineListener<T> timelineListener;

    /**
     * <code>boolean</code> COMMENT.
     */
    private boolean xCoorFixed = false;

    /**
     * <code>boolean</code> COMMENT.
     */
    private boolean yCoorFixed = false;

    /**
     * {@link SelectionHolder} COMMENT.
     */
    private transient SelectionHolder selectionHolder = null;

    /**
     * COMMENT.
     * 
     * @param modelRef
     *            {@link Timeline}
     */
    public JTimeline(final Timeline<T> modelRef) {
	setSize(300, 100);
	setModel(modelRef);
	setBackground(Color.WHITE);
	setForeground(Color.DARK_GRAY);
	setBorder(BorderFactory.createEtchedBorder());
	this.selectionHolder = new SelectionHolder();

	this.handleMoveListener = new ComponentAdapter() {
	    @SuppressWarnings( { "unchecked", "synthetic-access" })
	    @Override
	    public void componentMoved(ComponentEvent e) {
		final Point2D.Double location = ((WSHandle<T>) e.getComponent())
			.getCenter();
		final TimelineValueTuple<T> userObject = ((WSHandle<TimelineValueTuple<T>>) e
			.getComponent()).getModel();
		userObject.setTimestamp(getTimestampForXCoor((int) Math
			.round(location.x)));
		userObject.setValue(getValueForYCoor((int) Math
			.round(location.y)));
		repaint();
	    }
	};
	addComponentListener(new ComponentAdapter() {
	    @SuppressWarnings("synthetic-access")
	    @Override
	    public void componentResized(ComponentEvent e) {
		updateHandlesForScaleChange();
	    }
	});
	final JPopupMenu popupMenu = createJPopUpMenu();
	addMouseListener(new MouseAdapter() {
	    @Override
	    public void mousePressed(MouseEvent e) {
		showPopup(e);
	    }

	    @Override
	    public void mouseReleased(MouseEvent e) {
		showPopup(e);
	    }

	    /**
	     * COMMENT.
	     * 
	     * @param popupMenu
	     * @param e
	     */
	    private void showPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
		    if (isCanAddHandles()) {
			popupMenu.show(JTimeline.this, e.getPoint().x, e
				.getPoint().y);
		    }
		}
	    }
	});
    }

    /**
     * COMMENT.
     * 
     * @param next
     *            {@link TimelineValueTuple}
     */
    private void addHandleForTVT(final TimelineValueTuple<T> next) {
	final WSHandle<TimelineValueTuple<T>> handle = new WSHandle<TimelineValueTuple<T>>(
		next, this.selectionHolder);
	handle.setSize(DEFAULT_HANDLE_DIMENSION);
	handle.setBackground(Color.LIGHT_GRAY);
	handle.setOval(true);
	add(handle);
	handle.setCenter(getXCoorForTimestamp(next.getTimestamp()),
		getYCoorForValue(next.getValue()));
	handle.addComponentListener(this.handleMoveListener);
	// handle.addMouseListener(new MouseAdapter() {
	// @SuppressWarnings("unchecked")
	// @Override
	// public void mouseReleased(MouseEvent e) {
	// // to fit handle into possible raster! (sw)
	// updateHandleLocation((WSHandle<TimelineValueTuple<T>>) e
	// .getComponent());
	// // repaint to take care of drawn lines! (sw)
	// repaint();
	// }
	// });
	WSHandle<TimelineValueTuple<T>> put = JTimeline.this.handlesForTVT.put(
		next, handle);
	// remove potential old handle
	removeHandle(put);
    }

    /**
     * COMMENT.
     * 
     * @return {@link JPopupMenu}
     */
    @SuppressWarnings("serial")
    private JPopupMenu createJPopUpMenu() {
	final JPopupMenu popupMenu = new JPopupMenu();
	popupMenu.add(new JMenuItem(new AbstractAction("Add") {
	    @SuppressWarnings("synthetic-access")
	    public void actionPerformed(ActionEvent e) {
		Point location = popupMenu.getInvoker().getMousePosition();
		getModel().addToTrack(
			new TimelineValueTuple<T>(
				getTimestampForXCoor(location.x),
				getValueForYCoor(location.y)));
	    }
	}));
	return popupMenu;
    }

    /**
     * COMMENT.
     * 
     * @return <code>double</code>
     */
    private double getDSec() {
	double d = (50.0d * this.millisPerPixel) / 1000.0d;
	if (d <= 1) {
	    d = Math.ceil(d * 10) / 10;
	} else if (d <= 10) {
	    d = Math.ceil(d * 4) / 4;
	} else if (d > 10) {
	    d = Math.ceil(d);
	}
	return d;
    }

    /**
     * @return {@link int} the maximumMillisPerPixel.
     */
    public final int getMaximumMillisPerPixel() {
	return this.maximumMillisPerPixel;
    }

    /**
     * @return <code>int</code> the millisPerPixel.
     */
    public final int getMillisPerPixel() {
	return this.millisPerPixel;
    }

    /**
     * @return {@link int} the minimumMillisPerPixel.
     */
    public final int getMinimumMillisPerPixel() {
	return this.minimumMillisPerPixel;
    }

    /**
     * @return {@link Timeline<T>} the model.
     */
    public final Timeline<T> getModel() {
	return this.model;
    }

    /**
     * COMMENT.
     * 
     * @param d
     * @return
     */
    private int getPixelPerDSec(double d) {
	return (int) Math.round((d * 1000 / this.millisPerPixel));
    }

    /**
     * COMMENT.
     * 
     * @param x
     *            <code>int</code>
     * @return {@link Long}
     */
    private final Long getTimestampForXCoor(final int x) {
	return (long) x * getMillisPerPixel();
    }

    /**
     * COMMENT.
     * 
     * @param y
     *            <code>int</code>
     * @return <code>T</code>
     */
    private final T getValueForYCoor(final int y) {
	final double d = 1 - y
		/ (double) (getHeight() - DEFAULT_HANDLE_DIMENSION.height);
	return getModel().getValueForDouble(d);
    }

    /**
     * COMMENT.
     * 
     * @param timeStamp
     *            <code>long</code>
     * @return <code>int</code>
     */
    private final int getXCoorForTimestamp(final long timeStamp) {
	return Math.round(timeStamp / (float) getMillisPerPixel());
    }

    /**
     * COMMENT.
     * 
     * @param value
     *            <code>T</code>
     * @return <code>int</code>
     */
    private final int getYCoorForValue(final T value) {
	double val = getModel().getDoubleForValue(value);
	return (int) Math.round((getHeight() - DEFAULT_HANDLE_DIMENSION.height)
		* (1 - val));
    }

    /**
     * @return {@link boolean} the canAddHandles.
     */
    public final boolean isCanAddHandles() {
	return this.canAddHandles;
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
     * {@inheritDoc}
     * 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
	NumberFormat numberInstance = NumberFormat.getNumberInstance();
	numberInstance.setMaximumFractionDigits(2);

	Graphics2D g2d = (Graphics2D) g;
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);

	g.setColor(getBackground());
	g.fillRect(0, 0, getWidth(), getHeight());
	g2d.setStroke(StrokeImpl.FINEST);

	g.setColor(Color.LIGHT_GRAY);

	double sec = 0;
	double dSec = getDSec();
	int pixelPerDSec = getPixelPerDSec(dSec);
	for (int x = 0; x < getWidth(); x += pixelPerDSec, sec += dSec) {
	    g.drawLine(x, 0, x, getHeight());
	    g.drawString(numberInstance.format(sec) + "s", x + 1,
		    getHeight() - 1);
	}

	Iterator<ValueCat<T>> catsIt = getModel().getCategories().iterator();
	while (catsIt.hasNext()) {
	    ValueCat<T> next = catsIt.next();
	    int y = getYCoorForValue(next.getValue());
	    g.drawLine(0, y, getWidth(), y);
	    g.drawString(next.getName(), 1, g.getFontMetrics().getAscent() + y
		    + 1);
	}

	g.setColor(getForeground());
	final TreeSet<WSHandle<TimelineValueTuple<T>>> treeSet = new TreeSet<WSHandle<TimelineValueTuple<T>>>(
		new Comparator<WSHandle<TimelineValueTuple<T>>>() {
		    public int compare(WSHandle<TimelineValueTuple<T>> o1,
			    WSHandle<TimelineValueTuple<T>> o2) {
			return o1.getModel().getTimestamp().compareTo(
				o2.getModel().getTimestamp());
		    }
		});
	treeSet.addAll(this.handlesForTVT.values());
	WSHandle<TimelineValueTuple<T>> lastHandle = null;
	final Iterator<WSHandle<TimelineValueTuple<T>>> iterator = treeSet
		.iterator();
	while (iterator.hasNext()) {
	    final WSHandle<TimelineValueTuple<T>> handle = iterator.next();
	    if (lastHandle != null) {
		Point2D.Double p1 = lastHandle.getCenter();
		Point2D.Double p2 = handle.getCenter();
		g.drawLine((int) Math.round(p1.x), (int) Math.round(p1.y),
			(int) Math.round(p2.x), (int) Math.round(p2.y));
	    }
	    lastHandle = handle;
	}
    }

    /**
     * COMMENT.
     * 
     * @param remove2
     *            {@link WSHandle}
     */
    private void removeHandle(final WSHandle<TimelineValueTuple<T>> remove2) {
	if (remove2 != null) {
	    remove2.removeComponentListener(JTimeline.this.handleMoveListener);
	    remove(remove2);
	}
    }

    /**
     * @param canAddHandlesVal
     *            {@link boolean} the canAddHandles to set.
     */
    public final void setCanAddHandles(final boolean canAddHandlesVal) {
	this.canAddHandles = canAddHandlesVal;
    }

    /**
     * @param maximumMillisPerPixelVal
     *            {@link int} the maximumMillisPerPixel to set.
     */
    public final void setMaximumMillisPerPixel(
	    final int maximumMillisPerPixelVal) {
	this.maximumMillisPerPixel = maximumMillisPerPixelVal;
	if (this.millisPerPixel > this.maximumMillisPerPixel) {
	    this.millisPerPixel = this.maximumMillisPerPixel;
	    updateHandlesForScaleChange();
	}
    }

    /**
     * @param millisPerPixelVal
     *            {@link double} the millisPerPixel to set.
     */
    public final void setMillisPerPixel(final int millisPerPixelVal) {
	this.millisPerPixel = millisPerPixelVal;
	if (this.millisPerPixel > this.maximumMillisPerPixel) {
	    this.millisPerPixel = this.maximumMillisPerPixel;
	} else if (this.millisPerPixel < this.minimumMillisPerPixel) {
	    this.millisPerPixel = this.minimumMillisPerPixel;
	}
	updateHandlesForScaleChange();
    }

    /**
     * @param minimumMillisPerPixelVal
     *            {@link int} the minimumMillisPerPixel to set.
     */
    public final void setMinimumMillisPerPixel(
	    final int minimumMillisPerPixelVal) {
	this.minimumMillisPerPixel = minimumMillisPerPixelVal;
	if (this.millisPerPixel < this.minimumMillisPerPixel) {
	    this.millisPerPixel = this.minimumMillisPerPixel;
	    updateHandlesForScaleChange();
	}
    }

    /**
     * @param modelRef
     *            {@link Timeline<T>} the model to set.
     */
    public final void setModel(final Timeline<T> modelRef) {
	if (this.model != null) {
	    this.model.removeListener(this.timelineListener);
	    removeAll();
	    this.handlesForTVT.clear();
	}
	this.model = modelRef;
	if (this.model != null) {
	    final SortedSet<TimelineValueTuple<T>> sortedTimeline = this.model
		    .getSortedTimeline();

	    final Iterator<TimelineValueTuple<T>> stIt = sortedTimeline
		    .iterator();
	    while (stIt.hasNext()) {
		addHandleForTVT(stIt.next());
	    }

	    if (this.timelineListener == null) {
		this.timelineListener = new TimelineListener<T>() {
		    @SuppressWarnings("synthetic-access")
		    public void valueInserted(TimelineValueTuple<T> value) {
			addHandleForTVT(value);
			repaint();
		    }

		    @SuppressWarnings("synthetic-access")
		    public void valueRemoved(TimelineValueTuple<T> value) {
			final WSHandle<TimelineValueTuple<T>> remove2 = JTimeline.this.handlesForTVT
				.remove(value);
			removeHandle(remove2);
			repaint();
		    }

		};
	    }
	    this.model.addListener(this.timelineListener);
	}
    }

    /**
     * @param coorFixed
     *            {@link boolean} the xCoorFixed to set.
     */
    public final void setXCoorFixed(boolean coorFixed) {
	if (this.xCoorFixed != coorFixed) {
	    this.xCoorFixed = coorFixed;
	    final Iterator<WSHandle<TimelineValueTuple<T>>> handlesIt = this.handlesForTVT
		    .values().iterator();
	    while (handlesIt.hasNext()) {
		handlesIt.next().setXCoorFixed(coorFixed);
	    }
	}
    }

    /**
     * @param coorFixed
     *            {@link boolean} the yCoorFixed to set.
     */
    public final void setYCoorFixed(boolean coorFixed) {
	if (this.yCoorFixed != coorFixed) {
	    this.yCoorFixed = coorFixed;
	    final Iterator<WSHandle<TimelineValueTuple<T>>> handlesIt = this.handlesForTVT
		    .values().iterator();
	    while (handlesIt.hasNext()) {
		handlesIt.next().setYCoorFixed(coorFixed);
	    }
	}
    }

    /**
     * COMMENT.
     * 
     * @param handle
     *            {@link WSHandle}
     */
    private void updateHandleLocation(
	    final WSHandle<TimelineValueTuple<T>> handle) {
	handle.removeComponentListener(this.handleMoveListener);
	handle.setCenter(
		getXCoorForTimestamp(handle.getModel().getTimestamp()),
		getYCoorForValue(handle.getModel().getValue()));
	handle.addComponentListener(this.handleMoveListener);
    }

    /**
     * COMMENT.
     */
    private void updateHandlesForScaleChange() {
	final Iterator<WSHandle<TimelineValueTuple<T>>> handlesIt = this.handlesForTVT
		.values().iterator();
	while (handlesIt.hasNext()) {
	    updateHandleLocation(handlesIt.next());
	}
	repaint();
    }

}
//
// $Log: JTimeline.java,v $
// Revision 1.3 2009-02-09 16:14:42 sweiss
// bug fixing
//
// Revision 1.2 2009-02-09 13:10:30 sweiss
// fixed antialias stuff and added selection rectangle
//
// Revision 1.1 2009-02-02 16:23:39 sweiss
// *** empty log message ***
//
//
