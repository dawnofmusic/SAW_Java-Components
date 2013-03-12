package de.wsdevel.components;

/**
 * Created on 25.07.2010 for project: SAW_Components__HEAD. (c) 2010, Sebastian
 * A. Weiss - All rights reserved.
 * 
 * @author <a href="mailto:sebastian@scenejo.org">Sebastian A. Weiss -
 *         scenejo.org</a>
 */
public interface WSComponentListener {

    /**
     * COMMENT.
     * 
     * @param evt
     *            {@link WSComponentEvent}
     */
    void componentMoved(WSComponentEvent evt);

    /**
     * COMMENT.
     *
     * @param evt {@link WSComponentEvent}
     */
    void componentResized(WSComponentEvent evt);
}
//
// $Log: $
//
