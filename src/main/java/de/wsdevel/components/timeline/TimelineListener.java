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
 * (c) 2007, Weiss und Schmidt, Mediale Systeme GbR - All rights reserved.
 * 
 */
public interface TimelineListener<T> {

	/**
	 * COMMENT.
	 * 
	 * @param value
	 *            {@link TimelineValueTuple}
	 */
	void valueInserted(TimelineValueTuple<T> value);

	/**
	 * COMMENT.
	 * 
	 * @param value
	 *            {@link TimelineValueTuple}
	 */
	void valueRemoved(TimelineValueTuple<T> value);

}
//
// $Log: TimelineListener.java,v $
// Revision 1.1  2009-02-02 16:23:39  sweiss
// *** empty log message ***
//
//
