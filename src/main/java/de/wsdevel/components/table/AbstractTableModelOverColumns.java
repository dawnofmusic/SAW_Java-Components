package de.wsdevel.components.table;

import java.util.HashMap;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Created on 22.11.2003.
 * 
 * for project: tools
 * 
 * @author <a href="mailto:sweiss@teamforhire.de">Sebastian A. Weiss - team for
 *         hire</a>
 * @version $Author: sweiss $ -- $Revision: 1.6 $ -- $Date: 2009-02-09 16:54:24
 *          $ <br>
 *          (c) 2005, Weiss und Schmidt, Mediale Systeme GbR - All rights
 *          reserved.
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractTableModelOverColumns extends AbstractTableModel
		implements TableModel {

	/**
	 * {@link HashMap}.
	 */
	private HashMap<Integer, Column> columns = new HashMap<Integer, Column>();

	/**
	 * Created on 26.10.2005.
	 * 
	 * for project: tools
	 * 
	 * @author <a href="mailto:sweiss@teamforhire.de">Sebastian A. Weiss - team
	 *         for hire</a>
	 * @version $Author: sweiss $ -- $Revision: 1.6 $ -- $Date: 2005/10/26
	 *          16:56:23 $ <br>
	 *          (c) 2005, Weiss und Schmidt, Mediale Systeme GbR - All rights
	 *          reserved.
	 * 
	 */
	protected static interface Column {
		/**
		 * COMMENT.
		 * 
		 * @return {@link String}
		 */
		String getName();

		/**
		 * COMMENT.
		 * 
		 * @param index
		 *            <code>int</code>
		 * @return {@link Object}
		 */
		Object getValue(int index);

		/**
		 * COMMENT.
		 * 
		 * @return {@link Class}
		 */
		Class<?> getColumnClass();

		/**
		 * @return <code>boolean</code> <code>true</code> if cells in this
		 *         column can be edited!
		 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
		 */
		boolean isColumnEditable();

		/**
		 * COMMENT.
		 * 
		 * @param value
		 *            {@link Object}
		 * @param row
		 *            <code>int</code>
		 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
		 *      int, int)
		 */
		void setValueAt(Object value, int row);

	}

	/**
	 * Created on 26.10.2005.
	 * 
	 * for project: tools
	 * 
	 * @author <a href="mailto:sweiss@teamforhire.de">Sebastian A. Weiss - team
	 *         for hire</a>
	 * @version $Author: sweiss $ -- $Revision: 1.6 $ -- $Date: 2005/10/26
	 *          16:56:23 $ <br>
	 *          (c) 2005, Weiss und Schmidt, Mediale Systeme GbR - All rights
	 *          reserved.
	 * 
	 */
	protected abstract class DefaultColumn implements Column {
		/**
		 * @return {@link Class}
		 * @see de.wsdevel.tools.awt.components.table.AbstractTableModelOverColumns.Column#getColumnClass()
		 */
		public final Class<?> getColumnClass() {
			return Object.class;
		}
	}

	/**
	 * COMMENT.
	 * 
	 */
	public AbstractTableModelOverColumns() {
		initColumns(this.columns);
	}

	/**
	 * COMMENT.
	 * 
	 * @param columnsMap
	 *            {@link HashMap}< {@link Integer} , {@link Column} >
	 */
	protected abstract void initColumns(HashMap<Integer, Column> columnsMap);

	/**
	 * @return <code>int</code>
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public final int getColumnCount() {
		return this.columns.size();
	}

	/**
	 * @param columnIndex
	 *            <code>int</code>
	 * @return {@link String}
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	@Override
	public final String getColumnName(final int columnIndex) {
		return this.columns.get(Integer.valueOf(columnIndex)).getName();
	}

	/**
	 * @param columnIndex
	 *            <code>int</code>
	 * @return {@link Class}
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	@Override
	public final Class<?> getColumnClass(final int columnIndex) {
		return this.columns.get(Integer.valueOf(columnIndex)).getColumnClass();
	}

	/**
	 * @param columnIndex
	 *            <code>int</code>
	 * @param rowIndex
	 *            <code>int</code>
	 * @return {@link Object}
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public final Object getValueAt(final int rowIndex, final int columnIndex) {
		return this.columns.get(Integer.valueOf(columnIndex))
				.getValue(rowIndex);
	}

	/**
	 * @param row
	 *            <code>int</code>
	 * @param col
	 *            <code>int</code>
	 * @return <code>boolean</code>
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public final boolean isCellEditable(final int row, final int col) {
		return this.columns.get(Integer.valueOf(col)).isColumnEditable();
	}

	/**
	 * @param value
	 *            {@link Object}
	 * @param row
	 *            <code>int</code>
	 * @param col
	 *            <code>int</code>
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
	 *      int, int)
	 */
	@Override
	public void setValueAt(final Object value, final int row, final int col) {
		this.columns.get(col).setValueAt(value, row);
	}
}
//
// $Log: AbstractTableModelOverColumns.java,v $
// Revision 1.6 2009-02-09 16:54:24 sweiss
// bug fixing and cleanup
//
// Revision 1.5 2008-01-16 19:01:41 sweiss
// extension of edit functionality
//
// Revision 1.4 2008-01-16 18:24:43 sweiss
// added isColumnEditable
//
//
// Revision 1.3 2006-11-02 11:25:49 sweissTFH
// move to JDK 5
//
// Revision 1.2 2006/06/10 13:00:32 sweissTFH
// cleanup and smaller changes due to
// new compiler settings
//
// Revision 1.1 2006/05/02 16:06:00 sweissTFH
// cleaned up tools and moved
// everything to appropriate new packages
//
// Revision 1.4 2006/04/05 18:19:34 sweissTFH
// cleaned up checkstyle errors
//
// Revision 1.3 2005/12/27 16:06:01 sweissTFH
// moved to java 5 and very big clean
// up!
//
// Revision 1.2 2005/10/26 16:56:23 mschneiderTFH
// start of very big clean up and
// commenting! (sw)
//
//
