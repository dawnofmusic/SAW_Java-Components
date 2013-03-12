package de.wsdevel.components.timeline;

import de.wsdevel.tools.awt.model.observer.ObserverList;

/**
 * Created on 22.11.2008.
 * 
 * for project: Scenejo__Model
 * 
 * @author <a href="mailto:sweiss@weissundschmidt.de">Sebastian A. Weiss - Weiss
 *         und Schmidt, Mediale Systeme GbR</a>
 * @version $Author: sweiss $ -- $Revision: 1.1 $ -- $Date: 2009-02-02 16:23:39 $
 * 
 * <br>
 * (c) 2007, Weiss und Schmidt, Mediale Systeme GbR - All rights reserved.
 * 
 */
public class TimelineListenerSupport<T> {

	/**
	 * {@link ObserverList<TimelineListener<T>>} COMMENT.
	 */
	private ObserverList<TimelineListener<T>> listeners = new ObserverList<TimelineListener<T>>();

	/**
	 * COMMENT.
	 * 
	 * @param listener
	 *            {@link TimelineListener}
	 */
	public final void addListener(final TimelineListener<T> listener) {
		this.listeners.addListener(listener);
	}

	/**
	 * COMMENT.
	 * 
	 * @param value
	 *            {@link TimelineValueTuple}
	 */
	final void fireValueInserted(final TimelineValueTuple<T> value) {
		this.listeners.observe(new ObserverList.Action<TimelineListener<T>>() {
			public void doit(TimelineListener<T> arg0) {
				arg0.valueInserted(value);
			}
		});
	}

	/**
	 * COMMENT.
	 * 
	 * @param value
	 *            {@link TimelineValueTuple}
	 */
	final void fireElementRemoved(final TimelineValueTuple<T> value) {
		this.listeners.observe(new ObserverList.Action<TimelineListener<T>>() {
			public void doit(TimelineListener<T> arg0) {
				arg0.valueRemoved(value);
			}
		});
	}

	/**
	 * COMMENT.
	 * 
	 * @param listener
	 *            {@link TimelineListener}
	 */
	public final void removeListener(final TimelineListener<T> listener) {
		this.listeners.removeListener(listener);
	};

}
//
// $Log: TimelineListenerSupport.java,v $
// Revision 1.1  2009-02-02 16:23:39  sweiss
// *** empty log message ***
//
//
