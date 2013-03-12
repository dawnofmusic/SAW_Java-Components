package de.wsdevel.components;

import de.wsdevel.tools.awt.model.observer.ObserverList;
import de.wsdevel.tools.awt.model.observer.SwingThreadObserverList;

/**
 * Created on 25.07.2010 for project: SAW_Components__HEAD. (c) 2010, Sebastian
 * A. Weiss - All rights reserved.
 * 
 * @author <a href="mailto:sebastian@scenejo.org">Sebastian A. Weiss -
 *         scenejo.org</a>
 */
public class WSComponentSupport {

    /**
     * {@link SwingThreadObserverList<WSComponentListener>} COMMENT.
     */
    private final SwingThreadObserverList<WSComponentListener> listeners = new SwingThreadObserverList<WSComponentListener>();

    /**
     * COMMENT.
     * 
     * @param listener
     *            {@link WSComponentListener}
     */
    public final void addWSComponentListener(final WSComponentListener listener) {
	this.listeners.addListener(listener);
    }

    /**
     * COMMENT.
     * 
     * @param evtRef
     *            {@link WSComponentEvent}
     */
    public final void fireWSComponentMoved(final WSComponentEvent evtRef) {
	this.listeners.observe(new ObserverList.Action<WSComponentListener>() {
	    public void doit(final WSComponentListener arg0) {
		arg0.componentMoved(evtRef);
	    }
	});
    }

    /**
     * COMMENT.
     * 
     * @param evtRef
     *            {@link WSComponentEvent}
     */
    public final void fireWSComponentResized(final WSComponentEvent evtRef) {
	this.listeners.observe(new ObserverList.Action<WSComponentListener>() {
	    public void doit(final WSComponentListener arg0) {
		arg0.componentResized(evtRef);
	    }
	});
    }

    /**
     * COMMENT.
     * 
     * @param listener
     *            {@link WSComponentListener}
     */
    public final void removeWSComponentListener(
	    final WSComponentListener listener) {
	this.listeners.removeListener(listener);
    }

}
//
// $Log: $
//
