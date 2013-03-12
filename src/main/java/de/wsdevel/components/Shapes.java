package de.wsdevel.components;

import java.awt.Polygon;
import java.awt.Shape;

/**
 * Created on 29.04.2009 for project Java__Components. <br/>
 * (c) 2009, wei&szlig;&amp;schmidt - All rights reserved.
 * 
 * @author <a href="mailto:sweiss@weissundschmidt.de">Sebastian A. Wei&szlig; -
 *         wei&szlig;&amp;schmidt</a>
 * @version $Author: sweiss $ -- $Revision: 1.1 $ -- $Date: 2009-05-02 11:26:34 $
 */

public final class Shapes {

    /**
     * @param strokeStrength
     *            <code>double</code>
     * @param dim
     *            <code>int</code>
     * @param relPos
     *            <code>double</code>
     * @return <code>int</code>
     */
    private static int calcCoor(final double strokeStrength, final int dim,
	    final double relPos) {
	return (int) Math.round(strokeStrength + relPos
		* (dim - 2 * strokeStrength));
    }

    /**
     * Creates a simple cross shape.
     * 
     * @param strokeStrength
     *            <code>double</code>
     * @param dimX
     *            <code>int</code>
     * @param dimY
     *            <code>int</code>
     * @param thickness
     *            <code>double</code>
     * @return {@link Shape}
     */
    public static Shape createCrossShape(final double strokeStrength,
	    final int dimX, final int dimY, final double thickness) {
	double min = (1 - thickness) / 2d;
	double max = min + thickness;
	return new Polygon(new int[] { calcCoor(strokeStrength, dimX, min),
		calcCoor(strokeStrength, dimX, max),
		calcCoor(strokeStrength, dimX, max),
		calcCoor(strokeStrength, dimX, 1f),
		calcCoor(strokeStrength, dimX, 1f),
		calcCoor(strokeStrength, dimX, max),
		calcCoor(strokeStrength, dimX, max),
		calcCoor(strokeStrength, dimX, min),
		calcCoor(strokeStrength, dimX, min),
		calcCoor(strokeStrength, dimX, 0),
		calcCoor(strokeStrength, dimX, 0),
		calcCoor(strokeStrength, dimX, min) }, new int[] {
		calcCoor(strokeStrength, dimY, 0),
		calcCoor(strokeStrength, dimY, 0),
		calcCoor(strokeStrength, dimY, min),
		calcCoor(strokeStrength, dimY, min),
		calcCoor(strokeStrength, dimY, max),
		calcCoor(strokeStrength, dimY, max),
		calcCoor(strokeStrength, dimY, 1f),
		calcCoor(strokeStrength, dimY, 1f),
		calcCoor(strokeStrength, dimY, max),
		calcCoor(strokeStrength, dimY, max),
		calcCoor(strokeStrength, dimY, min),
		calcCoor(strokeStrength, dimY, min) }, 12);
    }

    /**
     * Hidden constructor.
     */
    private Shapes() {
    }

}
//
// $Log: Shapes.java,v $
// Revision 1.1  2009-05-02 11:26:34  sweiss
// added JTwoDSpace
//
//
