package de.wsdevel.components;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;

/**
 * Created on 09.04.2008.
 * 
 * for project: Java__Graph
 * 
 * @author <a href="mailto:sweiss@weissundschmidt.de">Sebastian A. Weiss - Weiss
 *         und Schmidt, Mediale Systeme GbR</a>
 * @version $Author: sweiss $ -- $Revision: 1.1 $ -- $Date: 2009-02-09 13:10:29 $
 * 
 * <br>
 *          (c) 2007, Weiss und Schmidt, Mediale Systeme GbR - All rights
 *          reserved.
 * 
 */
public enum StrokeImpl implements Stroke {

    /**
     * {@link StrokeImpl} COMMENT.
     */
    SIMPLE(new BasicStroke(1f)),

    /**
     * {@link StrokeImpl} COMMENT.
     */
    FINE(new BasicStroke(0.5f)),

    /**
     * {@link StrokeImpl} COMMENT.
     */
    FINEST(new BasicStroke(0.3f)),

    /**
     * {@link StrokeImpl} COMMENT.
     */
    DASHED(new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
	    BasicStroke.JOIN_MITER, 10.0f, new float[] { 10.0f, 10.0f }, 0.0f)),

    /**
     * {@link StrokeImpl} COMMENT.
     */
    DOTTED(new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
	    BasicStroke.JOIN_MITER, 10.0f, new float[] { 1f, 5.0f }, 0.0f)),

    /**
     * {@link StrokeImpl} COMMENT.
     */
    FINE_DOTTED(new BasicStroke(0.5f, BasicStroke.CAP_SQUARE,
	    BasicStroke.JOIN_MITER, 10.0f, new float[] { 0.5f, 5.0f }, 0.0f));

    /**
     * {@link BasicStroke} COMMENT.
     */
    private BasicStroke delegate;

    /**
     * @param delegateRef
     *            {@link BasicStroke}
     */
    private StrokeImpl(final BasicStroke delegateRef) {
	this.delegate = delegateRef;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.awt.Stroke#createStrokedShape(java.awt.Shape)
     */
    public final Shape createStrokedShape(final Shape p) {
	return this.delegate.createStrokedShape(p);
    }

    /**
     * @return <code>float</code> width of line.
     * @see java.awt.BasicStroke#getLineWidth()
     */
    public final float getLineWidth() {
	return this.delegate.getLineWidth();
    }

}
//
// $Log: StrokeImpl.java,v $
// Revision 1.1  2009-02-09 13:10:29  sweiss
// fixed antialias stuff and added selection rectangle
//
// Revision 1.2 2008-05-02 10:29:02 sweiss
// next steps
//
// Revision 1.1 2008-04-14 18:33:51 sweiss
// improved grouping
//
//
