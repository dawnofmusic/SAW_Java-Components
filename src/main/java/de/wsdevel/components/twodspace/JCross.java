package de.wsdevel.components.twodspace;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import de.wsdevel.components.Shapes;
import de.wsdevel.components.WSComponent;

/**
 * Created on 28.04.2009.
 * 
 * for project: Java__Components
 * 
 * @author <a href="mailto:sweiss@weissundschmidt.de">Sebastian A. Weiss - Weiss
 *         und Schmidt, Mediale Systeme GbR</a>
 * @version $Author: sweiss $ -- $Revision: 1.1 $ -- $Date: 2009-05-02 11:26:34 $
 * 
 * <br>
 *          (c) 2007, Weiss und Schmidt, Mediale Systeme GbR - All rights
 *          reserved.
 * 
 */
public class JCross extends WSComponent<Cross> {

    /**
     * <code>long</code> serial version unique id.
     */
    private static final long serialVersionUID = 8781045058890880800L;

    /**
     * Default constructor.
     */
    public JCross() {
	this(new Cross());
    }

    /**
     * @param cross
     *            {@link Cross} the model.
     */
    public JCross(final Cross cross) {
	setModel(cross);
	setPreferredSize(new Dimension(50, 50));
	setSize(getPreferredSize());
	setRestrictedToParentsVisibleRect(true);
	setMovable(true);
	setSelectable(false);
	addComponentListener(new ComponentAdapter() {
	    @Override
	    public void componentMoved(ComponentEvent e) {
		Cross model = getModel();
		Dimension parentSize = ((JTwoDSpace) getParent()).getSize();
		Dimension size = getSize();
		Point p = getLocation();
		model.setX((2d * p.x / (parentSize.width - size.width)) - 1);
		model
			.setY(-((2d * p.y / (parentSize.height - size.height)) - 1));
	    }
	});
	setHighlightedColor(Color.LIGHT_GRAY);
	setHighlightable(false);
    }

    /**
     * Calculates the location in parentSpace for given cross model.
     * 
     * @param parentSize
     *            {@link Dimension}
     * @param modelRef
     *            {@link Cross}
     * @return {@link Point}
     */
    private Point calcLocationForCross(final Dimension parentSize,
	    final Cross modelRef) {
	int rangeX = parentSize.width - getSize().width;
	int rangeY = parentSize.height - getSize().height;
	return new Point((int) Math
		.round((modelRef.getX() + 1) / 2.0d * rangeX), (int) Math
		.round((1 - modelRef.getY()) * rangeY / 2.0d));
    }

    /**
     * @return {@link Shape}
     * @see de.wsdevel.components.WSComponent#createShape()
     */
    @Override
    protected final Shape createShape() {
	return Shapes.createCrossShape(Math.ceil(getStroke().getLineWidth()),
		getPreferredSize().width, getPreferredSize().height, 0.2d);
    }

    /**
     * @param modelRef
     *            {@link Cross}
     * @see de.wsdevel.components.WSComponent#registerToModel(java.lang.Object)
     */
    @Override
    protected final void registerToModel(final Cross modelRef) {
	JTwoDSpace parent = (JTwoDSpace) getParent();
	if (parent != null) {
	    updateLocation(modelRef, parent);
	}
	// modelRef.addPropertyChangeListener(new PropertyChangeListener() {
	// @SuppressWarnings("synthetic-access")
	// public void propertyChange(final PropertyChangeEvent evt) {
	// Space parentRef = (Space) getParent();
	// if (parentRef != null) {
	// setLocation(calcLocationForCross(parentRef.getSize(),
	// modelRef));
	// }
	// }
	// });
	modelRef.addPropertyChangeListener(new PropertyChangeListener() {
	    public void propertyChange(PropertyChangeEvent evt) {
		Cross model = getModel();

		float greenPart = 0;
		float redPart = 0;
		float bluePart = 0;
		redPart = 1 - (float) (model.getX() + model.getY()) / 2;
		if (redPart > 1) {
		    redPart = 1;
		}
		greenPart = 1 - ((float) (model.getX() + model.getY()) / -2);
		if (greenPart > 1) {
		    greenPart = 1;
		}
		bluePart = 1 - (float) Math.abs((model.getX()));
		if (bluePart < 0) {
		    bluePart = 0;
		}

		setBackground(new Color(redPart, greenPart, bluePart));
	    }
	});

    }

    /**
     * COMMENT.
     * 
     * @param modelRef
     * @param parent
     */
    void updateLocation(final Cross modelRef, JTwoDSpace parent) {
	setLocation(calcLocationForCross(parent.getSize(), modelRef));
    }

}
//
// $Log: JCross.java,v $
// Revision 1.1  2009-05-02 11:26:34  sweiss
// added JTwoDSpace
//
//
