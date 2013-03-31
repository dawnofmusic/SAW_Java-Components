package de.wsdevel.components.plotter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created on 27.03.2009.
 * 
 * (c) 2008, Sebastian A. Weiss - All rights reserved.
 * 
 * @author <a href="mailto:post@sebastian-weiss.de">Sebastian A. Weiss</a>
 * @version $Author$ -- $Revision$ -- $Date$
 */
public class Graph {

	/**
	 * {@link Log} the logger.
	 */
	private static final Log LOG = LogFactory.getLog(Graph.class);

	/**
	 * {@link SortedSet<Double>} COMMENT.
	 */
	private SortedSet<Double> as = new TreeSet<Double>();

	/**
	 * {@link SortedSet<Double>} COMMENT.
	 */
	private SortedSet<Double> bs = new TreeSet<Double>();

	/**
	 * {@link Color} COMMENT.
	 */
	private Color color = Color.BLACK;

	/**
	 * {@link GraphListenerSupport} COMMENT.
	 */
	private GraphListenerSupport gls = new GraphListenerSupport();

	/**
	 * {@link float} COMMENT.
	 */
	private float strokeWidth = 1.0f;

	/**
	 * {@link ArrayList<ValueTuple>} COMMENT.
	 */
	private ArrayList<ValueTuple> tuples = new ArrayList<ValueTuple>();

	/**
	 * @param listener
	 *            {@link GraphListener}
	 * @see de.wsdevel.components.plotter.GraphListenerSupport#addListener(de.wsdevel.components.plotter.GraphListener)
	 */
	public final void addListener(final GraphListener listener) {
		this.gls.addListener(listener);
	}

	/**
	 * @param tuple
	 *            {@link ValueTuple}
	 */
	@SuppressWarnings("nls")
	public final void addTuple(final ValueTuple tuple) {
		this.tuples.add(tuple);
		this.as.add(tuple.getA());
		this.bs.add(tuple.getB());
		if (LOG.isDebugEnabled()) {
			LOG.debug("tuple added: " + tuple);
		}
		this.gls.fireGraphChanged();
	}

	/**
	 * @return {@link Color} the color.
	 */
	public final Color getColor() {
		return this.color;
	}

	/**
	 * COMMENT.
	 * 
	 * @return <code>double</code>
	 */
	public final double getDeltaA() {
		return getMaxA() - getMinA();
	}

	/**
	 * COMMENT.
	 * 
	 * @return <code>double</code>
	 */
	public final double getDeltaB() {
		return getMaxB() - getMinB();
	}

	/**
	 * @return <code>double</code>
	 */
	public final double getMaxA() {
		if (this.as.size() > 0) {
			final Double last = this.as.last();
			if (last == null) {
				return 0;
			}
			return last;
		}
		return 0;
	}

	/**
	 * @return <code>double</code>
	 */
	public final double getMaxB() {
		if (this.bs.size() > 0) {
			final Double last = this.bs.last();
			if (last == null) {
				return 0;
			}
			return last;
		}
		return 0;
	}

	/**
	 * @return <code>double</code>
	 */
	public final double getMinA() {
		if (this.as.size() > 0) {
			final Double first = this.as.first();
			if (first == null) {
				return 0;
			}
			return first;
		}
		return 0;
	}

	/**
	 * @return <code>double</code>
	 */
	public final double getMinB() {
		if (this.bs.size() > 0) {
			final Double first = this.bs.first();
			if (first == null) {
				return 0;
			}
			return first;
		}
		return 0;
	}

	/**
	 * @return {@link float} the strokeWidth.
	 */
	public final float getStrokeWidth() {
		return this.strokeWidth;
	}

	/**
	 * @return {@link ArrayList<ValueTuple>} the tuples.
	 */
	public final ArrayList<ValueTuple> getTuples() {
		return this.tuples;
	}

	/**
	 * @param listener
	 *            {@link GraphListener}
	 * @see de.wsdevel.components.plotter.GraphListenerSupport#removeListener(de.wsdevel.components.plotter.GraphListener)
	 */
	public final void removeListener(final GraphListener listener) {
		this.gls.removeListener(listener);
	}

	/**
	 * @param colorRef
	 *            {@link Color} the color to set.
	 */
	public final void setColor(final Color colorRef) {
		this.color = colorRef;
	}

	/**
	 * @param strokeWidthVal
	 *            {@link float} the strokeWidth to set.
	 */
	public final void setStrokeWidth(final float strokeWidthVal) {
		this.strokeWidth = strokeWidthVal;
	}

	/**
	 * @param tuplesRef
	 *            {@link ArrayList<ValueTuple>} the tuples to set.
	 */
	public final void setTuples(final ArrayList<ValueTuple> tuplesRef) {
		this.tuples = tuplesRef;
	}

}
//
// $Log$
// Revision 1.3 2009-04-09 09:57:31 sweiss
// real time logging of probes
//
// Revision 1.2 2009-04-01 11:57:25 sweiss
// added new uistuff
//
// Revision 1.1 2009-03-28 16:09:24 sweiss
// added a lot of graph stuff
//
//
