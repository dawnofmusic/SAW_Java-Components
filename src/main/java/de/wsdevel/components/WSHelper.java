package de.wsdevel.components;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

/**
 * Created on 25.07.2010 for project: SAW_Components__HEAD.
 * (c) 2010, Sebastian A. Weiss - All rights reserved.
 * @author <a href="mailto:sebastian@scenejo.org">Sebastian A. Weiss - scenejo.org</a>
 */
public final class WSHelper {

    /**
     * Hidden constructor.
     */
    private WSHelper() {
    }

    /**
     * COMMENT.
     * 
     * @param comp {@link Component}
     * @return {@link Point2D.Double}
     */
    public static Point2D.Double getCenterOfComponent(
	    final Point locationInSpace, final Component comp) {
	final Rectangle bounds = comp.getBounds();
	return new Point2D.Double(locationInSpace.x + bounds.width / 2,
		locationInSpace.y + bounds.height / 2);
    }

}
//
// $Log: $
//
