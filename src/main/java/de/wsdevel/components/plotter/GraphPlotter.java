/**
 * 
 */
package de.wsdevel.components.plotter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.text.DateFormat;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.wsdevel.tools.math.Graph;
import de.wsdevel.tools.math.ValueTuple;

/**
 * @author Sebastian A. Weiss
 * 
 */
public class GraphPlotter {

    /**
     * {@link Font} COMMENT.
     */
    private static final Font BACKGROUND_FONT = new Font("sansserif", //$NON-NLS-1$
	    Font.PLAIN, 18);

    /**
     * {@link Font} COMMENT.
     */
    @SuppressWarnings("nls")
    public static final Font DEFAULT_FONT = new Font("sansserif", Font.PLAIN,
	    10);

    /**
     * {@link Formatter} COMMENT.
     */
    public static final Formatter DEFAULT_FORMATTER = new Formatter() {
	@Override
	public String format(final double d) {
	    return Float.toString(Math.round(d * 1000) / 1000f);
	}
    };

    /**
     * {@link DateFormat} COMMENT.
     */
    public static final DateFormat DEFAULT_TIME_INSTANCE = DateFormat
	    .getTimeInstance(DateFormat.MEDIUM);

    /**
     * {@link BasicStroke} COMMENT.
     */
    public static final BasicStroke DOTTED_STROKE = new BasicStroke(0.5f,
	    BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[] {
		    0.5f, 5.0f }, 0.0f);

    /**
     * {@link BasicStroke} COMMENT.
     */
    public static final BasicStroke FINE_STROKE = new BasicStroke(0.5f);

    /**
     * {@link Log} the LOG.
     */
    private static final Log LOG = LogFactory.getLog(GraphPlotter.class);

    /**
     * @param offset
     *            {@link Point}
     * @param tuple
     *            {@link ValueTuple}
     * @return {@link Point}
     */
    protected static Point calcDrawingPos(final Point offset,
	    final ValueTuple tuple, final int height, final ValueTuple scale) {
	return new Point(calcDrawingXForA(offset, tuple.getA(), scale.getA()),
		calcDrawingYForB(offset, tuple.getB(), height, scale.getB()));
    }

    /**
     * COMMENT.
     * 
     * @param offset
     * @param a
     * @return
     */
    private static int calcDrawingXForA(final Point offset, final double a,
	    final double scaleX) {
	return (int) (Math.round(a * scaleX) - offset.x);
    }

    /**
     * COMMENT.
     * 
     * @param offset
     * @param b
     * @return
     */
    private static int calcDrawingYForB(final Point offset, final double b,
	    final int height, final double scaleY) {
	return (height - (int) Math.round(b * scaleY)) + offset.y;
    }

    /**
     * @return {@link Point}
     */
    public static Point calcOffset(final ValueTuple finalMin,
	    final ValueTuple scale) {
	final Point offset = new Point((int) Math.round(scale.getA()
		* finalMin.getA()), (int) Math.round(scale.getB()
		* finalMin.getB()) - 1);
	if (GraphPlotter.LOG.isDebugEnabled()) {
	    GraphPlotter.LOG
		    .debug("offset calculation [finalMin: " + finalMin + ", scale: " + scale //$NON-NLS-1$ //$NON-NLS-2$
			    + ", offset: " + offset + "]."); //$NON-NLS-1$ //$NON-NLS-2$
	}
	return offset;
    }

    /**
     * @return {@link Point}
     */
    public static Point calcOrigin(final Point offset, final int height) {
	return new Point(-offset.x, height + offset.y);
    }

    /**
     * @param size
     * @param valueRange
     * @return
     */
    public static ValueTuple calcScale(final Dimension size,
	    final ValueTuple valueRange) {
	final ValueTuple scale = new ValueTuple((size.getWidth() - 1)
		/ valueRange.getA(), (size.getHeight() - 1) / valueRange.getB());
	if (GraphPlotter.LOG.isDebugEnabled()) {
	    GraphPlotter.LOG
		    .debug("scale calculation [size: " + size + ", scale: " + scale //$NON-NLS-1$ //$NON-NLS-2$
			    + "]."); //$NON-NLS-1$
	}
	return scale;
    }

    /**
     * COMMENT.
     * 
     * @return {@link ValueTuple}
     */
    public static ValueTuple createFinalMax(
	    final LinkedList<GraphForComponent> graphs2) {
	ValueTuple finalMax = new ValueTuple(1, 1);
	if (graphs2 != null) {
	    for (final GraphForComponent graph : graphs2) {
		final Graph graphRef = graph.getModel();
		finalMax = new ValueTuple(
			finalMax.getA() > graphRef.getMaxA() ? finalMax.getA()
				: graphRef.getMaxA(),
			finalMax.getB() > graphRef.getMaxB() ? finalMax.getB()
				: graphRef.getMaxB());
	    }
	}
	return finalMax;
    }

    /**
     * @return {@link ValueTuple}
     */
    public static ValueTuple createFinalMin(
	    final LinkedList<GraphForComponent> graphs2) {
	ValueTuple finalMin = null;
	if (graphs2 != null) {
	    for (final GraphForComponent graph : graphs2) {
		final Graph graphRef = graph.getModel();
		finalMin = new ValueTuple(
			(finalMin != null && finalMin.getA() < graphRef
				.getMinA()) ? finalMin.getA() : graphRef
				.getMinA(),
			(finalMin != null && finalMin.getB() < graphRef
				.getMinB()) ? finalMin.getB() : graphRef
				.getMinB());
	    }
	}
	return finalMin;
    }

    /**
     * @return {@link ValueTuple}
     */
    public static ValueTuple createFinalValueRange(final ValueTuple finalMin,
	    final ValueTuple finalMax) {
	final ValueTuple range = new ValueTuple(finalMax.getA()
		- finalMin.getA(), finalMax.getB() - finalMin.getB());
	if (GraphPlotter.LOG.isDebugEnabled()) {
	    GraphPlotter.LOG
		    .debug("range calculation [fmax: " + finalMax + ", fmin: " + finalMin //$NON-NLS-1$ //$NON-NLS-2$
			    + ", range: " + range + "]."); //$NON-NLS-1$//$NON-NLS-2$
	}
	return range;
    }

    /**
     * @param size
     *            {@link Dimension}
     * @param g
     *            {@link Graphics}
     * @param backgroundTitle
     *            {@link String}
     * @param formatterA
     *            {@link Formatter}
     * @param formatterB
     *            {@link Formatter}
     * @param graphs
     *            {@link LinkedList}< {@link GraphForComponent}>
     * @param functionsToPlot
     *            {@link LinkedList}< {@link FunctionToPlot}>
     * @param stepCountA
     *            <code>int</code>
     * @param stepCountB
     *            <code>int</code>
     */
    public static void paintComponent(final Dimension size, final Graphics g,
	    final String backgroundTitle, final Formatter formatterA,
	    final Formatter formatterB,
	    final LinkedList<GraphForComponent> graphs,
	    final LinkedList<FunctionToPlot> functionsToPlot,
	    final int stepCountA, final int stepCountB) {
	final Graphics2D g2d = (Graphics2D) g;
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
	g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
		RenderingHints.VALUE_STROKE_NORMALIZE);

	final ValueTuple finalMin = createFinalMin(graphs);
	final ValueTuple finalMax = createFinalMax(graphs);
	final ValueTuple fvr = createFinalValueRange(finalMin, finalMax);
	final ValueTuple scale = calcScale(size, fvr);

	g.setColor(Color.WHITE);
	g.fillRect(0, 0, size.width, size.height);

	g.setColor(Color.LIGHT_GRAY);
	g.setFont(GraphPlotter.BACKGROUND_FONT);
	final FontMetrics fontMetrics = g
		.getFontMetrics(GraphPlotter.BACKGROUND_FONT);
	g.drawString(backgroundTitle,
		(size.width - fontMetrics.stringWidth(backgroundTitle)) / 2,
		(size.height - fontMetrics.getHeight()) / 2);

	final Point offset = calcOffset(finalMin, scale);
	final Point origin = calcOrigin(offset, size.height);

	g.setColor(Color.BLACK);
	g.drawLine(0, origin.y, size.width, origin.y);
	g.drawLine(0, 0, 0, size.height);
	// g.drawLine(origin.x, 0, origin.x, size.height);

	try {
	    if ((graphs != null) && !graphs.isEmpty()) {
		for (final GraphForComponent graphRef : graphs) {
		    paintGraph(g, g2d, origin, offset, graphRef, size.height,
			    scale);
		}
	    }
	} catch (final Throwable t) {
	    GraphPlotter.LOG.error(t.getLocalizedMessage(), t);
	}
	if ((fvr.getA() != 0) && (fvr.getB() != 0)) {

	    final Iterator<FunctionToPlot> funcIt = functionsToPlot.iterator();
	    while (funcIt.hasNext()) {
		final FunctionToPlot func = funcIt.next();
		g.setColor(func.getColor());
		g2d.setStroke(func.getStroke());
		int lastY = 0;
		for (int i = 0; i <= (size.width + 2); i += 2) {
		    final int y = calcDrawingYForB(offset,
			    func.f(i / scale.getA()), size.height, scale.getB());
		    if (i > 0) {
			g.drawLine(i - 2, lastY, i, y);
		    }
		    lastY = y;
		}

	    }

	    g.setColor(Color.BLACK);
	    g.setFont(GraphPlotter.DEFAULT_FONT);

	    final ValueTuple fm = createFinalMin(graphs);
	    double stepsWidth = fvr.getB() / (stepCountB + 1.0d);
	    for (int i = 1; i < (stepCountB + 1); i++) {
		final double val = (stepsWidth * i) + fm.getB();
		final Point calcDrawingPos = calcDrawingPos(offset,
			new ValueTuple(0, val), size.height, scale);
		g.drawString(formatterB.format(val), 5, calcDrawingPos.y
			+ g.getFontMetrics().getMaxAscent());
	    }

	    double stepCount = 2;
	    if (stepCountA > 0) {
		stepCount = stepCountA;
	    } else {
		stepCount = size.width / 60;
	    }
	    stepsWidth = fvr.getA() / stepCount;
	    for (int i = 0; i < stepCount; i++) {
		final double val = (stepsWidth * i) + fm.getA();
		final Point calcDrawingPos = calcDrawingPos(offset,
			new ValueTuple(val, 0), size.height, scale);
		g.drawString(formatterA.format(val), calcDrawingPos.x + 5,
			origin.y - 5);

		g2d.setStroke(GraphPlotter.DOTTED_STROKE);
		g.drawLine(calcDrawingPos.x, 0, calcDrawingPos.x, size.height);
	    }
	}
    }

    /**
     * @param g
     * @param g2d
     * @param origin
     * @param offset
     * @param graphRef
     */
    private static void paintGraph(final Graphics g, final Graphics2D g2d,
	    final Point origin, final Point offset,
	    final GraphForComponent graphRef, final int height,
	    final ValueTuple scale) {
	ValueTuple last = null;
	final Iterator<ValueTuple> iterator;
	synchronized (graphRef.getModel().getTuples()) {
	    iterator = new LinkedList<ValueTuple>(graphRef.getModel()
		    .getTuples()).iterator();
	}
	g.setColor(graphRef.getColor());
	g2d.setStroke(new BasicStroke(graphRef.getStrokeWidth()));
	while (iterator.hasNext()) {
	    final ValueTuple next = iterator.next();
	    if (last != null) {
		paintPart(g, last, next, origin, offset, graphRef.getColor(),
			graphRef.getFillColor(), height, scale);
	    }
	    last = next;
	}
    }

    /**
     * @param g
     *            {@link Graphics}
     * @param last
     *            {@link ValueTuple}
     * @param next
     *            {@link ValueTuple}
     * @param origin
     *            {@link Point}
     * @param offset
     *            {@link Point}
     */
    private static void paintPart(final Graphics g, final ValueTuple last,
	    final ValueTuple next, final Point origin, final Point offset,
	    final Color color, final Color fillColor, final int height,
	    final ValueTuple scale) {
	final Point p1 = calcDrawingPos(offset, last, height, scale);
	final Point p2 = calcDrawingPos(offset, next, height, scale);

	if (fillColor != null) {
	    g.setColor(fillColor);
	    final int y0 = calcDrawingYForB(offset, 0, height, scale.getB());
	    g.fillPolygon(new Polygon(new int[] { p1.x, p1.x, p2.x, p2.x },
		    new int[] { y0, p1.y, p2.y, y0 }, 4));
	}

	g.setColor(color);
	g.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

}
