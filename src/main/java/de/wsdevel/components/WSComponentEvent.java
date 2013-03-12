package de.wsdevel.components;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Created on 25.07.2010 for project: SAW_Components__HEAD. (c) 2010,
 * Sebastian A. Weiss - All rights reserved.
 * 
 * @author <a href="mailto:sebastian@scenejo.org">Sebastian A. Weiss -
 *         scenejo.org</a>
 */
public class WSComponentEvent {

    /**
     * {@link WSComponent<?>} COMMENT.
     */
    private WSComponent<?> source;

    /**
     * {@link Point} COMMENT.
     */
    private Point locationInParentSpace;

    /**
     * {@link Point} COMMENT.
     */
    private Point locationInRootSpace;

    /**
     * {@link Dimension} COMMENT.
     */
    private Dimension size;

    /**
     * {@link Point2D.Double} COMMENT.
     */
    private Point2D.Double centerInParentSpace;

    /**
     * {@link Point2D.Double} COMMENT.
     */
    private Point2D.Double centerInRootSpace;

    /**
     * COMMENT.
     * 
     * @param sourceRef
     *            {@link WSComponent}
     * @param locationInParentSpaceRef
     *            {@link Point}
     * @param locationInRootSpaceRef
     *            {@link Point}
     */
    public WSComponentEvent(final WSComponent<?> sourceRef,
	    final Point locationInParentSpaceRef,
	    final Point locationInRootSpaceRef,
	    final Point2D.Double centerInParentSpaceRef,
	    final Point2D.Double centerInRootSpaceRef, final Dimension sizeRef) {
	setSource(sourceRef);
	setLocationInParentSpace(locationInParentSpaceRef);
	setLocationInRootSpace(locationInRootSpaceRef);
	setCenterInParentSpace(centerInParentSpaceRef);
	setCenterInRootSpace(centerInRootSpaceRef);
	setSize(sizeRef);
    }

    /**
     * @return {@link Point2D.Double} the centerInParentSpace.
     */
    public final Point2D.Double getCenterInParentSpace() {
	return this.centerInParentSpace;
    }

    /**
     * @return {@link Point2D.Double} the centerInRootSpace.
     */
    public final Point2D.Double getCenterInRootSpace() {
	return this.centerInRootSpace;
    }

    /**
     * @return {@link Point} the locationInParentSpace.
     */
    public final Point getLocationInParentSpace() {
	return this.locationInParentSpace;
    }

    /**
     * @return {@link Point} the locationInRootSpace.
     */
    public final Point getLocationInRootSpace() {
	return this.locationInRootSpace;
    }

    /**
     * @return {@link Dimension} the size.
     */
    public final Dimension getSize() {
	return this.size;
    }

    /**
     * @return {@link WSComponent<?>} the source.
     */
    public final WSComponent<?> getSource() {
	return this.source;
    }

    /**
     * @param centerInParentSpaceRef {@link Point2D.Double} the centerInParentSpace to set.
     */
    public final void setCenterInParentSpace(
	    final Point2D.Double centerInParentSpaceRef) {
	this.centerInParentSpace = centerInParentSpaceRef;
    }

    /**
     * @param centerInRootSpaceRef {@link Point2D.Double} the centerInRootSpace to set.
     */
    public final void setCenterInRootSpace(
	    final Point2D.Double centerInRootSpaceRef) {
	this.centerInRootSpace = centerInRootSpaceRef;
    }

    /**
     * @param locationInParentSpaceRef
     *            {@link Point} the locationInParentSpace to set.
     */
    public final void setLocationInParentSpace(
	    final Point locationInParentSpaceRef) {
	this.locationInParentSpace = locationInParentSpaceRef;
    }

    /**
     * @param locationInRootSpaceRef
     *            {@link Point} the locationInRootSpace to set.
     */
    public final void setLocationInRootSpace(final Point locationInRootSpaceRef) {
	this.locationInRootSpace = locationInRootSpaceRef;
    }

    /**
     * @param sizeRef {@link Dimension} the size to set.
     */
    public final void setSize(final Dimension sizeRef) {
	this.size = sizeRef;
    }

    /**
     * @param sourceRef
     *            {@link WSComponent<?>} the source to set.
     */
    public final void setSource(final WSComponent<?> sourceRef) {
	this.source = sourceRef;
    }

}