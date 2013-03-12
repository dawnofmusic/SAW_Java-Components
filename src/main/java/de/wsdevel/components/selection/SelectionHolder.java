package de.wsdevel.components.selection;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import de.wsdevel.components.WSComponent;
import de.wsdevel.tools.awt.model.ListWithListModelImpl;

/**
 * Created on 29.03.2008.
 * 
 * for project: Java__Graph
 * 
 * @author <a href="mailto:sweiss@weissundschmidt.de">Sebastian A. Weiss - Weiss
 *         und Schmidt, Mediale Systeme GbR</a>
 * @version $Author: sweiss $ -- $Revision: 1.2 $ -- $Date: 2009-02-09 13:10:30
 *          $
 * 
 * <br>
 *          (c) 2007, Weiss und Schmidt, Mediale Systeme GbR - All rights
 *          reserved.
 * 
 */
public final class SelectionHolder implements PropertyChangeListener {

    /**
     * {@link ListWithListModelImpl}< {@link GraphElementComponent}<?>> the the
     * highlighted elements.
     */
    private final ListWithListModelImpl<WSComponent<?>> highlightedElements = new ListWithListModelImpl<WSComponent<?>>();

    /**
     * {@link ListWithListModelImpl}< {@link GraphElementComponent}<?>> the
     * selected elements.
     */
    private final ListWithListModelImpl<WSComponent<?>> selectedElements = new ListWithListModelImpl<WSComponent<?>>();

    /**
     * @return {@link ListWithListModelImpl<WSComponent<?>>} the
     *         highlightedElements.
     */
    public ListWithListModelImpl<WSComponent<?>> getHighlightedElements() {
	return this.highlightedElements;
    }

    /**
     * @return {@link ListWithListModelImpl<WSComponent<?>>} the
     *         selectedElements.
     */
    public final ListWithListModelImpl<WSComponent<?>> getSelectedElements() {
	return this.selectedElements;
    }

    /**
     * @param evt
     *            {@link PropertyChangeEvent}
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(final PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals(WSComponent.PROPERTY_NAME_SELECTED)) {
	    final WSComponent<?> source = (WSComponent<?>) evt.getSource();
	    if ((Boolean) evt.getNewValue()) {
		this.selectedElements.add(source);
	    } else {
		this.selectedElements.remove(source);
	    }
	}
	if (evt.getPropertyName().equals(WSComponent.PROPERTY_NAME_HIGHLIGHTED)) {
	    final WSComponent<?> source = (WSComponent<?>) evt.getSource();
	    if ((Boolean) evt.getNewValue()) {
		this.highlightedElements.add(source);
	    } else {
		this.highlightedElements.remove(source);
	    }
	}
    }

}
//
// $Log: SelectionHolder.java,v $
// Revision 1.2  2009-12-09 15:41:55  sweiss
// just some added stuff concerning highlighting
//
// Revision 1.1 2009-02-09 13:10:30 sweiss
// fixed antialias stuff and added selection rectangle
//
// Revision 1.1 2009-02-02 16:23:41 sweiss
// *** empty log message ***
//
// Revision 1.2 2008-04-18 11:24:12 sweiss
// *** empty log message ***
//
// Revision 1.1 2008-03-31 09:11:32 sweiss
// really a lot of stuff after weekend of illness
//
//
