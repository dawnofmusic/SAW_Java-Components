package de.wsdevel.components.plotter;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.wsdevel.tools.math.Graph;
import de.wsdevel.tools.math.ValueTuple;

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

    private ValueTuple finalMax;

    private ValueTuple finalMin;

    private ValueTuple finalValueRange;

    /**
     * {@link Formatter} COMMENT.
     */
    private Formatter formaterA = GraphPlotter.DEFAULT_FORMATTER;

    /**
     * {@link Formatter} COMMENT.
     */
    private Formatter formaterB = GraphPlotter.DEFAULT_FORMATTER;

    /**
     * {@link LinkedList<Function>} COMMENT.
     */
    private LinkedList<FunctionToPlot> functionsToPlot = new LinkedList<FunctionToPlot>();

    /** {@link GraphListener} The graphListener. */
    private GraphListener graphListener;

    /**
     * {@link LinkedList}< {@link GraphForComponent}> graphs.
     */
    private LinkedList<GraphForComponent> graphs = new LinkedList<GraphForComponent>();

    private ValueTuple scale;

    /**
     * <code>int</code> COMMENT.
     */
    private int stepCountA = 0;

    /**
     * <code>int</code> COMMENT.
     */
    private int stepCountB = 5;

    /**
     * Default constructor.
     */
    public GraphPanel() {
	setLayout(null);
	addComponentListener(new ComponentAdapter() {
	    @Override
	    public void componentResized(final ComponentEvent e) {
		updateScales();
		if (GraphPanel.LOG.isDebugEnabled()) {
		    GraphPanel.LOG.debug("GraphPanel resized to " + getSize()); //$NON-NLS-1$
		}
		repaint();
	    }

	    @Override
	    public void componentShown(final ComponentEvent e) {
		updateScales();
		repaint();
	    }
	});
	final JLabel label = new JLabel();
	label.setOpaque(false);
	label.setSize(100, 20);
	label.setFont(GraphPlotter.DEFAULT_FONT);
	add(label);
	addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseEntered(final MouseEvent e) {
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		label.setVisible(true);
	    }

	    @Override
	    public void mouseExited(final MouseEvent e) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		label.setVisible(false);
	    }
	});
	addMouseMotionListener(new MouseAdapter() {
	    @SuppressWarnings("nls")
	    @Override
	    public void mouseMoved(final MouseEvent e) {
		int newX = e.getX() + 5;
		final Rectangle visibleRect = getVisibleRect();
		if ((newX + label.getWidth()) > (visibleRect.x + visibleRect.width)) {
		    newX = e.getX() - 5 - label.getWidth();
		    label.setHorizontalAlignment(SwingConstants.RIGHT);
		} else {
		    label.setHorizontalAlignment(SwingConstants.LEFT);
		}
		label.setLocation(newX, e.getY());

		label.setText("["
			+ getFormaterA().format(
				calcAForMouseX(e.getX(),
					GraphPanel.this.finalMin,
					GraphPanel.this.scale))
			+ ","
			+ getFormaterB().format(
				(GraphPlotter.calcOrigin(GraphPlotter
					.calcOffset(GraphPanel.this.finalMin,
						GraphPanel.this.scale),
					getHeight()).y - e.getY())
					/ GraphPanel.this.scale.getB()) + "]");
	    }
	});
    }

    /**
     * @param graphRef
     *            {@link GraphForComponent}
     */
    public GraphPanel(final GraphForComponent graphRef) {
	this();
	addGraph(graphRef);
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
     * @param graphRef
     *            {@link GraphForComponent} the graph to set.
     */
    public final void addGraph(final GraphForComponent graphRef) {
	// if (this.graph != null) {
	// this.graph.removeListener(this.graphListener);
	// this.graph = null;
	// }
	// this.graph = graphRef;
	if (graphRef != null) {
	    if (this.graphListener == null) {
		this.graphListener = createGraphListener(graphRef.getModel());
	    }
	    graphRef.addListener(this.graphListener);
	    getGraphs().add(graphRef);

	    final Dimension size = getVisibleRect().getSize();
	    setMinimumSize(new Dimension(100, 100));
	    setPreferredSize(size);
	    setSize(size);
	    updateForNewGraph(graphRef.getModel());
	    updateScales();
	    repaint();
	}
    }

    /**
     * COMMENT.
     * 
     * @param mouseX
     *            <code>int</code>
     * @return <code>double</code>
     */
    protected double calcAForMouseX(final int mouseX,
	    final ValueTuple finalMin, final ValueTuple scale) {
	return finalMin.getA() + (mouseX / scale.getA());
    }

    /**
     * COMMENT.
     * 
     * @return
     */
    private GraphListener createGraphListener(final Graph graphRef) {
	return new GraphListener() {
	    @Override
	    public void graphChanged() {
		try {
		    SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
			    updateForNewGraph(graphRef);
			    updateScales();
			    repaint();
			}
		    });
		} catch (final InterruptedException e) {
		    GraphPanel.LOG.error(e.getLocalizedMessage(),
			    GraphPanel.LOG.isDebugEnabled() ? e : null);
		} catch (final InvocationTargetException e) {
		    GraphPanel.LOG.error(e.getLocalizedMessage(),
			    GraphPanel.LOG.isDebugEnabled() ? e : null);
		}
	    }
	};
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
     * @return the graphs
     */
    public LinkedList<GraphForComponent> getGraphs() {
	return this.graphs;
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
	GraphPlotter.paintComponent(getSize(), g, getBackgroundTitle(),
		getFormaterA(), getFormaterB(), getGraphs(),
		getFunctionsToPlot(), getStepCountA(), getStepCountB());
    }

    /**
     * @param backgroundTitleRef
     *            {@link String} the backgroundTitle to set.
     */
    public final void setBackgroundTitle(final String backgroundTitleRef) {
	this.backgroundTitle = backgroundTitleRef;
	repaint();
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
     * @param graphs
     *            the graphs to set
     */
    public void setGraphs(final LinkedList<GraphForComponent> graphs) {
	this.graphs = graphs;
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
     *            {@link GraphForComponent}
     */
    protected void updateForNewGraph(final Graph graphRef) {
	// to be implemented by subclasses (sw)
    }

    /**
     * Updating scale factors.
     */
    private void updateScales() {
	this.finalMin = GraphPlotter.createFinalMin(getGraphs());
	this.finalMax = GraphPlotter.createFinalMax(getGraphs());
	this.finalValueRange = GraphPlotter.createFinalValueRange(
		this.finalMin, this.finalMax);
	this.scale = GraphPlotter.calcScale(getSize(), this.finalValueRange);
	// // if (this.graph == null) {
	// // this.scaleX = 1;
	// // this.scaleY = 1;
	// // } else {
	// // System.out.println(this.getClass().getSimpleName());// TODO
	// // remove
	// // // sysout
	// // System.out.println("vr a: " + vr.getA());// TODO remove sysout
	// // System.out.println("width: " + getWidth());// TODO remove sysout
	// if (this.scale.getA() < 1) {
	// // System.out.println("scale is: " + scaleX);// TODO remove
	// // sysout
	// if (this.scale.getB() < 1) {
	// final Dimension preferredSize = new Dimension(
	// (int) Math.round(this.finalValueRange.getA() + 1),
	// (int) Math.round(this.finalValueRange.getB() + 1));
	// // System.out.println("1: " + preferredSize);// TODO remove
	// // // sysout
	// setPreferredSize(preferredSize);
	// setMinimumSize(preferredSize);
	// setSize(preferredSize);
	// } else {
	// final Dimension preferredSize = new Dimension(
	// (int) Math.round(this.finalValueRange.getA() + 1),
	// getHeight());
	// // System.out.println("2: " + preferredSize);// TODO remove
	// // // sysout
	// setPreferredSize(preferredSize);
	// setMinimumSize(preferredSize);
	// setSize(preferredSize);
	// }
	// } else if (this.scale.getB() < 1) {
	// final Dimension preferredSize = new Dimension(getWidth(),
	// (int) Math.round(this.finalValueRange.getB() + 1));
	// // System.out.println("3: " + preferredSize);// TODO remove
	// // sysout
	// setPreferredSize(preferredSize);
	// setMinimumSize(preferredSize);
	// setSize(preferredSize);
	// }
	// }
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
