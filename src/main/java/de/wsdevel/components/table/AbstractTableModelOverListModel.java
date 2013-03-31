package de.wsdevel.components.table;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Created on 24.10.2003.
 * 
 * for project: tools
 * 
 * (c) 2005, Sebastian A. Weiss - All rights reserved.
 * 
 * @author <a href="mailto:post@sebastian-weiss.de">Sebastian A. Weiss</a>
 * @version $Author: sweissTFH $ -- $Revision: 1.1 $ -- $Date: 2005/10/26
 *          16:56:23 $
 */
public abstract class AbstractTableModelOverListModel<T> extends
		AbstractTableModelOverColumns implements TableModelOverListModel<T> {

	/** {@link long} The serialVersionUID. */
	private static final long serialVersionUID = 6833498331906253470L;

	/**
	 * {@link ListModel}.
	 */
	private ListModel<T> listModel;

	/**
	 * COMMENT.
	 * 
	 * @param listModelVal
	 *            {@link ListModel}
	 */
	public AbstractTableModelOverListModel(final ListModel<T> listModelVal) {
		setListModel(listModelVal);
	}

	/**
	 * {@link ListDataListener}.
	 */
	private ListDataListener listener;

	/**
	 * COMMENT.
	 * 
	 * @return {@link ListDataListener}
	 */
	private ListDataListener getListDataListenerToInnerListModel() {
		if (this.listener == null) {
			this.listener = new ListDataListener() {
				public void contentsChanged(final ListDataEvent e) {
					fireTableRowsUpdated(e.getIndex0(), e.getIndex1());
				}

				public void intervalAdded(final ListDataEvent e) {
					fireTableRowsInserted(e.getIndex0(), e.getIndex1());
				}

				public void intervalRemoved(final ListDataEvent e) {
					fireTableRowsDeleted(e.getIndex0(), e.getIndex1());
				}
			};
		}
		return this.listener;
	}

	/**
	 * COMMENT.
	 * 
	 * @param inner
	 *            {@link ListModel}
	 */
	public final void setListModel(final ListModel<T> inner) {
		if (this.listModel != null) {
			if (getRowCount() > 0) {
				fireTableRowsDeleted(0, getRowCount() - 1);
			}
			this.listModel
					.removeListDataListener(getListDataListenerToInnerListModel());
			this.listModel = null;
		}
		this.listModel = inner;
		this.listModel
				.addListDataListener(getListDataListenerToInnerListModel());
		if (getRowCount() > 0) {
			fireTableRowsInserted(0, getRowCount() - 1);
		}
	}

	// methods depending on underlying data structure -------------------------

	/**
	 * @return <code>int</code>
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public final int getRowCount() {
		return this.listModel.getSize();
	}

	/**
	 * @return {@link ListModel}
	 * @see de.wsdevel.tools.awt.components.table.TableModelOverListModel#getListModel()
	 */
	public final ListModel<T> getListModel() {
		return this.listModel;
	}

}
/*
 * $Log: AbstractTableModelOverListModel.java,v $ Revision 1.1 2006-05-02
 * 16:06:00 sweissTFH cleaned up tools and moved everything to appropriate new
 * packages
 * 
 * Revision 1.4 2006/04/05 18:19:34 sweissTFH cleaned up checkstyle errors
 * Revision 1.3 2005/12/27 16:06:01 sweissTFH moved to java 5 and very big clean
 * up!
 * 
 * Revision 1.2 2005/10/26 16:56:23 mschneiderTFH start of very big clean up and
 * commenting! (sw)
 */
