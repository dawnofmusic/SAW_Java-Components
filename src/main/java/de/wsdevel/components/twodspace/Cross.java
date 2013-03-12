package de.wsdevel.components.twodspace;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created on 29.04.2009 for project Java__Components. <br/>
 * (c) 2009, wei&szlig;&amp;schmidt - All rights reserved.
 * 
 * @author <a href="mailto:sweiss@weissundschmidt.de">Sebastian A. Wei&szlig; -
 *         wei&szlig;&amp;schmidt</a>
 * @version $Author: sweiss $ -- $Revision: 1.1 $ -- $Date: 2009-05-02 11:26:34 $
 */
public class Cross {

    /**
     * {@link Log} the logger.
     */
    private static final Log LOG = LogFactory.getLog(Cross.class);

    /**
     * <code>double</code> maximum absolute value.
     */
    private static final double MAX = 1.0;

    /**
     * {@link PropertyChangeSupport} delegate used for property change events.
     */
    private PropertyChangeSupport pcs;

    /**
     * <code>double</code> x value.
     */
    private double x = 0;

    /**
     * <code>double</code> y value.
     */
    private double y = 0;

    /**
     * Default constructor.
     */
    public Cross() {
	this(0, 0);
    }

    /**
     * @param xVal
     *            <code>double</code>
     * @param yVal
     *            <code>double</code>
     */
    public Cross(final double xVal, final double yVal) {
	this.pcs = new PropertyChangeSupport(this);
	setX(xVal);
	setY(yVal);
    }

    /**
     * @param listener
     *            {@link PropertyChangeListener}
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public final void addPropertyChangeListener(
	    final PropertyChangeListener listener) {
	this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * @param propertyName
     *            {@link String}
     * @param listener
     *            {@link PropertyChangeListener}
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public final void addPropertyChangeListener(final String propertyName,
	    final PropertyChangeListener listener) {
	this.pcs.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * @return {@link double} the x.
     */
    public final double getX() {
	return this.x;
    }

    /**
     * @return {@link double} the y.
     */
    public final double getY() {
	return this.y;
    }

    /**
     * @param listener
     *            {@link PropertyChangeListener}
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public final void removePropertyChangeListener(
	    final PropertyChangeListener listener) {
	this.pcs.removePropertyChangeListener(listener);
    }

    /**
     * @param propertyName
     *            {@link String}
     * @param listener
     *            {@link PropertyChangeListener}
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    public final void removePropertyChangeListener(final String propertyName,
	    final PropertyChangeListener listener) {
	this.pcs.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * @param xVal
     *            {@link double} the x to set.
     */
    public final void setX(final double xVal) {
	double oldVal = this.x;
	if (xVal > MAX) {
	    this.x = MAX;
	} else if (xVal < -MAX) {
	    this.x = -MAX;
	}
	this.x = xVal;
	this.pcs.firePropertyChange("x", oldVal, this.x);
	if (LOG.isDebugEnabled()) {
	    LOG.debug("new x: " + this.x);
	}
    }

    /**
     * @param yVal
     *            {@link double} the y to set.
     */
    public final void setY(final double yVal) {
	double oldVal = this.y;
	if (yVal > MAX) {
	    this.y = MAX;
	} else if (yVal < -MAX) {
	    this.y = -MAX;
	}
	this.y = yVal;
	this.pcs.firePropertyChange("y", oldVal, this.y);
	if (LOG.isDebugEnabled()) {
	    LOG.debug("new y: " + this.y);
	}
    }

}
//
// $Log: Cross.java,v $
// Revision 1.1  2009-05-02 11:26:34  sweiss
// added JTwoDSpace
//
//
