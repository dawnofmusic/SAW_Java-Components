package de.wsdevel.components.timeline;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created on 22.11.2008.
 * 
 * for project: Scenejo__Model
 * 
 * @author <a href="mailto:sweiss@weissundschmidt.de">Sebastian A. Weiss - Weiss
 *         und Schmidt, Mediale Systeme GbR</a>
 * @version $Author: sweiss $ -- $Revision: 1.1 $ -- $Date: 2009-02-02 16:23:40 $
 * 
 * <br>
 *          (c) 2007, Weiss und Schmidt, Mediale Systeme GbR - All rights
 *          reserved.
 * 
 */
public class Timeline<T> {

	/**
	 * Created on 22.11.2008.
	 * 
	 * for project: Scenejo__Model
	 * 
	 * @author <a href="mailto:sweiss@weissundschmidt.de">Sebastian A. Weiss -
	 *         Weiss und Schmidt, Mediale Systeme GbR</a>
	 * @version $Author: sweiss $ -- $Revision: 1.1 $ -- $Date: 2009-02-02 16:23:40 $
	 * 
	 * <br>
	 *          (c) 2007, Weiss und Schmidt, Mediale Systeme GbR - All rights
	 *          reserved.
	 * 
	 */
	public static interface ValueAdapter<T> {

		List<ValueCat<T>> getCategories();

		/**
		 * COMMENT.
		 * 
		 * @param value
		 * @return
		 */
		double getDoubleForValue(T value);

		/**
		 * COMMENT.
		 * 
		 * @param d
		 * @return
		 */
		T getValueForDouble(double d);

		/**
		 * COMMENT.
		 * 
		 * @return <code>T</code>
		 */
		T getStartValue();
	}

	/**
	 * ValueCat created on 23.11.2008. for project: Scenejo__Model
	 * 
	 * @author <a href="mailto:sweiss@scenejo.org">Sebastian A. Weiss -
	 *         scenejo.org</a>
	 * @version $Author: sweiss $ -- $Revision: 1.1 $ -- $Date: 2009-02-02 16:23:40 $
	 * 
	 * <br>
	 *          (c) 2008, scenejo.org - All rights reserved. Scenejo - An
	 *          Interactive Storytelling Framework
	 */
	public static class ValueCat<T> {

		/**
		 * {@link String} COMMENT.
		 */
		private String name;

		/**
		 * {@link T} COMMENT.
		 */
		private T value;

		public ValueCat() {
		}

		public ValueCat(final String nameVal, final T valueRef) {
			setName(nameVal);
			setValue(valueRef);
		}

		/**
		 * @return {@link String} the name.
		 */
		public final String getName() {
			return this.name;
		}

		/**
		 * @return {@link T} the value.
		 */
		public final T getValue() {
			return this.value;
		}

		/**
		 * @param nameVal
		 *            {@link String} the name to set.
		 */
		public final void setName(final String nameVal) {
			this.name = nameVal;
		}

		/**
		 * @param valueRef
		 *            {@link T} the value to set.
		 */
		public final void setValue(final T valueRef) {
			this.value = valueRef;
		}
	}

	/**
	 * {@link ValueAdapter<T>} COMMENT.
	 */
	private ValueAdapter<T> adapter = null;

	/**
	 * {@link TimelineListenerSupport<T>} COMMENT.
	 */
	private TimelineListenerSupport<T> tls = new TimelineListenerSupport<T>();

	/**
	 * {@link SortedSet<TimelineValueTuple<T>>} COMMENT.
	 */
	private SortedSet<TimelineValueTuple<T>> values = new TreeSet<TimelineValueTuple<T>>(
			new Comparator<TimelineValueTuple<T>>() {
				public int compare(TimelineValueTuple<T> o1,
						TimelineValueTuple<T> o2) {
					return o1.getTimestamp().compareTo(o2.getTimestamp());
				}
			});

	/**
	 * COMMENT.
	 * 
	 * @param adapterRef
	 *            {@link ValueAdapter}
	 */
	public Timeline(final ValueAdapter<T> adapterRef) {
		setAdapter(adapterRef);
		addToTrack(new TimelineValueTuple<T>(0l, getAdapter().getStartValue()));
	}

	/**
	 * @param listener
	 *            {@link TimelineListener}
	 * @see de.wsdevel.components.timeline.TimelineListenerSupport#addListener(de.wsdevel.components.timeline.TimelineListener)
	 */
	public final void addListener(final TimelineListener<T> listener) {
		this.tls.addListener(listener);
	}

	/**
	 * COMMENT.
	 * 
	 * @param timestamp
	 *            <code>long</code>
	 * @param value
	 *            <code>T</code>
	 */
	public final void addToTrack(final TimelineValueTuple<T> value) {
		this.values.add(value);
		this.tls.fireValueInserted(value);
	}

	/**
	 * COMMENT.
	 */
	public final void clear() {
		synchronized (this.values) {
			final Iterator<TimelineValueTuple<T>> valIt = this.values
					.iterator();
			while (valIt.hasNext()) {
				this.tls.fireElementRemoved(valIt.next());
			}
			this.values.clear();
		}
	}

	/**
	 * @return {@link ValueAdapter<T>} the adapter.
	 */
	public final ValueAdapter<T> getAdapter() {
		return this.adapter;
	}

	/**
	 * @return {@link List}
	 * @see de.wsdevel.components.timeline.Timeline.ValueAdapter#getCategories()
	 */
	public List<ValueCat<T>> getCategories() {
		return this.adapter.getCategories();
	}

	/**
	 * COMMENT.
	 * 
	 * @param value
	 *            <code>T</code>
	 * @return <code>double</code>
	 */
	public double getDoubleForValue(final T value) {
		return this.adapter.getDoubleForValue(value);
	}

	/**
	 * COMMENT.
	 * 
	 * @return {@link SortedMap}
	 */
	public final SortedSet<TimelineValueTuple<T>> getSortedTimeline() {
		return this.values;
	}

	/**
	 * COMMENT.
	 * 
	 * @param d
	 *            <code>double</code>
	 * @return <code>T</code>
	 */
	public T getValueForDouble(double d) {
		return this.adapter.getValueForDouble(d);
	}

	/**
	 * COMMENT.
	 * 
	 * @param timestamp
	 *            <code>long</code>
	 * @param value
	 *            <code>T</code>
	 */
	public final void removeFromTrack(final TimelineValueTuple<T> value) {
		this.values.remove(value);
		this.tls.fireElementRemoved(value);
	}

	/**
	 * @param listener
	 *            {@link TimelineListener}
	 * @see de.wsdevel.components.timeline.TimelineListenerSupport#removeListener(de.wsdevel.components.timeline.TimelineListener)
	 */
	public final void removeListener(final TimelineListener<T> listener) {
		this.tls.removeListener(listener);
	}

	/**
	 * @param adapterRef
	 *            {@link ValueAdapter<T>} the adapter to set.
	 */
	public final void setAdapter(ValueAdapter<T> adapterRef) {
		this.adapter = adapterRef;
	}

}
//
// $Log: Timeline.java,v $
// Revision 1.1  2009-02-02 16:23:40  sweiss
// *** empty log message ***
//
//
