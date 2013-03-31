package de.wsdevel.components.plotter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

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
public class GraphPanel extends JPanel {

	/**
	 * {@link Font} COMMENT.
	 */
	private static final Font BACKGROUND_FONT = new Font("sansserif", //$NON-NLS-1$
			Font.BOLD, 30);

	/**
	 * {@link Font} COMMENT.
	 */
	@SuppressWarnings("nls")
	private static final Font DEFAULT_FONT = new Font("sanserif", Font.PLAIN,
			10);

	/**
	 * {@link Formatter} COMMENT.
	 */
	private static final Formatter DEFAULT_FORMATTER = new Formatter() {
		public String format(double d) {
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
	 * {@link Log} the logger.
	 */
	private static final Log LOG = LogFactory.getLog(GraphPanel.class);

	/**
	 * {@link long} COMMENT.
	 */
	private static final long serialVersionUID = -8612195651541083392L;

	/**
	 * {@link BasicStroke} COMMENT.
	 */
	public static final BasicStroke SIMPLE_STROKE = new BasicStroke(1f);

	/**
	 * {@link String} COMMENT.
	 */
	private String backgroundTitle = ""; //$NON-NLS-1$

	/**
	 * {@link Formatter} COMMENT.
	 */
	private Formatter formaterA = DEFAULT_FORMATTER;

	/**
	 * {@link Formatter} COMMENT.
	 */
	private Formatter formaterB = DEFAULT_FORMATTER;

	/**
	 * {@link LinkedList<Function>} COMMENT.
	 */
	private LinkedList<FunctionToPlot> functionsToPlot = new LinkedList<FunctionToPlot>();

	/**
	 * {@link Graph} COMMENT.
	 */
	private Graph graph = null;

	/**
	 * {@link ValueTuple} COMMENT.
	 */
	private ValueTuple maxValues;

	/**
	 * {@link ValueTuple} COMMENT.
	 */
	private ValueTuple minValues;

	/**
	 * <code>double</code> COMMENT.
	 */
	private double scaleX = 1;

	/**
	 * <code>double</code> COMMENT.
	 */
	private double scaleY = 1;

	/**
	 * <code>int</code> COMMENT.
	 */
	private int stepCountA = 0;

	/**
	 * <code>int</code> COMMENT.
	 */
	private int stepCountB = 5;

	private GraphListener graphListener;

	/**
	 * Default constructor.
	 */
	public GraphPanel() {
		setLayout(null);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				updateScales();
				if (LOG.isDebugEnabled()) {
					LOG.debug(getSize());
				}
				repaint();
			}
		});
		final JLabel label = new JLabel();
		label.setOpaque(false);
		label.setSize(100, 20);
		label.setFont(DEFAULT_FONT);
		add(label);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				label.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				label.setVisible(false);
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			@SuppressWarnings("nls")
			@Override
			public void mouseMoved(MouseEvent e) {
				int newX = e.getX() + 5;
				final Rectangle visibleRect = getVisibleRect();
				if (newX + label.getWidth() > visibleRect.x + visibleRect.width) {
					newX = e.getX() - 5 - label.getWidth();
					label.setHorizontalAlignment(SwingConstants.RIGHT);
				} else {
					label.setHorizontalAlignment(SwingConstants.LEFT);
				}
				label.setLocation(newX, e.getY());

				label.setText("["
						+ getFormaterA().format(calcAForMouseX(e.getX()))
						+ ","
						+ getFormaterB().format(
								(calcOrigin().y - e.getY())
										/ GraphPanel.this.scaleY) + "]");
			}

		});
	}

	/**
	 * @param graphRef
	 *            {@link Graph}
	 */
	public GraphPanel(final Graph graphRef) {
		this();
		setGraph(graphRef);
	}

	/**
	 * COMMENT.
	 * 
	 * @param functionToPlot
	 *            {@link Function}
	 */
	public final void addFunctionToPlot(final FunctionToPlot functionToPlot) {
		this.functionsToPlot.add(functionToPlot);
	}

	/**
	 * COMMENT.
	 * 
	 * @param mouseX
	 *            <code>int</code>
	 * @return <code>double</code>
	 */
	protected double calcAForMouseX(final int mouseX) {
		return createFinalMin().getA() + mouseX / GraphPanel.this.scaleX;
	}

	/**
	 * @param offset
	 *            {@link Point}
	 * @param tuple
	 *            {@link ValueTuple}
	 * @return {@link Point}
	 */
	protected Point calcDrawingPos(final Point offset, final ValueTuple tuple) {
		return new Point(calcDrawingXForA(offset, tuple.getA()),
				calcDrawingYForB(offset, tuple.getB()));
	}

	/**
	 * COMMENT.
	 * 
	 * @param offset
	 * @param a
	 * @return
	 */
	private int calcDrawingXForA(final Point offset, final double a) {
		return (int) (Math.round(a * this.scaleX) - offset.x);
	}

	/**
	 * COMMENT.
	 * 
	 * @param offset
	 * @param b
	 * @return
	 */
	private int calcDrawingYForB(final Point offset, final double b) {
		return getHeight() - (int) Math.round(b * this.scaleY) + offset.y;
	}

	/**
	 * @return {@link Point}
	 */
	private Point calcOffset() {
		final ValueTuple fm = createFinalMin();
		Point offset = new Point((int) Math.round(this.scaleX * fm.getA()),
				(int) Math.round(this.scaleY * fm.getB()) - 1);
		return offset;
	}

	/**
	 * @return {@link Point}
	 */
	private Point calcOrigin() {
		final Point offset = calcOffset();
		return new Point(offset.x, getHeight() + offset.y);
	}

	/**
	 * COMMENT.
	 * 
	 * @return
	 */
	private ValueTuple createFinalMax() {
		if (getGraph() == null) {
			if (getMaxValues() != null) {
				return new ValueTuple(getMaxValues().getA(), getMaxValues()
						.getB());
			}
			return new ValueTuple(1, 1);
		}
		ValueTuple finalMax = new ValueTuple(getGraph().getMaxA(), getGraph()
				.getMaxB());
		if (getMaxValues() != null) {
			final double a = getMaxValues().getA();
			if (a > getGraph().getMaxA()) {
				finalMax.setA(a);
			}
			final double b = getMaxValues().getB();
			if (b > getGraph().getMaxB()) {
				finalMax.setB(b);
			}
		}
		return finalMax;
	}

	/**
	 * @return {@link ValueTuple}
	 */
	private ValueTuple createFinalMin() {
		if (getGraph() == null) {
			if (getMinValues() != null) {
				return new ValueTuple(getMinValues().getA(), getMinValues()
						.getB());
			}
			return new ValueTuple(0, 0);
		}
		ValueTuple finalMin = new ValueTuple(getGraph().getMinA(), getGraph()
				.getMinB());
		if (getMinValues() != null) {
			final double a = getMinValues().getA();
			if (a < getGraph().getMinA()) {
				finalMin.setA(a);
			}
			final double b = getMinValues().getB();
			if (b < getGraph().getMinB()) {
				finalMin.setB(b);
			}
		}
		return finalMin;
	}

	/**
	 * @return {@link ValueTuple}
	 */
	private ValueTuple createFinalValueRange() {
		final ValueTuple fmax = createFinalMax();
		final ValueTuple fmin = createFinalMin();
		return new ValueTuple(fmax.getA() - fmin.getA(), fmax.getB()
				- fmin.getB());
	}

	/**
	 * @return {@link String} the backgroundTitle.
	 */
	public final String getBackgroundTitle() {
		return this.backgroundTitle;
	}

	/**
	 * @return {@link Formatter} the formaterA.
	 */
	public final Formatter getFormaterA() {
		return this.formaterA;
	}

	/**
	 * @return {@link Formatter} the formaterB.
	 */
	public final Formatter getFormaterB() {
		return this.formaterB;
	}

	/**
	 * @return {@link LinkedList<Function>} the functionsToPlot.
	 */
	public final LinkedList<FunctionToPlot> getFunctionsToPlot() {
		return this.functionsToPlot;
	}

	/**
	 * @return {@link Graph} the graph.
	 */
	public final Graph getGraph() {
		return this.graph;
	}

	/**
	 * @return {@link ValueTuple} the maxValues.
	 */
	public final ValueTuple getMaxValues() {
		return this.maxValues;
	}

	/**
	 * @return {@link ValueTuple} the minValues.
	 */
	public final ValueTuple getMinValues() {
		return this.minValues;
	}

	/**
	 * @return {@link int} the stepCountA.
	 */
	public final int getStepCountA() {
		return this.stepCountA;
	}

	/**
	 * @return {@link int} the stepCountB.
	 */
	public final int getStepCountB() {
		return this.stepCountB;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(final Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_NORMALIZE);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.LIGHT_GRAY);
		g.setFont(GraphPanel.BACKGROUND_FONT);
		final FontMetrics fontMetrics = g.getFontMetrics(BACKGROUND_FONT);
		g.drawString(
				getBackgroundTitle(),
				(getWidth() - fontMetrics.stringWidth(getBackgroundTitle())) / 2,
				(getHeight() - fontMetrics.getHeight()) / 2);

		Point origin = calcOrigin();
		Point offset = calcOffset();
		g.setColor(Color.BLACK);
		g.drawLine(0, origin.y, getWidth(), origin.y);
		g.drawLine(origin.x, 0, origin.x, getHeight());

		if (getGraph() != null) {
			ValueTuple last = null;
			final Iterator<ValueTuple> iterator = getGraph().getTuples()
					.iterator();
			g.setColor(getGraph().getColor());
			g2d.setStroke(new BasicStroke(getGraph().getStrokeWidth()));
			while (iterator.hasNext()) {
				final ValueTuple next = iterator.next();
				if (last != null) {
					paintPart(g, last, next, origin, offset);
				}
				last = next;
			}
		}

		final ValueTuple fvr = createFinalValueRange();
		if (this.graph != null && fvr.getA() != 0 && fvr.getB() != 0) {

			final Iterator<FunctionToPlot> funcIt = this.functionsToPlot
					.iterator();
			while (funcIt.hasNext()) {
				final FunctionToPlot func = funcIt.next();
				g.setColor(func.getColor());
				g2d.setStroke(func.getStroke());
				int lastY = 0;
				for (int i = 0; i <= getWidth() + 2; i += 2) {
					final int y = calcDrawingYForB(offset,
							func.f(i / this.scaleX));
					if (i > 0) {
						g.drawLine(i - 2, lastY, i, y);
					}
					lastY = y;
				}

			}

			g.setColor(Color.BLACK);
			g.setFont(GraphPanel.DEFAULT_FONT);

			final ValueTuple fm = createFinalMin();
			double stepsWidth = fvr.getB() / (getStepCountB() + 1.0d);
			for (int i = 1; i < getStepCountB() + 1; i++) {
				final double val = (stepsWidth * i) + fm.getB();
				final Point calcDrawingPos = calcDrawingPos(offset,
						new ValueTuple(0, val));
				g.drawString(getFormaterB().format(val), 5, calcDrawingPos.y
						+ g.getFontMetrics().getMaxAscent());
			}

			double stepCount = 2;
			if (getStepCountA() > 0) {
				stepCount = getStepCountA();
			} else {
				stepCount = getWidth() / 60;
			}
			stepsWidth = fvr.getA() / stepCount;
			for (int i = 0; i < stepCount; i++) {
				final double val = (stepsWidth * i) + fm.getA();
				final Point calcDrawingPos = calcDrawingPos(offset,
						new ValueTuple(val, 0));
				g.drawString(getFormaterA().format(val), calcDrawingPos.x + 5,
						origin.y - 5);

				g2d.setStroke(GraphPanel.DOTTED_STROKE);
				g.drawLine(calcDrawingPos.x, 0, calcDrawingPos.x, getHeight());

			}
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
	protected void paintPart(final Graphics g, final ValueTuple last,
			final ValueTuple next, final Point origin, final Point offset) {
		Point p1 = calcDrawingPos(offset, last);
		Point p2 = calcDrawingPos(offset, next);
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
	}

	/**
	 * @param backgroundTitleRef
	 *            {@link String} the backgroundTitle to set.
	 */
	public final void setBackgroundTitle(final String backgroundTitleRef) {
		this.backgroundTitle = backgroundTitleRef;
	}

	/**
	 * @param formaterARef
	 *            {@link Formatter} the formaterA to set.
	 */
	public final void setFormaterA(final Formatter formaterARef) {
		this.formaterA = formaterARef;
	}

	/**
	 * @param formaterBRef
	 *            {@link Formatter} the formaterB to set.
	 */
	public final void setFormaterB(final Formatter formaterBRef) {
		this.formaterB = formaterBRef;
	}

	/**
	 * @param functionsToPlotRef
	 *            {@link LinkedList<Function>} the functionsToPlot to set.
	 */
	public final void setFunctionsToPlot(
			final LinkedList<FunctionToPlot> functionsToPlotRef) {
		this.functionsToPlot = functionsToPlotRef;
	}

	/**
	 * @param graphRef
	 *            {@link Graph} the graph to set.
	 */
	public final void setGraph(final Graph graphRef) {
		if (this.graph != null) {
			this.graph.removeListener(this.graphListener);
			this.graph = null;
		}
		this.graph = graphRef;
		if (this.graph != null) {
			if (this.graphListener == null) {
				this.graphListener = createGraphListener();
			}
			this.graph.addListener(this.graphListener);
		}
		final Dimension size = getVisibleRect().getSize();
		setMinimumSize(new Dimension(100, 100));
		setPreferredSize(size);
		setSize(size);
		updateForNewGraph(graphRef);
		updateScales();
		repaint();
	}

	/**
	 * COMMENT.
	 * 
	 * @return
	 */
	private GraphListener createGraphListener() {
		return new GraphListener() {
			public void graphChanged() {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
							updateForNewGraph(GraphPanel.this.graph);
							updateScales();
							repaint();
						}
					});
				} catch (InterruptedException e) {
					LOG.error(e.getLocalizedMessage(), LOG.isDebugEnabled() ? e
							: null);
				} catch (InvocationTargetException e) {
					LOG.error(e.getLocalizedMessage(), LOG.isDebugEnabled() ? e
							: null);
				}
			}
		};
	}

	/**
	 * @param maxValuesRef
	 *            {@link ValueTuple} the maxValues to set.
	 */
	public final void setMaxValues(final ValueTuple maxValuesRef) {
		this.maxValues = maxValuesRef;
	}

	/**
	 * @param minValuesRef
	 *            {@link ValueTuple} the minValues to set.
	 */
	public final void setMinValues(final ValueTuple minValuesRef) {
		this.minValues = minValuesRef;
	}

	/**
	 * @param stepCountAVal
	 *            <code>int</code> the stepCountA to set.
	 */
	public final void setStepCountA(final int stepCountAVal) {
		this.stepCountA = stepCountAVal;
	}

	/**
	 * @param stepCountBVal
	 *            <code>int</code> the stepCountB to set.
	 */
	public final void setStepCountB(final int stepCountBVal) {
		this.stepCountB = stepCountBVal;
	}

	/**
	 * To be overwritten by subclasses to update properties after setting a new
	 * graph instance.
	 * 
	 * @param graphRef
	 *            {@link Graph}
	 */
	protected void updateForNewGraph(final Graph graphRef) {
		// to be implemented by subclasses (sw)
	}

	/**
	 * Updating scale factors.
	 */
	private void updateScales() {
		if (this.graph == null) {
			this.scaleX = 1;
			this.scaleY = 1;
		} else {
			final ValueTuple vr = createFinalValueRange();
			this.scaleX = (getWidth() - 1) / vr.getA();
			// System.out.println(this.getClass().getSimpleName());// TODO
			// remove
			// // sysout
			// System.out.println("vr a: " + vr.getA());// TODO remove sysout
			// System.out.println("width: " + getWidth());// TODO remove sysout
			this.scaleY = (getHeight() - 1) / vr.getB();
			if (this.scaleX < 1) {
				// System.out.println("scale is: " + scaleX);// TODO remove
				// sysout
				if (this.scaleY < 1) {
					final Dimension preferredSize = new Dimension(
							(int) Math.round(vr.getA() + 1),
							(int) Math.round(vr.getB() + 1));
					// System.out.println("1: " + preferredSize);// TODO remove
					// // sysout
					setPreferredSize(preferredSize);
					setMinimumSize(preferredSize);
					setSize(preferredSize);
				} else {
					final Dimension preferredSize = new Dimension(
							(int) Math.round(vr.getA() + 1), getHeight());
					// System.out.println("2: " + preferredSize);// TODO remove
					// // sysout
					setPreferredSize(preferredSize);
					setMinimumSize(preferredSize);
					setSize(preferredSize);
				}
			} else if (this.scaleY < 1) {
				final Dimension preferredSize = new Dimension(getWidth(),
						(int) Math.round(vr.getB() + 1));
				// System.out.println("3: " + preferredSize);// TODO remove
				// sysout
				setPreferredSize(preferredSize);
				setMinimumSize(preferredSize);
				setSize(preferredSize);
			}
		}
	}
}
//
// $Log$
// Revision 1.6 2009-04-09 09:57:31 sweiss
// real time logging of probes
//
// Revision 1.5 2009-04-02 08:25:18 sweiss
// starting creating application
//
// Revision 1.4 2009-04-01 11:57:25 sweiss
// added new uistuff
//
// Revision 1.3 2009-03-28 22:01:11 sweiss
// *** empty log message ***
//
// Revision 1.2 2009-03-28 21:22:37 sweiss
// *** empty log message ***
//
// Revision 1.1 2009-03-28 16:09:25 sweiss
// added a lot of graph stuff
//
//
