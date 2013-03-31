package de.wsdevel.components.plotter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JPanel;

/**
 * Created on 26.05.2006.
 * 
 * (c) 2005, Sebastian A. Weiss - All rights reserved.
 * 
 * @author <a href="mailto:post@sebastian-weiss.de">Sebastian A. Weiss</a>
 * @version $Author: sweissTFH $ -- $Revision: 1.3 $ -- $Date: 2006-07-15
 *          14:36:46 $
 */
public class PlotterPanel extends JPanel {

	/** The panels border width. */
	private static final int BORDER = 10;

	/** The panels height. */
	private static final int HEIGHT = 600;

	/** The panels width. */
	private static final int WIDTH = 800;

	/** The prefered size. */
	private static final Dimension PREFERRED_SIZE = new Dimension(WIDTH, HEIGHT);

	/**
	 * {@link int} COMMENT.
	 */
	private static final int WIDTH_EFFECTIVE = WIDTH - 2 * BORDER;

	/**
	 * {@link long} COMMENT.
	 */
	private static final long serialVersionUID = -7728654105943701970L;

	/** The y baseline. */
	private static final Point ORIGIN = new Point(WIDTH / 2, HEIGHT / 2);

	/**
	 * {@link LinkedList}< {@link FunctionToPlot}> COMMENT.
	 */
	private LinkedList<FunctionToPlot> functions = new LinkedList<FunctionToPlot>();

	/**
	 * {@link double} COMMENT.
	 */
	private double scale;

	/**
	 * COMMENT.
	 * 
	 * @param scaleVal
	 *            <code>double</code>
	 */
	public PlotterPanel(final double scaleVal) {
		super();
		this.scale = scaleVal;
	}

	/**
	 * COMMENT.
	 * 
	 * @param function
	 *            {@link FunctionToPlot}
	 */
	public final void addFunction(final FunctionToPlot function) {
		this.functions.add(function);
	}

	/**
	 * @see javax.swing.JComponent#getMinimumSize()
	 * @return {@link Dimension}
	 */
	@Override
	public final Dimension getMinimumSize() {
		return this.getPreferredSize();
	}

	/**
	 * @see javax.swing.JComponent#getPreferredSize()
	 * @return {@link Dimension}
	 */
	@Override
	public final Dimension getPreferredSize() {
		return PREFERRED_SIZE;
	}

	/**
	 * @param g
	 *            {@link Graphics}
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	@Override
	public final void paint(final Graphics g) {
		super.paint(g);
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// axis
		g.setColor(Color.black);
		g.drawLine(BORDER, ORIGIN.y, WIDTH - BORDER, ORIGIN.y);
		g.drawLine(ORIGIN.x, BORDER, ORIGIN.x, HEIGHT - BORDER);
		g.drawOval(ORIGIN.x - 2, ORIGIN.y - 2, 4, 4);

		// 1
		if (this.scale > 1) {
			int y = (int) Math.round(ORIGIN.y - this.scale);
			g.drawLine(ORIGIN.x - 2, y, ORIGIN.x + 2, y);
			y = (int) Math.round(ORIGIN.y + this.scale);
			g.drawLine(ORIGIN.x - 2, y, ORIGIN.x + 2, y);

			int x = (int) Math.round(ORIGIN.x - this.scale);
			g.drawLine(x, ORIGIN.y - 2, x, ORIGIN.y + 2);
			x = (int) Math.round(ORIGIN.x + this.scale);
			g.drawLine(x, ORIGIN.y - 2, x, ORIGIN.y + 2);
		}

		Iterator<FunctionToPlot> it = this.functions.iterator();
		while (it.hasNext()) {
			FunctionToPlot next = it.next();
			drawFunction(g, next);
		}
	}

	/**
	 * @param g
	 *            {@link Graphics}
	 * @param function
	 *            {@link java.lang.Double[]}
	 */
	private void drawFunction(final Graphics g, final FunctionToPlot function) {
		g.setColor(function.getColor());

		int posX = 0;
		int posY = 0;
		int oldPosX = 0;
		int oldPosY = 0;
		double x = 0;
		double y = 0;
		for (int i = 0; i < WIDTH_EFFECTIVE; i++) {
			x = i - WIDTH_EFFECTIVE / (double) 2;
			x = x / this.scale;
			y = function.f(x);
			posX = i + BORDER;
			posY = ORIGIN.y - (int) Math.round(y * this.scale);

			if (i > 0) {
				g.drawLine(oldPosX, oldPosY, posX, posY);
			}

			oldPosX = posX;
			oldPosY = posY;
		}

		// int size = function.size();
		// double xDelta = 1;
		// double oldX = BORDER;
		// double oldY = Y_BASELINE;
		// int i = 0;
		// for (double x = BORDER; x < WIDTH + BORDER; x += xDelta) {
		// if (i > size - 1) {
		// break;
		// }
		// double y = function.get(i * this.scale).doubleValue();
		// g.drawLine((int) Math.round(oldX), Y_BASELINE
		// - (int) Math.round(oldY * this.scale), (int) Math.round(x),
		// Y_BASELINE - (int) Math.round(y * this.scale));
		// oldY = y;
		// oldX = x;
		// i++;
		// }
	}

}
/*
 * $Log: PlotterPanel.java,v $ Revision 1.3 2006-07-15 14:36:46 sweissTFH ***
 * empty log message ***
 * 
 * Revision 1.2 2006/06/10 13:00:32 sweissTFH cleanup and smaller changes due to
 * new compiler settings
 * 
 * Revision 1.1 2006/05/26 18:25:05 sweissTFH added plotter panel and fermi
 * function
 */
