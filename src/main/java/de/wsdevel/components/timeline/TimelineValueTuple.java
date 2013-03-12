package de.wsdevel.components.timeline;

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
 *          (c) 2007, Weiss und Schmidt, Mediale Systeme GbR - All rights
 *          reserved.
 * 
 * @param <T>
 */
public class TimelineValueTuple<T> {

	/**
	 * {@link Long} COMMENT.
	 */
	private Long timestamp;

	/**
	 * {@link T} COMMENT.
	 */
	private T value;

	/**
	 * Default constructor.
	 */
	public TimelineValueTuple() {
	}

	/**
	 * COMMENT.
	 * 
	 * @param timestampVal
	 *            {@link Long}
	 * @param valueRef
	 *            <code>T</code>
	 */
	public TimelineValueTuple(final Long timestampVal, final T valueRef) {
		setTimestamp(timestampVal);
		setValue(valueRef);
	}

	/**
	 * @return {@link Long} the timestamp.
	 */
	public final Long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @return {@link T} the value.
	 */
	public final T getValue() {
		return this.value;
	}

	/**
	 * @param timestampVal
	 *            {@link Long} the timestamp to set.
	 */
	public final void setTimestamp(final Long timestampVal) {
		this.timestamp = timestampVal;
	}

	/**
	 * @param valueRef
	 *            {@link T} the value to set.
	 */
	public final void setValue(final T valueRef) {
		this.value = valueRef;
	}

}
//
// $Log: TimelineValueTuple.java,v $
// Revision 1.1  2009-02-02 16:23:39  sweiss
// *** empty log message ***
//
//
